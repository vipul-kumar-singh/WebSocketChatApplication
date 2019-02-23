package com.websocketchatapplication.server;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WSController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public boolean receive(@RequestBody String message) throws Exception {
        System.out.println(message + "\n");
        return true;
    }
}