package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class LobbyServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Test
    public void createLobbyTest(){
        Lobby lobby = lobbyService.createLobby();
        System.out.println("Pin: " + lobby.getPin());
        assertNotNull(lobby);
    }

    @Test
    public void checkIfLobbyPinExistsTrueTest(){
        Lobby lobby = lobbyService.createLobby();
        assertTrue(lobbyService.checkIfPinExists(lobby.getPin()));
    }

    @Test
    public void checkIfLobbyPinExistsFalseTest() {
        assertFalse(lobbyService.checkIfPinExists(2));
    }

    @Test
    public void addPlayer(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer1");
        Lobby newLobby = lobbyService.addPlayer(player, lobby.getPin());
        assertEquals(newLobby.getPlayers().size(), 1);
    }
}
