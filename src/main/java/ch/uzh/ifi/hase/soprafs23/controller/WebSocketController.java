package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Challenge;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.*;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final WebSocketService webSocketService;

    private final LobbyService lobbyService;

    private final ChallengeService challengeService;


    WebSocketController(WebSocketService webSocketService, LobbyService lobbyService, ChallengeService challengeService) {
        this.webSocketService = webSocketService;
        this.lobbyService = lobbyService;
        this.challengeService = challengeService;
    }



    // @MessageMapping is used for repetitive messaging from server to clients
    @MessageMapping("/lobbies/{lobbyId}/lobbyJoin")
    //return value is broadcast to all subscribers of /topic/{lobbyId}
    @SendTo("/topic/lobbies/{lobbyId}")
    public void getLobby(@DestinationVariable long lobbyId){
        Lobby foundLobby = lobbyService.findLobby(lobbyId);

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(foundLobby);

        webSocketService.sendMessageToClients("/topic/lobbies/" + lobbyId, lobbyGetDTO);
    }


    @MessageMapping("/lobbies/{lobbyId}/challengeForRounds/{roundId}")
    @SendTo("/topic/lobbies/{lobbyId}/challenges")
    public void getChallenge(@DestinationVariable long lobbyId, @DestinationVariable int roundId){
        Challenge challenge = challengeService.createChallengeForRound(lobbyId, roundId);
        webSocketService.sendMessageToClients("/topic/lobbies/" + lobbyId + "/challenges", challenge);
    }


}
