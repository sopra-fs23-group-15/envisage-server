package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
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

    LobbyController(LobbyService lobbyService, PlayerService playerService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
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
        boolean pinExists = lobbyService.checkIfPinExists(lobbyId);

        if(pinExists){
            // create user
            Player createdPlayer = playerService.addPlayer(playerInput, lobbyId);
            // convert internal representation of user back to API
            return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(createdPlayer);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLobby(@PathVariable long lobbyId){
        Lobby foundLobby = lobbyService.findLobby(lobbyId);
        if(foundLobby==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else{
            return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(foundLobby);
        }
    }
}