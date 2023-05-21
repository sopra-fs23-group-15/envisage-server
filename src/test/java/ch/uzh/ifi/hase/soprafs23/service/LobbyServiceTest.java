package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LobbyServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    PlayerService playerService;

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
        lobbyService.addPlayer(player, lobby.getPin());
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

    @Test
    void getLobbies(){
        Lobby lobby = new Lobby();
        lobby.setPin(123L);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        List<Lobby> lobbyList = lobbyService.getLobbies();

        Lobby lobby2 = new Lobby();
        lobby2.setPin(234L);
        lobbyRepository.save(lobby2);
        lobbyRepository.flush();

        List<Lobby> lobbyList2 = lobbyService.getLobbies();

        assertEquals( lobbyList.size()+1, lobbyList2.size());
    }

    @Test
    void deleteLobby_noLobby(){
        assertThrows(LobbyDoesNotExistException.class, () -> lobbyService.deleteLobby(888888888L));
    }

    @Test
    void deleteLobby_success(){
        Lobby lobby = lobbyService.createLobby();
        long lobbyPin = lobby.getPin();

        Player player = new Player();
        player.setUserName("Anna");
        Player player2 = new Player();
        player2.setUserName("Simon");

        playerService.addPlayer(player, lobbyPin);
        playerService.addPlayer(player2, lobbyPin);

        assertEquals(2, lobby.getPlayers().size());

        playerService.removePlayerFromLobby(lobbyPin, "Simon");
        lobbyService.deleteLobby(lobbyPin);

        assertNotNull(lobbyRepository.findByPin(lobbyPin));

        playerService.removePlayerFromLobby(lobbyPin, "Anna");
        lobbyService.deleteLobby(lobbyPin);

        assertNull(lobbyRepository.findByPin(lobbyPin));

    }
}
