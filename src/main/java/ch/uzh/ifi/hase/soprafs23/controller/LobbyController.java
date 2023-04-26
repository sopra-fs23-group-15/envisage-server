package ch.uzh.ifi.hase.soprafs23.controller;



import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    private final PlayerImageService playerImageService;

    LobbyController(LobbyService lobbyService, PlayerService playerService, GameService gameService, RoundService roundService, PlayerScoreService playerScoreService, PlayerImageService playerImageService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.roundService = roundService;
        this.playerScoreService = playerScoreService;
        this.playerImageService = playerImageService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        // create lobby
        Lobby createdLobby = lobbyService.createLobby();
        // for configuring lobby with other settings than default ones
        //TODO: FIX this
        //Lobby updatedLobby = lobbyService.updateLobbyConfiguration(createdLobby.getPin(), lobbyPostDTO);
        // convert internal representation of user back to API


        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }


    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbyGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (Lobby lobby : lobbies) {
            lobbyGetDTOS.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }
        return lobbyGetDTOS;
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
        } catch(MaxPlayersReachedException mpre){
            throw new ResponseStatusException(HttpStatus.CONFLICT, mpre.getMessage());
        } catch(GameInProgressException gipe){
            throw new ResponseStatusException(HttpStatus.CONFLICT, gipe.getMessage());
        }
    }

    // might be not needed anymore as Websocket returns lobby when player joins lobby
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

    @PostMapping("/lobbies/{lobbyId}/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameDTO startGame(@PathVariable long lobbyId) {
        // create game
        Game createdGame = gameService.createGame(lobbyId);
        createdGame.setStatus(GameStatus.IN_PROGRESS);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameDTO(createdGame);
    }

    @GetMapping("/lobbies/{lobbyId}/games")
    @ResponseStatus(HttpStatus.OK)
    public GameDTO getGame(@PathVariable long lobbyId) {
        try {
            Game foundGame = gameService.getGame(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToGameDTO(foundGame);
        } catch (LobbyDoesNotExistException ldne) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ldne.getMessage());
        }
    }

    @PostMapping("/lobbies/{lobbyId}/games/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RoundDTO startRound(@PathVariable long lobbyId) {
        // create round
        try {
            Round roundAddedGame = roundService.createRound(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToRoundDTO(roundAddedGame);
        } catch(LobbyDoesNotExistException ldne){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ldne.getMessage());
        } catch(GameDoesNotExistException gdne){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gdne.getMessage());
        }
    }

    @GetMapping("/lobbies/{lobbyId}/games/{roundId}")
    @ResponseStatus(HttpStatus.OK)
    public RoundDTO getRound(@PathVariable long lobbyId, @PathVariable int roundId) {
        try {
            long gameId = gameService.getGame(lobbyId).getId();
            Round foundRound = roundService.getRound(roundId, gameId);
            return DTOMapper.INSTANCE.convertEntityToRoundDTO(foundRound);
        } catch (RoundDoesNotExistException rdne) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, rdne.getMessage());
        }
    }

    @PutMapping("/lobbies/{lobbyId}/games/{roundId}/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String generateImages (@PathVariable long lobbyId, @PathVariable int roundId, @PathVariable String username, @RequestBody KeywordsDTO keywordsDTO){
        try{
        Keywords keywords = DTOMapper.INSTANCE.convertKeywordsDTOtoEntity(keywordsDTO);
        return  playerImageService.createImage(keywords, lobbyId, roundId, username);
        } catch (PlayerDoesNotExist pde){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, pde.getMessage());
        } catch (GameDoesNotExistException gme){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gme.getMessage());
        } catch (RoundDoesNotExistException rdne){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, rdne.getMessage());
        }

    }



    @PutMapping("/lobbies/{lobbyId}/games/votes")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDTO scoreUpdate(@PathVariable long lobbyId, @RequestBody PlayerScoreDTO playerScoreDTO){
        try {
//            Game foundGame = gameService.getGame(lobbyId);
            PlayerScore playerScore = DTOMapper.INSTANCE.convertPlayerScoreDTOtoEntity(playerScoreDTO);
//            foundGame.setPlayerScore(playerScore);
            Game updatedGame = playerScoreService.updatePlayerScore(lobbyId, playerScore);
            return DTOMapper.INSTANCE.convertEntityToGameDTO(updatedGame);
        } catch (LobbyDoesNotExistException ldne) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ldne.getMessage());
        }
    }



}
