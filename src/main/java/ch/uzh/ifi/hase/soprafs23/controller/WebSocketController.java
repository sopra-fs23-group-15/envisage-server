package ch.uzh.ifi.hase.soprafs23.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    // Ignore this stuff, I will remove it when I'm completely done with the websockets
    // @MessageMapping is used for repetitive messaging from server to clients
    @MessageMapping("/lobbyId")
    //return value is broadcast to all subscribers of /topic/{lobbyId}
    @SendTo("/topic/{lobbyId}")
    public String hello(){
        return "hi";
    }
}
