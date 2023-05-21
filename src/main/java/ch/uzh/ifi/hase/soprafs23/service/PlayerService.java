package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.exceptions.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository, LobbyRepository lobbyRepository) {
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }


    public Player addPlayer(Player newPlayer, long lobbyPin) {
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);

        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        if(lobbyByPin.getPlayers().size() == EnvisageConstants.MAX_PLAYERS){
            throw new MaxPlayersReachedException();
        }

        Game currentGame = lobbyByPin.getGame();
        if(currentGame!=null && currentGame.getStatus().equals(GameStatus.IN_PROGRESS)){
            throw new GameInProgressException();
        }

        Player foundPlayer = playerRepository.findPlayerByUserNameAndAndLobby_Pin(newPlayer.getUserName(), lobbyPin);
        if(foundPlayer!=null){
            throw new DuplicateUserException(foundPlayer.getUserName());
        }

        newPlayer.setLobbyCreator(lobbyByPin.getPlayers().isEmpty());
        newPlayer.setLobby(lobbyByPin);

        lobbyByPin.addPlayer(newPlayer);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        Player savedPlayer = playerRepository.save(newPlayer);
        playerRepository.flush();
        return savedPlayer;
    }

    public void removePlayerFromLobby(long lobbyPin, String username){
        Lobby lobbyFound = lobbyRepository.findByPin(lobbyPin);
        if (lobbyFound == null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        Player playerFound = playerRepository.findPlayerByUserNameAndAndLobby_Pin(username, lobbyPin);
        if (playerFound == null){
            throw new PlayerDoesNotExistException(username);
        }

        lobbyFound.removePlayer(playerFound);
        lobbyRepository.save(lobbyFound);
        lobbyRepository.flush();

        playerFound.setLobby(null);
        playerRepository.save(playerFound);
        playerRepository.flush();

    }


}
