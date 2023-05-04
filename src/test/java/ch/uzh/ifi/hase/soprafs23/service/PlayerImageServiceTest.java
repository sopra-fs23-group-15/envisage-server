package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.*;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerImageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@SpringBootTest
public class PlayerImageServiceTest {

    @Autowired
    PlayerImageService playerImageService;

    @Autowired
    PlayerImageRepository playerImageRepository;

    @Autowired
    LobbyService lobbyService;

    @Autowired
    GameService gameService;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    GameRepository gameRepository;


    @Test
    void createImage_playerDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testUser"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        Game game = gameService.createGame(lobby.getPin());
        Keywords keywords = new Keywords();
        keywords.setKeywords("test");
        assertThrows(PlayerDoesNotExistException.class, () -> playerImageService.createImage(keywords, lobby.getPin(), 1,"testUser50"));
    }

    @Test
    void createImage_gameDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testUser1");
        lobbyService.addPlayer(player, lobby.getPin());
        Keywords keywords = new Keywords();
        keywords.setKeywords("test");
        assertThrows(GameDoesNotExistException.class, () -> playerImageService.createImage(keywords, lobby.getPin(), 1, player.getUserName()));
    }

    @Test
    void createImage_roundDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testUser"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        Game game = gameService.createGame(lobby.getPin());
        Keywords keywords = new Keywords();
        keywords.setKeywords("test");
        assertThrows(RoundDoesNotExistException.class, () -> playerImageService.createImage(keywords, lobby.getPin(), 2, "testUser1"));
    }

    @Test
    void updatesVotesImages_noSuchPlayerImage(){
        assertThrows(PlayerImageDoesNotExistException.class, () -> playerImageService.updatesVotesImages(1));
    }

    @Test
    void getImagesFromRound_Exception(){
        Round round = new Round();
        Game game = new Game();
        Lobby lobby = new Lobby();
        lobby.setPin(12345L);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
        game.setLobby(lobby);
        gameRepository.save(game);
        gameRepository.flush();
        round.setRoundNumber(3);
        round.setGame(game);
        roundRepository.save(round);
        roundRepository.flush();
        assertThrows(ImagesDontExistException.class, () -> playerImageService.getImagesFromRound(12345L, 3));
    }

    @Test
    void getImagesFromRound_success(){
        Round round = new Round();
        Game game = new Game();
        Lobby lobby = new Lobby();
        lobby.setPin(1234L);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
        game.setLobby(lobby);
        gameRepository.save(game);
        gameRepository.flush();
        round.setRoundNumber(1);
        round.setGame(game);
        roundRepository.save(round);
        roundRepository.flush();

        PlayerImage playerImage = new PlayerImage();
        playerImage.setKeywords("Envisage");
        playerImage.setVotes(4);
        playerImage.setRound(round);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        PlayerImage playerImage2 = new PlayerImage();
        playerImage2.setKeywords("Envisage2");
        playerImage2.setVotes(3);
        playerImage2.setRound(round);
        playerImageRepository.save(playerImage2);
        playerImageRepository.flush();

        List<PlayerImage> images = playerImageService.getImagesFromRound(1234L, 1);

        assertEquals(images.size(), 2);

    }


    @Test
    void getWinningImage(){
        PlayerImage playerImage = new PlayerImage();
        Round round = new Round();
        Game game = new Game();
        Lobby lobby = new Lobby();
        lobby.setPin(1234L);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
        game.setLobby(lobby);
        gameRepository.save(game);
        gameRepository.flush();
        round.setRoundNumber(1);
        round.setGame(game);
        roundRepository.save(round);
        roundRepository.flush();
        playerImage.setRound(round);
        playerImage.setKeywords("Envisage");
        playerImage.setVotes(4);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        PlayerImage winner = playerImageService.getWinningImage(1234L, 1);

        assertEquals(winner, playerImage);
    }
}
