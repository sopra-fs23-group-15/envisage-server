package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerScore;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PlayerScoreService {
    private final GameRepository gameRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public PlayerScoreService(@Qualifier("gameRepository")  GameRepository gameRepository, LobbyRepository lobbyRepository) {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public Game updatePlayerScore(long lobbyId, PlayerScore playerScore){
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyId);
        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyId);
        }

        Game game = gameRepository.findByLobbyPin(lobbyId);
        if(game==null){
            throw new GameDoesNotExistException(lobbyId);
        }
        game.setPlayerScore(playerScore);
        Game savedGame = gameRepository.save(game);
        gameRepository.flush();
        return savedGame;

    }
}
