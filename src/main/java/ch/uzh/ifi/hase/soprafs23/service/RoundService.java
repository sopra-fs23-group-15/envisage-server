package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoundDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


@Service
@Transactional
public class RoundService {
    private final RoundRepository roundRepository;
    private final GameRepository gameRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public RoundService(@Qualifier("roundRepository") RoundRepository roundRepository, GameRepository gameRepository, LobbyRepository lobbyRepository) {
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public Round getRound(int roundNumber, long gameId) {
        Round round = roundRepository.findByRoundNumberAndGame_Id(roundNumber, gameId);
        if(round == null){
            throw new RoundDoesNotExistException(roundNumber);
        } else{
            return round;
        }
    }

    public Round createRound(long lobbyPin){
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);
        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        Game game = gameRepository.findByLobbyPin(lobbyPin);
        if(game==null){
            throw new GameDoesNotExistException(lobbyPin);
        }


        int numberOfRounds = game.getRounds().size();
        if(numberOfRounds==1){
            game.setStatus(GameStatus.IN_PROGRESS);
        }

        Round newRound = new Round();
        newRound.setRoundNumber(numberOfRounds+1);
        newRound.setGame(game);
        game.addRound(newRound);
        Round savedRound = roundRepository.save(newRound);
        roundRepository.flush();
        return savedRound;
    }
}
