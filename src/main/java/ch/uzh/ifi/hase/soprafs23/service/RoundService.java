package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

    public Optional<Round> getRound(int id) throws Exception {
        Optional<Round> round = roundRepository.findById(id);
        if(round.isEmpty()){
            throw new Exception("round did not happem");
        } else{
            return round;
        }
    }

    public Round createRound(long lobbyPin, int roundId){
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);
        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        Round round = new Round();
        Game game = gameRepository.findByLobbyPin(lobbyPin);
        round.setGame(game);
//        round.setLobby(lobbyByPin);
        round.setRoundNumber(roundId);
//        game.addRound(round); //gives internal server error
//        System.out.println("##########################");
//        System.out.println(game.getRounds().size());
        Round savedRound = roundRepository.save(round);
        roundRepository.flush();
        System.out.println("Saved to round repository");
        return savedRound;
    }

//    public Round updateRound(long lobbyPin, long roundId){
//        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);
//        if(lobbyByPin==null){
//            throw new LobbyDoesNotExistException(lobbyPin);
//        }
//        //update only required things
//        return new Round();
//    }
//
//    public Player getRoundWinner(long lobbyPin){
//        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);
//        if(lobbyByPin==null){
//            throw new LobbyDoesNotExistException(lobbyPin);
//        }
//        //get player by max vote and return Player
//        return new Player();
//    }
}
