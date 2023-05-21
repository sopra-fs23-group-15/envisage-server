package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GameRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    void findByPin() {
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        entityManager.persist(lobby);
        entityManager.flush();

        Game game = new Game();
        game.setLobby(lobby);
        lobby.setGame(game);
        entityManager.persist(game);
        entityManager.flush();

        // when
        Game foundGame = gameRepository.findByLobbyPin(lobby.getPin());

        // then
        assertNotNull(foundGame);
        assertNotNull(lobby.getGame());
        assertEquals(lobby.getPin(), foundGame.getLobby().getPin());
    }


    @Test
    void findByPin_LobbyDoesNotExist() {
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        entityManager.persist(lobby);
        entityManager.flush();

        Game game = new Game();
        game.setLobby(lobby);
        lobby.setGame(game);
        entityManager.persist(game);
        entityManager.flush();

        Game foundGame = gameRepository.findByLobbyPin(1L);

        assertNull(foundGame);
    }

}
