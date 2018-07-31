package com.example.demo.communication;

import com.example.demo.SpringUtil;
import com.example.demo.data.CollectorTerminal;
import com.example.demo.data.DataAddress;
import com.example.demo.data.MsgManager;
import com.example.demo.data.Omnibus;
import com.example.demo.data.device.*;
import com.example.demo.repository.MsgManagerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelHandlerContext channel;

    public static MsgManager msgManager;

    private MsgManagerRepository msgManagerRepository = SpringUtil.getBean(MsgManagerRepository.class);

    public static Device d1;
    public static Device d2;
    public static Device d3;
    public static Device c1;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (null != channel) {
            channel.close();
            channel = null;
        }
        channel = ctx;
        logger.info("new channel " + ctx.channel().id().asShortText());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        try {
            byte[] req = new byte[m.readableBytes()];
            m.readBytes(req);
            //MyClient.getIns().send(req);
            logger.info(bytesToHexString(req));
            int len = (req[0] << 8) | req[1];
            // 数据有效长度不包括长度字节数和通信管理机字节数,长度字节数=2,通信管理机字节数=4
            if (req.length != len + 8) {
                logger.error("长度不匹配");
                return;
            }
            int managerNum = (req[2] << 24) | (req[3] << 16) | (req[4] << 8) | req[5];
            if (null == msgManager) {
                msgManager = msgManagerRepository.findByNum(managerNum);
                setListener(msgManager);
            }

            if (msgManager == null) {
                logger.error("通信管理机不存在-num:" + managerNum);
                return;
            }
            byte[] by;
            by = Arrays.copyOfRange(req, 6, req.length);
            msgManager.handler(by);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
        logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
    }

    public static void send(byte[] by) {
        if (null != channel) {
            channel.writeAndFlush(Unpooled.copiedBuffer(by));
        }
    }

    private void setListener(MsgManager m) {
        if (null == m) {
            return;
        }
        for (Omnibus o : m.getListOmnibus()) {
            for (CollectorTerminal c : o.getListCollectorTerminal()) {
                for (DataAddress d : c.getListDataAddress()) {
                    for (Device dev : d.getListDevice()) {
                        switch (dev.getCoding()) {
                            case "d1":
                                d1 = dev;
                                break;
                            case "d2":
                                d2 = dev;
                                break;
                            case "d3":
                                d3 = dev;
                                break;
                            case "c1":
                                c1 = dev;
                                for (ValueTrigger t : c1.getListValueTrigger()) {
                                    t.setOnTriggedChangedListener(new MyOnTriggedChangedListener());
                                }
                                break;
                        }
                        dev.setOnValueChangedListener(new MyOnValueChangedListener());
                    }
                }
            }
        }
    }

    public static List<ValueData> findValueData(){
        if (null == msgManager) {
            return null;
        }
        List<ValueData> valueDataList = new ArrayList<>();

        for (Omnibus o : msgManager.getListOmnibus()) {
            for (CollectorTerminal c : o.getListCollectorTerminal()) {
                for (DataAddress d : c.getListDataAddress()) {
                    for (Device dev : d.getListDevice()) {
                        ValueData valueData = new ValueData(dev.getCoding(), dev.getValue());
                        valueDataList.add(valueData);
                    }
                }
            }
        }
        return valueDataList;
    }

    public static EleInfo findEleInfo(){
        if (null == msgManager) {
            return null;
        }
        for (Omnibus o : msgManager.getListOmnibus()) {
            for (CollectorTerminal c : o.getListCollectorTerminal()) {
                for (DataAddress d : c.getListDataAddress()) {
                    for (Device dev : d.getListDevice()) {
                        Map<String, Object> map = new HashMap<>();
                        if (dev instanceof Electrical) {
                            return ((Electrical) dev).getEleInfo();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        byte[] b = new byte[]{0x00, 0, 1, 0};
        int managerNum = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];
        System.out.println(managerNum + "?");
    }

}