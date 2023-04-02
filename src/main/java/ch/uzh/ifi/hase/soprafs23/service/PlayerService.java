package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Player Service
 * This class is the "worker" and responsible for all functionality related to
 * the player
 */

@Service
@Transactional
public class PlayerService {
    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository, LobbyRepository lobbyRepository) {
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    /**
     * method to create a new player
     * @param newPlayer
     * @param lobbyPin
     * @return
     */
    public Player addPlayer(Player newPlayer, long lobbyPin) {
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);

        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        Player foundPlayer = playerRepository.findPlayerByUserNameAndAndLobby_Pin(newPlayer.getUserName(), lobbyPin);
        if(foundPlayer!=null){
            throw new DuplicateUserException(foundPlayer.getUserName());
        }

        newPlayer.setLobbyCreator(lobbyByPin.getPlayers().isEmpty());
        newPlayer.setLobby(lobbyByPin);
        lobbyByPin.addPlayer(newPlayer);
        // TODO: set game pin of the game the player is joining
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        Player savedPlayer = playerRepository.save(newPlayer);
        playerRepository.flush();
        return savedPlayer;
    }
}
