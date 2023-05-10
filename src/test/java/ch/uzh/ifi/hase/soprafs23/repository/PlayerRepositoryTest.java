package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void findPlayerByUserNameAndAndLobby_Pin(){
        Player player = new Player();
        player.setUserName("testuser1");
        entityManager.persist(player);
        entityManager.flush();

        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        lobby.addPlayer(player);
        player.setLobby(lobby);
        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Player foundPlayer = playerRepository.findPlayerByUserNameAndAndLobby_Pin(player.getUserName(), lobby.getPin());

        // then
        assertNotNull(foundPlayer);
        assertEquals(player.getUserName(), foundPlayer.getUserName());
        assertEquals(lobby.getPin(), foundPlayer.getLobby().getPin());
    }

    @Test
    void findPlayerByUserNameAndAndLobby_Pin_PlayerDoesNotExist(){
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Player foundPlayer = playerRepository.findPlayerByUserNameAndAndLobby_Pin("testuser1", lobby.getPin());

        // then
        assertNull(foundPlayer);
    }

    @Test
    void findPlayerByUserNameAndAndLobby_Pin_LobbyDoesNotExist(){
        Player player = new Player();
        player.setUserName("testuser1");
        entityManager.persist(player);
        entityManager.flush();

        // when
        Player foundPlayer = playerRepository.findPlayerByUserNameAndAndLobby_Pin(player.getUserName(), 1L);

        // then
        assertNull(foundPlayer);
    }
}
