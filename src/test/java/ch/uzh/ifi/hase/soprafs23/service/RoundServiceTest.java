package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoundDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoundServiceTest {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameService gameService;

    @Autowired
    private RoundService roundService;

    @Test
    public void getRound(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i< EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        Game game = gameService.createGame(lobby.getPin());
        assertEquals(1, roundService.getRound(1, game.getId()).getRoundNumber());
        assertNotNull(roundService.getRound(1, game.getId()).getGame());
    }

    @Test
    public void getRound_roundDoesNotExist(){
        assertThrows(RoundDoesNotExistException.class, () -> roundService.getRound(1, 1));
    }

    @Test
    public void createRound(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i< EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        Game game = gameService.createGame(lobby.getPin());
        Round newRound = roundService.createRound(lobby.getPin());
        assertEquals(2, newRound.getRoundNumber());
        assertEquals(2, newRound.getGame().getRounds().size());
        assertNotNull(newRound.getGame());
    }

    @Test
    public void createRound_lobbyDoesNotExist(){
        assertThrows(LobbyDoesNotExistException.class, () -> roundService.createRound(1));
    }

    @Test
    public void createRound_gameDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i< EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        assertThrows(GameDoesNotExistException.class, () -> roundService.createRound(lobby.getPin()));
    }
}
