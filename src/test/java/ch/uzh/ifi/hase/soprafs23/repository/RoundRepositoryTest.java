package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoundRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoundRepository roundRepository;

    @Test
    void findByRoundNumberAndGame_Id(){
        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        Game game = new Game();
        game.addRound(round);
        round.setGame(game);
        entityManager.persist(game);
        entityManager.persist(round);
        entityManager.flush();

        // when
        Round foundRound = roundRepository.findByRoundNumberAndGame_Id(round.getRoundNumber(), game.getId());

        // then
        assertNotNull(foundRound);
        assertEquals(round.getRoundNumber(), foundRound.getRoundNumber());
        assertEquals(round.getGame().getId(), foundRound.getGame().getId());
    }

    @Test
    void findByRoundNumberAndGame_Id_RoundNumberDoesNotExist(){
        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        Game game = new Game();
        game.addRound(round);
        round.setGame(game);
        entityManager.persist(game);
        entityManager.persist(round);
        entityManager.flush();

        Round foundRound = roundRepository.findByRoundNumberAndGame_Id(2, game.getId());

        assertNull(foundRound);
    }

    @Test
    void findByRoundNumberAndGame_Id_GameIdDoesNotExist(){
        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        Round foundRound = roundRepository.findByRoundNumberAndGame_Id(round.getRoundNumber(), 1L);

        assertNull(foundRound);
    }
}
