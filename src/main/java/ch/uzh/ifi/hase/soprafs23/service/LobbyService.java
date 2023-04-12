package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;
import java.util.List;

/**
* Lobby Service
* This class is the "worker" and responsible for all functionality related to
* the lobby
*/
@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    private Random rand = new SecureRandom();

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    /**
     * create a Lobby entity and save it to the lobbyRepository
     */
    public Lobby createLobby() {
        log.debug("___________________________________________________________");
        Lobby newLobby = new Lobby();
        newLobby.setPin(createPin());
        newLobby.setNumberOfRounds(EnvisageConstants.DEFAULT_NO_OF_ROUNDS);
        newLobby.setRoundDuration(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        log.debug("___________________________________________"+ newLobby);
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        return newLobby;
    }

    /**
     * helper function which generates a random 8-digit number
     * if there already exists a Lobby with this Pin a new one will be generated
     */
    private long createPin(){
        long tryPin = 10000000 + this.rand.nextLong(90000000);
        while(checkIfPinExists(tryPin)){
            tryPin = 10000000 + this.rand.nextLong(90000000);
        }
        return tryPin;
    }

    /**
     * helper function if there already exists a lobby with this Pin
     * if not false is returned
     * else true is returned
     * @param pin
     */
    public boolean checkIfPinExists(long pin){
        Lobby lobbyByPin = lobbyRepository.findByPin(pin);
        return lobbyByPin != null;
    }

    public Lobby findLobby(long lobbyPin){
        return lobbyRepository.findByPin(lobbyPin);
    }

    // can be used to check if game can be started yet
    public boolean minPlayersReached(long lobbyPin){
        Lobby lobby = findLobby(lobbyPin);
        if(lobby!=null){
            return lobby.getPlayers().size() >= EnvisageConstants.MIN_PLAYERS;
        } else{
            throw new LobbyDoesNotExistException(lobbyPin);
        }
    }

    /**
     * method to create a new player
     * @param newPlayer
     * @param lobbyPin
     * @return
     */
    public Lobby addPlayer(Player newPlayer, long lobbyPin) {
        Lobby lobbyByPin = lobbyRepository.findByPin(lobbyPin);
        if(lobbyByPin==null){
            throw new LobbyDoesNotExistException(lobbyPin);
        }

        List<Player> playerList = lobbyByPin.getPlayers();
        newPlayer.setLobbyCreator(playerList.isEmpty());
        for (Player player: playerList) {
            if(player.getUserName().equalsIgnoreCase(newPlayer.getUserName())){
                throw new DuplicateUserException(newPlayer.getUserName());
            }
        }
        newPlayer.setLobby(lobbyByPin);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        lobbyByPin.addPlayer(newPlayer);
        Lobby newLobby = lobbyRepository.save(lobbyByPin);
        lobbyRepository.flush();
        return newLobby;
    }

}
