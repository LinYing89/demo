package com.example.demo.service;

import com.example.demo.communication.ServerHandler;
import com.example.demo.data.device.EleInfo;
import com.example.demo.data.device.ValueData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValueChangedService {

    private SimpMessageSendingOperations messaging;

    @Autowired
    public ValueChangedService(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    /**
     * 广播设备值变化
     * @param valueData 值变化的封装
     */
    public void broadcastValueChanged(ValueData valueData){
        messaging.convertAndSend("/topic/valueChanged", valueData);
    }

    /**
     * 广播电力数据, 网页的每一次心跳会调用一次
     */
    public void broadcastEleInfo(){
        EleInfo eleInfo = ServerHandler.findEleInfo();
        if(null != eleInfo) {
            messaging.convertAndSend("/topic/eleInfo", eleInfo);
        }
    }

    /**
     * 刷新所有数据, 网页打开第一次websocket连接后会发送刷新命令,调用此方法
     */
    public void broadcastRefresh(){
        broadcastEleInfo();
        List<ValueData> list = ServerHandler.findValueData();
        if(null != list){
            for(ValueData valueData : list){
                broadcastValueChanged(valueData);
            }
        }
    }
}
