package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.NotEnoughPlayersException;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, LobbyRepository lobbyRepository) {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public Game getGame(long lobbyPin) {
        Lobby lobby = lobbyRepository.findByPin(lobbyPin);
        if(lobby==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }else{
            return lobby.getGame();
        }
    }

    public Game createGame(long lobbyPin){
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);
        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        if(lobbyByPin.getPlayers().size() < EnvisageConstants.MIN_PLAYERS){
            throw new NotEnoughPlayersException();
        }

        //create the game
        return initialiseGame(lobbyByPin);
    }

    private Game initialiseGame(Lobby lobbyByPin) {
        Game game = new Game();
        game.setLobby(lobbyByPin);

        // initialise player scores
        List<PlayerScore> playerScores = new ArrayList<>();
        for(Player player : lobbyByPin.getPlayers()){
            PlayerScore ps = new PlayerScore();
            ps.setPlayer(player);
            ps.setScore(0);
            ps.setGame(game);
            playerScores.add(ps);
        }
        game.setPlayerScores(playerScores);
        game.setStatus(GameStatus.IN_PROGRESS);

        //create the first round
        Round round = new Round();
        round.setGame(game);
        round.setRoundNumber(1);

        game.addRound(round);

        lobbyByPin.setGame(game);
        Lobby storedLobby = lobbyRepository.save(lobbyByPin);
        lobbyRepository.flush();
        return storedLobby.getGame();
    }
}
