package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LobbyServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Test
    void createLobby_configureParameters(){
        Lobby lobby = lobbyService.createLobby(2, 30);
        System.out.println("Pin: " + lobby.getPin());
        assertNotNull(lobby);
        assertEquals(2, lobby.getNumberOfRounds());
        assertEquals(30, lobby.getRoundDuration());
    }

    @Test
    void createLobby(){
        Lobby lobby = lobbyService.createLobby();
        System.out.println("Pin: " + lobby.getPin());
        assertNotNull(lobby);
        assertEquals(5, lobby.getNumberOfRounds());
        assertEquals(60, lobby.getRoundDuration());
    }

    @Test
    void checkIfLobbyPinExists(){
        Lobby lobby = lobbyService.createLobby();
        assertTrue(lobbyService.checkIfPinExists(lobby.getPin()));
    }

    @Test
    void checkIfLobbyPinExists_noSuchPin() {
        assertFalse(lobbyService.checkIfPinExists(2));
    }

    @Test
    void addPlayer(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer1");
        Lobby newLobby = lobbyService.addPlayer(player, lobby.getPin());
        assertEquals(1, newLobby.getPlayers().size());
    }

    @Test
    void addPlayer_noSuchLobby(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer1");
        Lobby newLobby = lobbyService.addPlayer(player, lobby.getPin());
        assertThrows(LobbyDoesNotExistException.class, () -> lobbyService.addPlayer(player, 2));
    }

    @Test
    void addPlayer_duplicateUser(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer1");
        Lobby newLobby = lobbyService.addPlayer(player, lobby.getPin());

        Player duplicatePlayer = new Player();
        duplicatePlayer.setUserName("testplayer1");
        assertThrows(DuplicateUserException.class, () -> lobbyService.addPlayer(duplicatePlayer, newLobby.getPin()));
    }
}
