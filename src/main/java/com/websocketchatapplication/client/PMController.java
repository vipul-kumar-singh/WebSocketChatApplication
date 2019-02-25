package com.websocketchatapplication.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.websocketchatapplication.server.WSController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.concurrent.BrokenBarrierException;

@RestController
public class PMController {

    private final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping(value = "/send")
    public ResponseEntity send(@RequestBody Message message) throws JsonProcessingException {
        LOGGER.info("PMController send..");

        if (!(webSocketService.sendMessage(message)))
            return new ResponseEntity<>(new ResponseEntityBody("Not Connected"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ResponseEntityBody("Success"), HttpStatus.OK);
    }

    @GetMapping(value = "/disconnectAll")
    public ResponseEntity disconnectAll() {
        LOGGER.info("PMController disconnectAll..");

        if (!webSocketService.disconnectAll())
            return new ResponseEntity<>(new ResponseEntityBody("Connection Already Closed"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ResponseEntityBody("Disconnected Successfully"), HttpStatus.OK);
    }

    @GetMapping(value = "/connectUser/{username}")
    public ResponseEntity connectUser(@PathVariable String username) throws InterruptedException, BrokenBarrierException {
        LOGGER.info("PMController connectUser..");

        webSocketService.webSocketConnection(username);
        return new ResponseEntity<>(new ResponseEntityBody("Connection Successful"), HttpStatus.OK);
    }

    @GetMapping(value = "/disconnectUser/{username}")
    public ResponseEntity disconnectUser(@PathVariable String username) {
        LOGGER.info("PMController disconnectUser..");

        if (!webSocketService.disconnect(username))
            return new ResponseEntity<>(new ResponseEntityBody("Connection Already Closed"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ResponseEntityBody("Disconnected Successfully"), HttpStatus.OK);
    }
}
