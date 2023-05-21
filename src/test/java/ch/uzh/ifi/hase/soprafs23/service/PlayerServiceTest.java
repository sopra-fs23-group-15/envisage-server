package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.exceptions.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PlayerServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Autowired
    PlayerService playerService;

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Test
    void addPlayer(){
        Player player = new Player();
        player.setUserName("testplayer1");

        Lobby lobby = lobbyService.createLobby();
        Player savedPlayer = playerService.addPlayer(player, lobby.getPin());
        assertNotNull(savedPlayer);
        lobby = lobbyService.findLobby(lobby.getPin());
        Player foundPlayer = lobby.getPlayers().get(0);
        assertEquals(player.getUserName(), foundPlayer.getUserName());
        assertTrue(foundPlayer.isLobbyCreator());
    }

    @Test
    void addPlayer_lobbyDoesNotExist(){
        Player player = new Player();
        player.setUserName("testplayer1");
        assertThrows(LobbyDoesNotExistException.class, () -> {playerService.addPlayer(player, 1);});
    }

    @Test
    void addPlayer_maxPlayersReached(){
        Lobby lobby = lobbyService.createLobby();
        for(int i =0; i<EnvisageConstants.MAX_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            playerService.addPlayer(player, lobby.getPin());
        }
        Player extraPlayer = new Player();
        extraPlayer.setUserName("testPlayer"+(EnvisageConstants.MAX_PLAYERS+1));
        assertThrows(MaxPlayersReachedException.class, () -> {playerService.addPlayer(extraPlayer, lobby.getPin());});
    }

    @Test
    void addPlayer_gameInProgress(){
        // create a lobby with necessary amount of players to start a game
        Lobby lobby = lobbyService.createLobby();
        for(int i =0; i<EnvisageConstants.MIN_PLAYERS; i++){
            Player player = new Player();
            player.setUserName("testplayer"+(i+1));
            playerService.addPlayer(player, lobby.getPin());
        }

        // create a game and put its status to "in progress" to mimic a game being started
        Game game = new Game();
        game.setStatus(GameStatus.IN_PROGRESS);
        lobby.setGame(game);

        // assert that GameInProgressException is thrown when a new player tries to join game
        Player player = new Player();
        player.setUserName("testPlayer"+(EnvisageConstants.MIN_PLAYERS+1));
        assertThrows(GameInProgressException.class, () -> {playerService.addPlayer(player, lobby.getPin());});
    }

    @Test
    void addPlayer_duplicateUser(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer");
        playerService.addPlayer(player, lobby.getPin());

        Player duplicateUser = new Player();
        duplicateUser.setUserName(player.getUserName());
        assertThrows(DuplicateUserException.class, () -> {playerService.addPlayer(duplicateUser, lobby.getPin());});
    }

    @Test
    void removePlayerFromLobby_noLobby(){
        assertThrows(LobbyDoesNotExistException.class, () -> playerService.removePlayerFromLobby(77777777L, "Rupert"));
    }

    @Test
    void removePlayerFromLobby_noPlayer(){
        Lobby lobby = new Lobby();
        long lobbyPin = 12345678L;
        lobby.setPin(lobbyPin);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        assertThrows(PlayerDoesNotExistException.class, () -> playerService.removePlayerFromLobby(lobbyPin, "Rupert"));
    }

    @Test
    void removePlayerFromLobby_success(){
        Lobby lobby = lobbyService.createLobby();
        long lobbyPin = lobby.getPin();

        Player player1 = new Player();
        player1.setUserName("Gertrude");
        player1.setLobby(lobby);
        player1.setLobbyCreator(true);

        Player player2 = new Player();
        player2.setUserName("Peter");
        player2.setLobby(lobby);
        player2.setLobbyCreator(false);

        Player player3 = new Player();
        player3.setUserName("Wolfgang");
        player3.setLobby(lobby);
        player3.setLobbyCreator(false);

        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        lobby.addPlayer(player3);
        playerRepository.save(player1);
        playerRepository.save(player2);
        playerRepository.save(player3);
        playerRepository.flush();
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        assertTrue(player1.isLobbyCreator());
        assertEquals(3, lobby.getPlayers().size());

        playerService.removePlayerFromLobby(lobbyPin, player3.getUserName());
        lobby = lobbyRepository.findByPin(lobbyPin);
        assertEquals(2, lobby.getPlayers().size());
        assertNull(player3.getLobby());
        assertNull(playerRepository.findPlayerByUserNameAndAndLobby_Pin("Wolfgang", lobbyPin));

        playerService.removePlayerFromLobby(lobbyPin, player1.getUserName());
        lobby = lobbyRepository.findByPin(lobbyPin);
        assertEquals(1, lobby.getPlayers().size());
        assertTrue(player2.isLobbyCreator());
        assertNull( player1.getLobby());
        assertNull(playerRepository.findPlayerByUserNameAndAndLobby_Pin("Gertrude", lobbyPin));


        playerService.removePlayerFromLobby(lobbyPin, player2.getUserName());
        lobby = lobbyRepository.findByPin(lobbyPin);
        assertEquals(0, lobby.getPlayers().size());
        assertNull(player2.getLobby());
        assertNull(playerRepository.findPlayerByUserNameAndAndLobby_Pin("Peter", lobbyPin));


    }
}
