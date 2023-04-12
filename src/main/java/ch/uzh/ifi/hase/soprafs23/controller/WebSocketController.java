package ch.uzh.ifi.hase.soprafs23.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    @MessageMapping("/hello")
    //return value is broadcast to all subscribers of _____
    @SendTo("/Bye")
    public void hello(){

    }
}
