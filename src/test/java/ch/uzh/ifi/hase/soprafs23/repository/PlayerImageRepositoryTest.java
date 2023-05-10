package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PlayerImageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerImageRepository playerImageRepository;

    @Test
    void findAllByRound(){
        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        for(int i = 0; i<2; i++){
            Player player = new Player();
            player.setUserName("testuser"+(i+1));
            entityManager.persist(player);
            entityManager.flush();

            PlayerImage playerImage = new PlayerImage();
            playerImage.setPlayer(player);
            playerImage.setRound(round);
            playerImage.setKeywords(String.valueOf(i+1));
            entityManager.persist(playerImage);
            entityManager.flush();
        }

        // when
        List<PlayerImage> playerImages = playerImageRepository.findAllByRound(round);

        // then
        assertEquals(2, playerImages.size());
        assertEquals("1", playerImages.get(0).getKeywords());
        assertEquals("2", playerImages.get(1).getKeywords());
    }
    @Test
    void findAllByRound_NoImagesInRound(){
        for(int i = 0; i<2; i++){
            Player player = new Player();
            player.setUserName("testuser"+(i+1));
            entityManager.persist(player);
            entityManager.flush();
        }

        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        // when
        List<PlayerImage> playerImages = playerImageRepository.findAllByRound(round);

        // then
        assertEquals(0, playerImages.size());
    }
    @Test
    void findByPlayerAndRound(){
        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        Player player = new Player();
        player.setUserName("testuser1");
        entityManager.persist(player);
        entityManager.flush();

        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(player);
        playerImage.setRound(round);
        playerImage.setKeywords("1");
        entityManager.persist(playerImage);
        entityManager.flush();

        // when
        PlayerImage foundImage = playerImageRepository.findByPlayerAndRound(player, round);

        // then
        assertNotNull(foundImage);
        assertEquals("1", foundImage.getKeywords());
    }
    @Test
    void findByPlayerAndRound_PlayerHasNoImages(){
        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        Player player = new Player();
        player.setUserName("testuser1");
        entityManager.persist(player);
        entityManager.flush();

        // when
        PlayerImage foundImage = playerImageRepository.findByPlayerAndRound(player, round);

        // then
        assertNull(foundImage);
    }
    @Test
    void findByPlayerAndRound_NoImagesInRound(){
        Round round = new Round();
        round.setRoundNumber(1);
        entityManager.persist(round);
        entityManager.flush();

        Player player = new Player();
        player.setUserName("testuser1");
        entityManager.persist(player);
        entityManager.flush();

        // image created but not assigned to round
        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(player);
        playerImage.setKeywords("1");
        entityManager.persist(playerImage);
        entityManager.flush();

        // when
        PlayerImage foundImage = playerImageRepository.findByPlayerAndRound(player, round);

        // then
        assertNull(foundImage);
    }
    @Test
    void findById(){
        Player player = new Player();
        player.setUserName("testuser1");
        entityManager.persist(player);
        entityManager.flush();

        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(player);
        playerImage.setKeywords("1");
        entityManager.persist(playerImage);
        entityManager.flush();

        // when
        Optional<PlayerImage> foundImage = playerImageRepository.findById(playerImage.getId());

        // then
        assertNotNull(foundImage);
        assertEquals(player.getUserName(), foundImage.get().getPlayer().getUserName());
        assertEquals("1", foundImage.get().getKeywords());
    }
    @Test
    void findById_IdDoesNotExist() {
        Player player = new Player();
        player.setUserName("testuser1");
        entityManager.persist(player);
        entityManager.flush();

        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(player);
        playerImage.setKeywords("1");
        entityManager.persist(playerImage);
        entityManager.flush();

        // when
        PlayerImage foundImage = playerImageRepository.findById(1L);

        // then
        assertNull(foundImage);
    }

}
