package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class LobbyServiceTest {

    @Autowired
    LobbyService lobbyService;

    @Autowired
    private LobbyRepository lobbyRepository;

    /**@Test
    public void getLobbies_fromRepository(){
        Lobby lobby1 = new Lobby();
        lobby1.setPin(1L);
        lobby1.setNumberOfRounds(1);
        lobby1.setRoundDuration(20);
        lobbyRepository.save(lobby1);
        lobbyRepository.flush();

        Lobby lobby2 = new Lobby();
        lobby2.setPin(2L);
        lobby2.setNumberOfRounds(1);
        lobby2.setRoundDuration(20);
        lobbyRepository.save(lobby2);
        lobbyRepository.flush();

        List<Lobby> allLobbiesList = lobbyService.getLobbies();

        assertEquals(allLobbiesList.size(), lobbyRepository.count());

    }**/

    @Test
    public void createLobby_configureParameters(){
        Lobby lobby = lobbyService.createLobby(2, 30);
        System.out.println("Pin: " + lobby.getPin());
        assertNotNull(lobby);
        assertEquals(2, lobby.getNumberOfRounds());
        assertEquals(30, lobby.getRoundDuration());
    }

    @Test
    public void createLobby(){
        Lobby lobby = lobbyService.createLobby();
        System.out.println("Pin: " + lobby.getPin());
        assertNotNull(lobby);
        assertEquals(5, lobby.getNumberOfRounds());
        assertEquals(60, lobby.getRoundDuration());
    }

    @Test
    public void checkIfLobbyPinExists(){
        Lobby lobby = lobbyService.createLobby();
        assertTrue(lobbyService.checkIfPinExists(lobby.getPin()));
    }

    @Test
    public void checkIfLobbyPinExists_noSuchPin() {
        assertFalse(lobbyService.checkIfPinExists(2));
    }

    @Test
    public void addPlayer(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer1");
        Lobby newLobby = lobbyService.addPlayer(player, lobby.getPin());
        assertEquals(1, newLobby.getPlayers().size());
    }

    @Test
    public void addPlayer_noSuchLobby(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer1");
        Lobby newLobby = lobbyService.addPlayer(player, lobby.getPin());
        assertThrows(LobbyDoesNotExistException.class, () -> lobbyService.addPlayer(player, 2));
    }

    @Test
    public void addPlayer_duplicateUser(){
        Lobby lobby = lobbyService.createLobby();
        Player player = new Player();
        player.setUserName("testplayer1");
        Lobby newLobby = lobbyService.addPlayer(player, lobby.getPin());

        Player duplicatePlayer = new Player();
        duplicatePlayer.setUserName("testplayer1");
        assertThrows(DuplicateUserException.class, () -> lobbyService.addPlayer(duplicatePlayer, newLobby.getPin()));
    }


}
