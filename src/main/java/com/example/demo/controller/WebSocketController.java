package com.example.demo.controller;

import com.example.demo.communication.ServerHandler;
import com.example.demo.data.Shout;
import com.example.demo.data.device.EleInfo;
import com.example.demo.service.ValueChangedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private ValueChangedService valueChangedService;

    @MessageMapping("/refresh")
    public void greeting() {
        logger.info("refresh");
        valueChangedService.broadcastRefresh();
    }

    @MessageMapping("/eleInfo")
    @SendTo("/topic/eleInfo")
    public void eleInfo(Shout message) {
        logger.info(message.getMessage());
        valueChangedService.broadcastEleInfo();
    }
}
