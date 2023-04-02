package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
* Lobby Controller
* This class is responsible for handling all REST request that are related to
* the lobby.
* The controller will receive the request and delegate the execution to the
* LobbyService and finally return the result.
*/

@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final PlayerService playerService;
    private final GameService gameService;

    private final RoundService roundService;
    private final PlayerScoreService playerScoreService;

    LobbyController(LobbyService lobbyService, PlayerService playerService, GameService gameService, RoundService roundService, PlayerScoreService playerScoreService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.roundService = roundService;
        this.playerScoreService = playerScoreService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby() {
        // create lobby
        Lobby createdLobby= lobbyService.createLobby();
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PostMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetDTO addPlayer(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable long lobbyId) {
        // convert API user to internal representation
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        try{
            // create user
            Player createdPlayer = playerService.addPlayer(playerInput, lobbyId);
            // convert internal representation of user back to API
            return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(createdPlayer);
        } catch(LobbyDoesNotExistException lde){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, lde.getMessage());
        } catch(DuplicateUserException due){
            throw new ResponseStatusException(HttpStatus.CONFLICT, due.getMessage());
        }
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLobby(@PathVariable long lobbyId){
        Lobby foundLobby = lobbyService.findLobby(lobbyId);
        if(foundLobby==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with pin %s does not exist", lobbyId));
        } else{
            return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(foundLobby);
        }
    }

    @PostMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameDTO startGame(@PathVariable long lobbyId) {
        // create game
        Game createdGame = gameService.createGame(lobbyId);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameDTO(createdGame);
    }

    @PostMapping("/lobbies/{lobbyId}/game/{roundId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RoundDTO startRound(@PathVariable long lobbyId, @PathVariable int roundId) {
        // create round
        Round roundAddedGame = roundService.createRound(lobbyId, roundId);
        // convert internal representation of user back to API
//        return DTOMapper.INSTANCE.convertEntityToGameDTO(roundAddedGame);
        return DTOMapper.INSTANCE.convertEntityToRoundDTO(roundAddedGame);
    }

    @PostMapping("/lobbies/{lobbyId}/game/{roundId}/vote")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerScoreDTO doVote(@RequestBody PlayerScoreDTO playerScoreDTO, @PathVariable long lobbyId, @PathVariable long roundId) {
        //player?
        //score?
        //game?
        PlayerScore scores = playerScoreService.setScore(playerScoreDTO, lobbyId, roundId);
        return DTOMapper.INSTANCE.convertEntityToPlayerScoreDTO(scores);
    }
}
