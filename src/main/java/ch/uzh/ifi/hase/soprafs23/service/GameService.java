package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        Game game = gameRepository.findByLobbyPin(lobbyPin);
        if(game==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }else{
            return game;
        }
    }

    public Game createGame(long lobbyPin){
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);
        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        Game game = new Game();
        game.setLobby(lobbyByPin);
//        lobbyByPin.setGame(game);
        Game savedGame = gameRepository.save(game);
        gameRepository.flush();
        return savedGame;
    }
}
