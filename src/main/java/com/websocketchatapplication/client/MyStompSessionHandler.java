package com.websocketchatapplication.client;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private final Log LOGGER = LogFactory.getLog(getClass());
    private static Map<String,StompSession> stompSessionMap = new HashMap<>();
    private static String username;

    public MyStompSessionHandler(String username) {
        this.username = username;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("MyStompSessionHandler afterConnected..");
        System.out.println("New session established : " + session.getSessionId());
        session.subscribe("/user/topic/messages", this);
        System.out.println("Subscribed to /topic/messages");
        stompSessionMap.put(username,session);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        LOGGER.info("MyStompSessionHandler handleException..");
        System.out.println("Got an exception" + exception.toString());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        LOGGER.info("MyStompSessionHandler getPayloadType..");
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LOGGER.info("MyStompSessionHandler handleFrame..");
        Message message = (Message) payload;
        System.out.println("Received : " + message);
    }

    public static Map<String, StompSession> getStompSessionMap() {
        return stompSessionMap;
    }

    public static void setStompSessionMap(Map<String, StompSession> stompSessionMap) {
        MyStompSessionHandler.stompSessionMap = stompSessionMap;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MyStompSessionHandler.username = username;
    }
}