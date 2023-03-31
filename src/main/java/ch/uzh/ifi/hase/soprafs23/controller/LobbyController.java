package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public PlayerGetDTO createPlayer(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable long lobbyId) {
        // convert API user to internal representation
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        lobbyService.checkIfLobbyIdExists(lobbyId);
        // create user
        Player createdPlayer = playerService.createPlayer(playerInput, lobbyId);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(createdPlayer);
    }


}
