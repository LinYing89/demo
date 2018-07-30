package com.example.demo.controller;

import com.example.demo.data.Shout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebSocketController {

    private Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Shout greeting(Shout message) throws Exception {
        logger.info(message.getMessage());
        Thread.sleep(2000); // simulated delay
        return new Shout("Hello, " + HtmlUtils.htmlEscape(message.getMessage()) + "!");
    }
}
