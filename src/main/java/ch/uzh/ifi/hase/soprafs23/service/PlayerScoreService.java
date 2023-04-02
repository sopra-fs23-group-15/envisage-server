package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerScore;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerScoreRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PlayerScoreService {
    private final GameRepository gameRepository;
    private final PlayerScoreRepository playerScoreRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerScoreService(@Qualifier("playerScoreRepository") GameRepository gameRepository, PlayerScoreRepository playerScoreRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerScoreRepository = playerScoreRepository;
        this.playerRepository = playerRepository;
    }

    public PlayerScore setScore(PlayerScoreDTO playerScoreDTO, Long lobbyId, Long roundId){
        Game game = gameRepository.findByLobbyPin(lobbyId);
        PlayerScore playerScore = new PlayerScore();
        playerScore.setGame(game);
        PlayerGetDTO player = playerScoreDTO.getPlayer();
        playerScore.setScore(playerScore.getScore()+ playerScoreDTO.getScore());
        playerScore.setPlayer(playerRepository.findPlayerByUserNameAndAndLobby_Pin(player.getUserName(), lobbyId));
        return playerScore;
    }

}
