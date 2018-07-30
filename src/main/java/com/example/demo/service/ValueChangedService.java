package com.example.demo.service;

import com.example.demo.data.Shout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class ValueChangedService {

    private SimpMessageSendingOperations messaging;

    @Autowired
    public ValueChangedService(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    public void broadcastValueChanged(){
        messaging.convertAndSend("/topic/valueChanged", new Shout("valueChanged!"));
    }
}
