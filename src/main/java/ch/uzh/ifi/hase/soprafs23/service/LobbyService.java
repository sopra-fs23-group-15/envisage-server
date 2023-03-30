package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Random;

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
        Lobby newLobby = new Lobby();
        newLobby.setPin(createPin());

        // saves the given entity but data is only persisted in the database once
        // flush() is called
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
    private boolean checkIfPinExists(long pin){
        Lobby lobbyByPin = lobbyRepository.findByPin(pin);
        if (lobbyByPin == null)
        {
            return false;
        }
        return true;
    }

    public void checkIfLobbyIdExists(long lobbyId){
        Lobby lobbyByLobbyID = lobbyRepository.findByPin(lobbyId);

        String baseErrorMessage = "No Lobby with LobbyId %d was found";
        if (lobbyByLobbyID == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(baseErrorMessage, lobbyId));
        }
    }

}
