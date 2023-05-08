package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class LobbyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    void findByPin_success() {

       Lobby lobby = new Lobby();
       List<Player> playerList = new ArrayList<>();
       lobby.setPin(12345678L);
       lobby.setRoundDuration(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);
       lobby.setNumberOfRounds(EnvisageConstants.DEFAULT_NO_OF_ROUNDS);
       lobby.setPlayers(playerList);

        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Lobby found = lobbyRepository.findByPin(lobby.getPin());

        // then
        assertNotNull(found.getPin());
        assertEquals(found.getPin(), lobby.getPin());
        assertEquals(found.getPlayers(), lobby.getPlayers());
        assertEquals(found.getRoundDuration(), lobby.getRoundDuration());
        assertEquals(found.getNumberOfRounds(), lobby.getNumberOfRounds());
        assertEquals(found.getGame(), lobby.getGame());
    }


    @Test
    void findByPin_failure() {

        Lobby lobby = new Lobby();
        List<Player> playerList = new ArrayList<>();
        lobby.setPin(12345678L);
        lobby.setRoundDuration(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);
        lobby.setNumberOfRounds(EnvisageConstants.DEFAULT_NO_OF_ROUNDS);
        lobby.setPlayers(playerList);

        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Lobby found = lobbyRepository.findByPin(123L);

        // then
        assertNull(found);
    }

}