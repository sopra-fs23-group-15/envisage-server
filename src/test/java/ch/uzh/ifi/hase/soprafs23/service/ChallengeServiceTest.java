package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Challenge;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;


    @Test
    void createChallengeTest(){
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        Round round = new Round();
        round.setRoundNumber(1);
        Challenge createdChallenge = challengeService.createChallengeForRound(12345678L, 1, "random");
        assertEquals(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS, createdChallenge.getDurationInSeconds());
        assertNotNull(createdChallenge.getStyleRequirement());
        assertNotNull(createdChallenge.getImagePrompt());
    }

}