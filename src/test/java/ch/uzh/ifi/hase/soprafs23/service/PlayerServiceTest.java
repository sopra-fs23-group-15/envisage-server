package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameInProgressException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.MaxPlayersReachedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PlayerServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Autowired
    PlayerService playerService;

    @Test
    void addPlayer(){
        Player player = new Player();
        player.setUserName("testplayer1");

        Lobby lobby = lobbyService.createLobby();
        Player savedPlayer = playerService.addPlayer(player, lobby.getPin());
        assertNotNull(savedPlayer);
        lobby = lobbyService.findLobby(lobby.getPin());
        Player foundPlayer = lobby.getPlayers().get(0);
        assertEquals(player.getUserName(), foundPlayer.getUserName());
        assertTrue(foundPlayer.isLobbyCreator());
    }

    @Test
    void addPlayer_lobbyDoesNotExist(){
        Player player = new Player();
        player.setUserName("testplayer1");
        assertThrows(LobbyDoesNotExistException.class, () -> {playerService.addPlayer(player, 1);});
    }

    @Test
    void addPlayer_maxPlayersReached(){
        Lobby lobby = lobbyService.createLobby();
        for(int i =0; i<EnvisageConstants.MAX_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            playerService.addPlayer(player, lobby.getPin());
        }
        Player extraPlayer = new Player();
        extraPlayer.setUserName("testPlayer"+(EnvisageConstants.MAX_PLAYERS+1));
        assertThrows(MaxPlayersReachedException.class, () -> {playerService.addPlayer(extraPlayer, lobby.getPin());});
    }

    @Test
    void addPlayer_gameInProgress(){
        // create a lobby with necessary amount of players to start a game
        Lobby lobby = lobbyService.createLobby();
        for(int i =0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            playerService.addPlayer(player, lobby.getPin());
        }

        // create a game and put its status to "in progress" to mimic a game being started
        Game game = new Game();
        game.setStatus(GameStatus.IN_PROGRESS);
        lobby.setGame(game);

        // assert that GameInProgressException is thrown when a new player tries to join game
        Player player = new Player();
        player.setUserName("testPlayer"+(EnvisageConstants.MIN_PLAYERS+1));
        assertThrows(GameInProgressException.class, () -> {playerService.addPlayer(player, lobby.getPin());});
    }

    @Test
    void addPlayer_duplicateUser(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer");
        playerService.addPlayer(player, lobby.getPin());

        Player duplicateUser = new Player();
        duplicateUser.setUserName(player.getUserName());
        assertThrows(DuplicateUserException.class, () -> {playerService.addPlayer(duplicateUser, lobby.getPin());});
    }
}
