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
public class PlayerServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Autowired
    PlayerService playerService;

    @Test
    public void addPlayer(){
        Player player = new Player();
        player.setUserName("testplayer1");

        Lobby lobby = lobbyService.createLobby();
        Player savedPlayer = playerService.addPlayer(player, lobby.getPin());
        assertNotNull(savedPlayer);
        lobby = lobbyService.findLobby(lobby.getPin());
        Player foundPlayer = lobby.getPlayers().get(0);
        assertEquals(foundPlayer.getUserName(), player.getUserName());
        assertTrue(foundPlayer.isLobbyCreator());
    }
}
