package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerScore;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.NotEnoughPlayersException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private LobbyService lobbyService;


    @Test
    public void getGame(){
       Lobby lobby = lobbyService.createLobby();
       for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
           Player player = new Player();
           player.setUserName("testplayer"+(i+1));
           lobbyService.addPlayer(player, lobby.getPin());
       }
       Game game = gameService.createGame(lobby.getPin());

       assertEquals(lobby.getPin(), gameService.getGame(lobby.getPin()).getLobby().getPin());
    }

    @Test
    public void getGame_lobbyDoesNotExist(){
        assertThrows(LobbyDoesNotExistException.class, () -> {gameService.getGame(1L);});
    }

    @Test
    public void createGame(){
        // create lobby and add min. no. of players necessary to start a game
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        // create game and assert its lobby pin
        Game game = gameService.createGame(lobby.getPin());
        assertEquals(lobby.getPin(), game.getLobby().getPin());

        // assert player scores have been set with the correct information
        for(int i = 0; i<game.getPlayerScores().size(); i++){
            PlayerScore playerScore = game.getPlayerScores().get(i);
            assertEquals(0, playerScore.getScore());
            assertNotNull(playerScore.getGame());
            assertEquals("testplayer"+(i+1), playerScore.getPlayer().getUserName());
        }

        // assert game status has been set and first round is ready
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertEquals(1, game.getRounds().size());
    }

    @Test
    public void createGame_lobbyDoesNotExist(){
        assertThrows(LobbyDoesNotExistException.class, () -> {gameService.createGame(1L);});
    }

    @Test
    public void createGame_notEnoughPlayers(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer");
        lobbyService.addPlayer(player, lobby.getPin());
        assertThrows(NotEnoughPlayersException.class, () -> {gameService.createGame(lobby.getPin());});
    }
}
