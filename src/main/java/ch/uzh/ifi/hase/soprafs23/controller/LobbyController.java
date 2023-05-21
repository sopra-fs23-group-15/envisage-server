package ch.uzh.ifi.hase.soprafs23.controller;



import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
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

    // creates new lobby
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        // configuring lobby with other settings than default ones
        int numberOfRounds =
                (lobbyPostDTO.getNoOfRounds()>0)?lobbyPostDTO.getNoOfRounds(): EnvisageConstants.DEFAULT_NO_OF_ROUNDS;
        int roundDuration =
                (lobbyPostDTO.getRoundDurationInSeconds()>0)?lobbyPostDTO.getRoundDurationInSeconds(): EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS;
        // create lobby
        Lobby createdLobby = lobbyService.createLobby(numberOfRounds, roundDuration);

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    // retrieves users
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

    // adds a player to the lobby (throws 404 if no such lobby exists or 409 in case of other conflicts)
    @PostMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetDTO addPlayer(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable long lobbyId) {
        // convert API user to internal representation
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        try{
            // create user
            Player createdPlayer = playerService.addPlayer(playerInput, lobbyId);
            return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(createdPlayer);
        } catch(LobbyDoesNotExistException lde){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, lde.getMessage());
        } catch(DuplicateUserException | MaxPlayersReachedException | GameInProgressException exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
    }


    // retrieves lobby (throws 404 if no such lobby exists)
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

    // delete a player from a lobby and delete lobby when no player is left
    @DeleteMapping("/lobbies/{lobbyId}/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void removePlayer(@PathVariable long lobbyId, @PathVariable String username) {
        try{
            playerService.removePlayerFromLobby(lobbyId, username);
        } catch (PlayerDoesNotExistException | LobbyDoesNotExistException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }

    }

    // restart game (throws 404 if no such lobby/game exists )
    @PostMapping("/lobbies/{lobbyId}/games/restarts")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDTO restartGame(@PathVariable long lobbyId){
        try {
            gameService.restartGame(lobbyId);
            Game newGame = gameService.createGame(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToGameDTO(newGame);
        } catch(LobbyDoesNotExistException |GameDoesNotExistException exmsg){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exmsg.getMessage());
        } catch(NotEnoughPlayersException  | GameAlreadyExistsException msg){
            throw new ResponseStatusException(HttpStatus.CONFLICT, msg.getMessage());
        }
    }

    // starts a game (throws 404 if no such lobby exists or 409 when there are not enough players)
    @PostMapping("/lobbies/{lobbyId}/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameDTO startGame(@PathVariable long lobbyId) {
        try{
            // create game
            Game createdGame = gameService.createGame(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToGameDTO(createdGame);
        } catch(LobbyDoesNotExistException ldne){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ldne.getMessage());
        } catch(NotEnoughPlayersException  | GameAlreadyExistsException msg){
            throw new ResponseStatusException(HttpStatus.CONFLICT, msg.getMessage());
        }
    }

    // retrieves game (throws 404 if no such lobby exists)
    @GetMapping("/lobbies/{lobbyId}/games")
    @ResponseStatus(HttpStatus.OK)
    public GameDTO getGame(@PathVariable long lobbyId) {
        try {
            Game foundGame = gameService.getGame(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToGameDTO(foundGame);
        } catch (LobbyDoesNotExistException ldne) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ldne.getMessage());
        }
    }

    // starts a round (throws 404 if no such lobby or game exists)
    @PostMapping("/lobbies/{lobbyId}/games/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RoundDTO startRound(@PathVariable long lobbyId) {
        // create round
        try {
            Round roundAddedGame = roundService.createRound(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToRoundDTO(roundAddedGame);
        } catch(LobbyDoesNotExistException | GameDoesNotExistException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    // retrieves a round (throws 404 if no such round exists)
    @GetMapping("/lobbies/{lobbyId}/games/{roundId}")
    @ResponseStatus(HttpStatus.OK)
    public RoundDTO getRound(@PathVariable long lobbyId, @PathVariable int roundId) {
        try {
            long gameId = gameService.getGame(lobbyId).getId();
            Round foundRound = roundService.getRound(roundId, gameId);
            return DTOMapper.INSTANCE.convertEntityToRoundDTO(foundRound);
        } catch (LobbyDoesNotExistException | RoundDoesNotExistException exception) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    // generates images (throws 404 if no such player, game or round exists)
    @PutMapping("/lobbies/{lobbyId}/games/{roundId}/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerImageGetDTO generateImages (@PathVariable long lobbyId, @PathVariable int roundId, @PathVariable String username, @RequestBody KeywordsDTO keywordsDTO){
        try{
            Keywords keywords = DTOMapper.INSTANCE.convertKeywordsDTOtoEntity(keywordsDTO);
            PlayerImage playerImage = playerImageService.createImage(keywords, lobbyId, roundId, username);
            return DTOMapper.INSTANCE.convertEntityToPlayerImageGetDTO(playerImage);
        } catch (PlayerDoesNotExistException | GameDoesNotExistException | RoundDoesNotExistException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (KeywordsLimitException | PlayerImageDuplicateException exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
    }

    // retrieves images (throws 404 if images do not exist)
    @GetMapping("/lobbies/{lobbyId}/games/{roundId}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerImageGetDTO> getImagesForVoting(@PathVariable long lobbyId, @PathVariable int roundId){
        try {
            List<PlayerImage> playerImageList = playerImageService.getImagesFromRound(lobbyId, roundId);
            List<PlayerImageGetDTO> playerImageGetDTOList = new ArrayList<>();
            for (PlayerImage playerImage : playerImageList) {
                playerImageGetDTOList.add(DTOMapper.INSTANCE.convertEntityToPlayerImageGetDTO(playerImage));
            }
            return playerImageGetDTOList;
        } catch (ImagesDontExistException | GameDoesNotExistException | RoundDoesNotExistException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    // retrieves images of a player (throws 404 if no such player exists)
    @GetMapping("/lobbies/{lobbyId}/games/images/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerImageGetDTO> getImagesOfPlayer(@PathVariable long lobbyId, @PathVariable String username){
        try {
            List<PlayerImage> playerImages = playerImageService.getImagesOfPlayer(lobbyId, username);
            List<PlayerImageGetDTO> playerImageGetDTOList = new ArrayList<>();
            for(PlayerImage playerImage : playerImages){
                playerImageGetDTOList.add(DTOMapper.INSTANCE.convertEntityToPlayerImageGetDTO(playerImage));
            }
            return playerImageGetDTOList;
        } catch (PlayerDoesNotExistException pdne){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, pdne.getMessage());
        }
    }

    // updates score (throws 404 if no such lobby, game or playerImage exist)
    @PutMapping("/lobbies/{lobbyId}/games/votes/{imageId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDTO scoreUpdate(@PathVariable long lobbyId, @PathVariable int imageId, @RequestBody PlayerScoreDTO playerScoreDTO) {
        try {
            PlayerScore playerScore = DTOMapper.INSTANCE.convertPlayerScoreDTOtoEntity(playerScoreDTO);
            Game updatedGame = playerScoreService.updatePlayerScore(lobbyId, playerScore);
            playerImageService.updatesVotesImages(imageId, playerScore.getPlayer());
            return DTOMapper.INSTANCE.convertEntityToGameDTO(updatedGame);
        }
        catch (LobbyDoesNotExistException | GameDoesNotExistException | PlayerImageDoesNotExistException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (ImageIdDoesNotMatchPlayerException iidnmp){
            throw new ResponseStatusException(HttpStatus.CONFLICT, iidnmp.getMessage());
        }
    }

    // retrieves winning image (throws 404 if images do not exist)
    @GetMapping("/lobbies/{lobbyId}/games/{roundId}/winners")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerImageGetDTO getWinnerImageOfRound(@PathVariable long lobbyId, @PathVariable int roundId){
        try {
            PlayerImage winningImage = playerImageService.getWinningImage(lobbyId, roundId);
            return DTOMapper.INSTANCE.convertEntityToPlayerImageGetDTO(winningImage);
        } catch (ImagesDontExistException ide){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ide.getMessage());
        }
    }

    // retrieves all winning images (throws 404 if images do not exist)
    @GetMapping("/lobbies/{lobbyId}/games/winners")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerImageGetDTO> getWinningImages(@PathVariable long lobbyId){
        try {
            List<PlayerImage> winningImageList = playerImageService.getAllWinningImages(lobbyId);
            List<PlayerImageGetDTO> winningImageListDTO = new ArrayList<>();
            for (PlayerImage playerImage : winningImageList) {
                winningImageListDTO.add(DTOMapper.INSTANCE.convertEntityToPlayerImageGetDTO(playerImage));
            }
            return winningImageListDTO;
        } catch (ImagesDontExistException | LobbyDoesNotExistException | GameDoesNotExistException | RoundDoesNotExistException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }
}
