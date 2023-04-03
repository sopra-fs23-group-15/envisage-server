package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    public void getGameTest_LobbyExists(){
/*        Lobby lobby = new Lobby();
        lobby.setPin(1L);
        Game game = new Game();
        game.setLobby(lobby);
        assertEquals(gameService.getGame(lobby.getPin()).getLobby().getPin(), lobby.getPin());*/
    }

}
