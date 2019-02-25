package com.websocketchatapplication.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketService {

    private final Log LOGGER = LogFactory.getLog(getClass());

    private static String URL = "ws://localhost:8083/user/";

    List<String> userNameList = new ArrayList<>();

    public boolean sendMessage(Message message) throws JsonProcessingException {
        LOGGER.info("WebSocketService sendMessage..");

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(message);

        StompSession stompSession = MyStompSessionHandler.getStompSessionMap().get(message.getTo());
        if (stompSession == null)
            return false;

        stompSession.send("/app/chat", json);
        System.out.println("Message sent to " + message.getTo());
        return true;
    }

    public boolean disconnect(String name) {
        LOGGER.info("WebSocketService disconnect..");
        StompSession stompSession = MyStompSessionHandler.getStompSessionMap().get(name);
        if (stompSession == null)
            return false;
        MyStompSessionHandler.getStompSessionMap().remove(name);
        stompSession.disconnect();
        return true;
    }

    public void webSocketConnection(String username) {
        LOGGER.info("WebSocketService webSocketConnection..");
        userNameList.add(username);

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient transport = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler(username);
        stompClient.connect(URL, sessionHandler);
    }

    public boolean disconnectAll() {
        LOGGER.info("WebSocketService disconnect..");
        if (MyStompSessionHandler.getStompSessionMap().isEmpty())
            return false;
        for (Map.Entry<String, StompSession> entry : MyStompSessionHandler.getStompSessionMap().entrySet()) {
            entry.getValue().disconnect();
        }
        MyStompSessionHandler.getStompSessionMap().clear();
        return true;
    }
}
