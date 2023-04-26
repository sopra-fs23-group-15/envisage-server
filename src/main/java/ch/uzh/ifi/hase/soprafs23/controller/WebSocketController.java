package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Challenge;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerImage;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.*;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    private final WebSocketService webSocketService;

    private final LobbyService lobbyService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final RoundService roundService;

    private final DalleAPIService dalleAPIService;
    private final PlayerScoreService playerScoreService;

    private final PlayerImageService playerImageService;

    private final ChallengeService challengeService;


    WebSocketController(WebSocketService webSocketService, LobbyService lobbyService, PlayerService playerService, GameService gameService, RoundService roundService, DalleAPIService dalleAPIService, PlayerScoreService playerScoreService, PlayerImageService playerImageService, ChallengeService challengeService) {
        this.webSocketService = webSocketService;
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.roundService = roundService;
        this.dalleAPIService = dalleAPIService;
        this.playerScoreService = playerScoreService;
        this.playerImageService = playerImageService;
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

    @MessageMapping("/lobbies/{lobbyId}/{roundId}/images")
    @SendTo("/topic/lobbies/{lobbyId}")
    public void getImagesForVoting(@DestinationVariable long lobbyId, @DestinationVariable int roundId){
        long gameId = gameService.getGame(lobbyId).getId();
        Round foundRound = roundService.getRound(roundId, gameId);
        //List<PlayerImage> playerImageList = roundService.getImagesForVotingRound(foundRound);
        //webSocketService.sendMessageToClients("/topic/lobbies/" + lobbyId, playerImageList);
    }

}
