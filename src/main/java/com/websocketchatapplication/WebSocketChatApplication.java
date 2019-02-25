package com.websocketchatapplication;

import com.websocketchatapplication.client.MyStompSessionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebSocketChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketChatApplication.class, args);

        if (!MyStompSessionHandler.getStompSessionMap().isEmpty())
            MyStompSessionHandler.getStompSessionMap().clear();
    }

}
