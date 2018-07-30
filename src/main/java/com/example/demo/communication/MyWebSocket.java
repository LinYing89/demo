package com.example.demo.communication;

import com.example.demo.data.CompareSymbol;
import com.example.demo.data.Untils;
import com.example.demo.data.device.DevSwitch;
import com.example.demo.data.device.ValueTrigger;
import com.example.demo.repository.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/websocket")
public class MyWebSocket {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private DeviceRepository deviceRepository;

    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        MyWebSocketHelper.addGroupWebSocket(this);
    }

    @OnClose
    public void onClose() {
        closeWebSocket();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        analysisMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        System.out.println(thr.getMessage());
        closeWebSocket();
    }

    private void closeWebSocket() {
        MyWebSocketHelper.removeGroupWebSocket(this);
        session = null;
    }

    public void sendMap(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(map);
            if (null != json) {
                sendMessage(json);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (null != session) {
            try {
                logger.info(message);
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void analysisMessage(String msg) {
        if(msg.equals("H")) {
            sendMessage("H");
            ServerHandler.refreshEleInfo(this, ServerHandler.msgManager);
//			StartUpListener.testEleInfo();
        }else if (msg.equals("rf")) {
            refreshValues();
            return;
        }else if(msg.startsWith("{")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<?, ?> map = mapper.readValue(msg, Map.class);
                float great = Float.parseFloat(map.get("great").toString());
                float less = Float.parseFloat(map.get("less").toString());
                for(ValueTrigger t : ServerHandler.c1.getListValueTrigger()) {
                    if(t.getCompareSymbol() == CompareSymbol.GREAT) {
                        t.setTriggerValue(great);
                    }else {
                        t.setTriggerValue(less);
                    }
                }

                deviceRepository.save(ServerHandler.c1);
                Map<String, Object> map1 = new HashMap<>();
                //2设置成功
                map1.put("id", 2);
                sendMap(map1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        byte[] by = null;
        //最终发出去的数组
        byte[] byMsg = null;
        byte action;
        switch (msg) {
            case "d1":
                action = ((DevSwitch)ServerHandler.d1).getAction();
                by = new byte[] { 00, 07, 00, 00, 00, 04, 00, 00, 05, 00, 05, action, 00};
                break;
            case "d2":
                action = ((DevSwitch)ServerHandler.d2).getAction();
                by = new byte[] { 00, 07, 00, 00, 00, 04, 00, 00, 05, 00, 06, action, 00};
                break;
            case "d3":
                action = ((DevSwitch)ServerHandler.d3).getAction();
                by = new byte[] { 00, 07, 00, 00, 00, 04, 00, 00, 05, 00, 07, action, 00};
                break;
        }
        byMsg = Untils.createByteMsg(by);
        if (null != byMsg) {
            logger.info(ServerHandler.bytesToHexString(byMsg));
            ServerHandler.send(byMsg);
        }
    }

    private void refreshValues() {
        ServerHandler.refreshValues(ServerHandler.msgManager);
    }

}