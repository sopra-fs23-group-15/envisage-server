package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class WebSocketController {

    private final WebSocketService webSocketService;

    private final LobbyService lobbyService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final RoundService roundService;

    private final DalleAPIService dalleAPIService;
    private final PlayerScoreService playerScoreService;

    private final MetMuseumAPIService metMuseumAPIService;


    WebSocketController(WebSocketService webSocketService, LobbyService lobbyService, PlayerService playerService, GameService gameService, RoundService roundService, DalleAPIService dalleAPIService, PlayerScoreService playerScoreService, MetMuseumAPIService metMuseumAPIService) {
        this.webSocketService = webSocketService;
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.roundService = roundService;
        this.dalleAPIService = dalleAPIService;
        this.playerScoreService = playerScoreService;
        this.metMuseumAPIService = metMuseumAPIService;
    }



    // @MessageMapping is used for repetitive messaging from server to clients
    @MessageMapping("/lobbies/{lobbyId}/join")
    //return value is broadcast to all subscribers of /topic/{lobbyId}
    @SendTo("/topic/lobbies/{lobbyId}")
    //@SubscribeMapping("/topic/lobbies/{lobbyId}")
    public void getLobby(@DestinationVariable long lobbyId){
        Lobby foundLobby = lobbyService.findLobby(lobbyId);

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(foundLobby);

        webSocketService.sendMessageToClients("/topic/lobbies/" + lobbyId, lobbyGetDTO);

    }
}
