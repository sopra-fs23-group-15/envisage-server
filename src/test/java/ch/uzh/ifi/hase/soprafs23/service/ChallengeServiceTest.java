package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.ImageType;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerImageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private PlayerImageRepository playerImageRepository;


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
        assertEquals(ImageType.URL, createdChallenge.getImagePrompt().getImageType());

    }

    @Test
    void createChallengeTest_round2(){
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        Round round = new Round();
        round.setRoundNumber(1);

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

        PlayerImage playerImage = new PlayerImage();
        playerImage.setKeywords("Envisage");
        playerImage.setVotes(4);
        playerImage.setRound(round);
        playerImage.setImage("image");
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();


        Challenge createdChallenge = challengeService.createChallengeForRound(lobby.getPin(), 2, "random");
        assertEquals(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS, createdChallenge.getDurationInSeconds());
        assertNotNull(createdChallenge.getStyleRequirement());
        assertNotNull(createdChallenge.getImagePrompt());
        assertEquals( playerImage.getImage(), createdChallenge.getImagePrompt().getImage());
        assertEquals(ImageType.URL, createdChallenge.getImagePrompt().getImageType());
        assertEquals(2, createdChallenge.getRoundNr());
    }

}