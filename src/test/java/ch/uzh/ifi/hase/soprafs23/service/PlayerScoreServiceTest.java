package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerScore;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class PlayerScoreServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Autowired
    GameService gameService;

    @Autowired
    PlayerScoreService playerScoreService;

    @Test
    void updatePlayerScore(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        Game game = gameService.createGame(lobby.getPin());
        PlayerScore playerScore = game.getPlayerScores().get(0);
        Game savedGame = playerScoreService.updatePlayerScore(lobby.getPin(), playerScore);
        assertEquals(0, savedGame.getPlayerScores().get(0).getScore());
        assertEquals("testplayer1", savedGame.getPlayerScores().get(0).getPlayer().getUserName());
    }

    @Test
    void updatePlayerScore_lobbyDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testUser1");
        player.setLobby(lobby);

        PlayerScore playerScore = new PlayerScore();
        playerScore.setPlayer(player);
        playerScore.setScore(5);
        assertThrows(LobbyDoesNotExistException.class, () -> {playerScoreService.updatePlayerScore(1L, playerScore); });
    }

    @Test
    void updatePlayerScore_gameDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testUser1");
        player.setLobby(lobby);

        PlayerScore playerScore = new PlayerScore();
        playerScore.setPlayer(player);
        playerScore.setScore(5);
        assertThrows(GameDoesNotExistException.class, () -> {playerScoreService.updatePlayerScore(lobby.getPin(), playerScore); });
    }
}