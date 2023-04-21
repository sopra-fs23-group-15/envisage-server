package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.transaction.Transactional;

@Service
@Transactional
public class WebSocketService {

    protected final LobbyRepository lobbyRepository;
    private final LobbyService lobbyService;


    @Autowired
    protected SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(@Qualifier("lobbyRepository")LobbyRepository lobbyRepository, @Lazy LobbyService lobbyService) {
        this.lobbyRepository = lobbyRepository;
        this.lobbyService = lobbyService;
    }

    public void sendMessageToClients(String destination, Object objectDTO) {
        this.simpMessagingTemplate.convertAndSend(destination, objectDTO);
    }
}
