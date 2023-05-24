package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Image;
import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.*;
import ch.uzh.ifi.hase.soprafs23.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
class PlayerImageServiceTest {

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

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    RoundService roundService;


    @Test
    void createImage_playerDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testUser"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }

        gameService.createGame(lobby.getPin());
        Keywords keywords = new Keywords();
        keywords.setKeywords("test");
        assertThrows(PlayerDoesNotExistException.class, () -> playerImageService.createImage(keywords, lobby.getPin(), 1,"testUser50"));
    }

    @Test
    void createImage_blankImage(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testUser"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }
        gameService.createGame(lobby.getPin());
        Round round = roundService.createRound(lobby.getPin());

        Keywords keywords = new Keywords();
        keywords.setKeywords("");
        keywords.setEnvironment("development");
        PlayerImage playerImage = playerImageService.createImage(keywords, lobby.getPin(), round.getRoundNumber(), "testUser1");
        assertEquals(Image.WHITE, playerImage.getImage());

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

        gameService.createGame(lobby.getPin());
        Keywords keywords = new Keywords();
        keywords.setKeywords("test");
        assertThrows(RoundDoesNotExistException.class, () -> playerImageService.createImage(keywords, lobby.getPin(), 2, "testUser1"));
    }

    @Test
    void createImage_playerImageDuplicate(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testUser"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }
        gameService.createGame(lobby.getPin());
        Round round = roundService.createRound(lobby.getPin());

        Keywords keywords = new Keywords();
        keywords.setKeywords("image1");
        keywords.setEnvironment("development");
        playerImageService.createImage(keywords, lobby.getPin(), round.getRoundNumber(), "testUser1");

        assertThrows(PlayerImageDuplicateException.class, () -> playerImageService.createImage(keywords, lobby.getPin(), round.getRoundNumber(), "testUser1"));

    }

    @Test
    void createImage_dall_E_default(){
        Lobby lobby = lobbyService.createLobby();
        for(int i = 0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testUser"+(i+1));
            lobbyService.addPlayer(player, lobby.getPin());
        }
        gameService.createGame(lobby.getPin());
        Round round = roundService.createRound(lobby.getPin());

        Keywords keywords = new Keywords();
        keywords.setKeywords("image1");
        keywords.setEnvironment("production");
        PlayerImage playerImage = playerImageService.createImage(keywords, lobby.getPin(), round.getRoundNumber(), "testUser1");

        assertEquals(Image.DALL_E, playerImage.getImage());
        assertEquals("image1", playerImage.getKeywords());

    }

    @Test
    void updatesVotesImages_noSuchPlayerImage(){
        Player player = new Player();
        assertThrows(PlayerImageDoesNotExistException.class, () -> playerImageService.updatesVotesImages(1L, player));
    }

    @Test
    void updatesVotesImages_IdAndPlayerDontMatch(){
        Player player = new Player();
        player.setUserName("Hans");
        Player player2 = new Player();
        player2.setUserName("Albert");
        playerRepository.save(player2);
        playerRepository.flush();
        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(player2);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();
        assertThrows(ImageIdDoesNotMatchPlayerException.class, () -> playerImageService.updatesVotesImages(playerImage.getId(), player));
    }

    @Test
    void updatesVotesImages_Success(){
        PlayerImage playerImage = new PlayerImage();
        playerImage.setVotes(0);
        Player player = new Player();
        player.setUserName("Bernd");
        playerRepository.save(player);
        playerRepository.flush();
        playerImage.setPlayer(player);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();
        playerImageService.updatesVotesImages(playerImage.getId(), player);

        assertEquals(1, playerImage.getVotes());

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
    void getImagesFromRound_ExceptionGame(){
        Lobby lobby = new Lobby();
        lobby.setPin(12345L);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        assertThrows(GameDoesNotExistException.class, () -> playerImageService.getImagesFromRound(12345L, 3));
    }

    @Test
    void getImagesFromRound_ExceptionRound(){
        Game game = new Game();
        Lobby lobby = new Lobby();
        lobby.setPin(12345L);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
        game.setLobby(lobby);
        gameRepository.save(game);
        gameRepository.flush();

        assertThrows(RoundDoesNotExistException.class, () -> playerImageService.getImagesFromRound(12345L, 3));
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

        assertEquals(2, images.size());
        assertEquals(playerImage, images.get(0));
        assertEquals(playerImage2, images.get(1));
    }


    @Test
    void getWinningImage(){
        PlayerImage playerImage = new PlayerImage();
        PlayerImage playerImage2 = new PlayerImage();
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
        playerImage2.setRound(round);
        playerImage2.setKeywords("Envisage2");
        playerImage2.setVotes(3);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        PlayerImage winner = playerImageService.getWinningImage(1234L, 1);

        assertEquals(winner, playerImage);
        assertNotEquals(winner, playerImage2);
    }

    @Test
    void getWinningImage_2(){
        PlayerImage playerImage = new PlayerImage();
        PlayerImage playerImage2 = new PlayerImage();
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
        playerImage.setVotes(3);
        playerImage2.setRound(round);
        playerImage2.setKeywords("Envisage2");
        playerImage2.setVotes(4);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        PlayerImage winner = playerImageService.getWinningImage(1234L, 1);

        assertEquals(winner, playerImage);
        assertNotEquals(winner, playerImage2);
    }

    @Test
    void getImagesOfPlayer(){
        // create lobby and add players
        Lobby lobby = lobbyService.createLobby();
        Player player1 = new Player();
        player1.setUserName("testuser1");
        playerRepository.save(player1);
        playerRepository.flush();
        lobby.addPlayer(player1);
        Player player2 = new Player();
        player2.setUserName("testuser2");
        playerRepository.save(player2);
        playerRepository.flush();
        lobby.addPlayer(player2);
        Round round = new Round();
        Game game = new Game();
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
        game.setLobby(lobby);
        gameRepository.save(game);
        gameRepository.flush();
        round.setRoundNumber(1);
        round.setGame(game);
        roundRepository.save(round);
        roundRepository.flush();

        // create images (player1)
        for(int i = 0; i<EnvisageConstants.DEFAULT_NO_OF_ROUNDS; i++){
            PlayerImage playerImage = new PlayerImage();
            playerImage.setRound(round);
            playerImage.setPlayer(player1);
            playerImageRepository.save(playerImage);
            playerImageRepository.flush();
        }

        // create images (player2)
        for(int i = 0; i<EnvisageConstants.DEFAULT_NO_OF_ROUNDS; i++) {
            PlayerImage playerImage2 = new PlayerImage();
            playerImage2.setRound(round);
            playerImageRepository.save(playerImage2);
            playerImageRepository.flush();
        }

        // assert that returned images are by correct player
        for(PlayerImage playerImage : playerImageService.getImagesOfPlayer(lobby.getPin(), player1.getUserName())){
            assertEquals(player1.getUserName(), playerImage.getPlayer().getUserName());
        }

        for(PlayerImage playerImage : playerImageService.getImagesOfPlayer(lobby.getPin(), player2.getUserName())){
            assertEquals(player2.getUserName(), playerImage.getPlayer().getUserName());
        }
    }

    @Test
    void getImagesOfPlayer_playerDoesNotExist(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testuser1");
        lobby.addPlayer(player);
        assertThrows(PlayerDoesNotExistException.class, () -> playerImageService.getImagesOfPlayer(lobby.getPin(), "testuser2"));
    }

    @Test
    void getAllWinningImages(){
        PlayerImage playerImage = new PlayerImage();
        PlayerImage playerImage2 = new PlayerImage();
        PlayerImage playerImage3 = new PlayerImage();
        Round round = new Round();
        Round round2 = new Round();
        Game game = new Game();
        Lobby lobby = new Lobby();
        lobby.setPin(1234L);
        lobby.setNumberOfRounds(2);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
        game.setLobby(lobby);
        gameRepository.save(game);
        gameRepository.flush();
        round.setRoundNumber(1);
        round.setGame(game);
        round2.setRoundNumber(2);
        round2.setGame(game);
        game.addRound(round);
        game.addRound(round2);
        roundRepository.save(round);
        roundRepository.save(round2);
        roundRepository.flush();
        playerImage.setRound(round);
        playerImage.setVotes(4);
        playerImage2.setRound(round2);
        playerImage2.setVotes(5);
        playerImage3.setRound(round2);
        playerImage3.setVotes(2);
        playerImageRepository.save(playerImage);
        playerImageRepository.save(playerImage2);
        playerImageRepository.flush();

        List<PlayerImage> playerImageList = playerImageService.getAllWinningImages(lobby.getPin());

        assertEquals(2, playerImageList.size());
        assertEquals(playerImage, playerImageList.get(0));
        assertEquals(playerImage2, playerImageList.get(1));
        assertFalse(playerImageList.contains(playerImage3));
    }

    @Test
    void getAllWinningImages_noLobby(){
        assertThrows(LobbyDoesNotExistException.class, () -> playerImageService.getAllWinningImages(12345L));
    }


}
