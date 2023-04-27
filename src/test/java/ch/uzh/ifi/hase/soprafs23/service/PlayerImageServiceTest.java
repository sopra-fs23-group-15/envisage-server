package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.ImagesDontExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.PlayerDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoundDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerImageRepository;
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
    LobbyService lobbyService;

    @Autowired
    GameService gameService;



    @Autowired
    PlayerImageRepository playerImageRepository;



    @Test
    public void createImage_playerDoesNotExist(){
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
    public void createImage_gameDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testUser1");
        lobbyService.addPlayer(player, lobby.getPin());
        Keywords keywords = new Keywords();
        keywords.setKeywords("test");
        assertThrows(GameDoesNotExistException.class, () -> playerImageService.createImage(keywords, lobby.getPin(), 1, player.getUserName()));
    }

    @Test
    public void createImage_roundDoesNotExist(){
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
    public void getImagesFromRound_Exception(){
        assertThrows(ImagesDontExistException.class, () -> playerImageService.getImagesFromRound(12345L, 3));
    }

    @Test
    public void getImagesFromRound_success(){
        PlayerImage playerImage = new PlayerImage();
        playerImage.setLobbyId(1234L);
        playerImage.setRoundNr(1);
        playerImage.setKeywords("Envisage");
        playerImage.setVotes(4);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        PlayerImage playerImage2 = new PlayerImage();
        playerImage2.setLobbyId(1234L);
        playerImage2.setRoundNr(1);
        playerImage2.setKeywords("Envisage2");
        playerImage2.setVotes(3);
        playerImageRepository.save(playerImage2);
        playerImageRepository.flush();

        List<PlayerImage> images = playerImageService.getImagesFromRound(1234L, 1);

        assertEquals(images.size(), 2);

    }


    @Test
    public void getWinningImage(){
        PlayerImage playerImage = new PlayerImage();
        playerImage.setLobbyId(1234L);
        playerImage.setRoundNr(1);
        playerImage.setKeywords("Envisage");
        playerImage.setVotes(4);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        PlayerImage winner = playerImageService.getWinningImage(1234L, 1);

        assertEquals(winner, playerImage);
    }
}
