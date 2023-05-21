package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.exceptions.DuplicateUserException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;
import java.util.List;


@Service
@Transactional
public class LobbyService {

    private final LobbyRepository lobbyRepository;

    private Random rand = new SecureRandom();

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }


    public Lobby createLobby(int numberOfRounds, int roundDuration) {
        Lobby newLobby = new Lobby();
        newLobby.setPin(createPin());
        newLobby.setNumberOfRounds(numberOfRounds);
        newLobby.setRoundDuration(roundDuration);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        return newLobby;
    }


    public Lobby createLobby() {
        return createLobby(EnvisageConstants.DEFAULT_NO_OF_ROUNDS, EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);
    }


    private long createPin(){
        long tryPin = 100000 + this.rand.nextLong(900000);
        while(checkIfPinExists(tryPin)){
            tryPin = 100000 + this.rand.nextLong(900000);
        }
        return tryPin;
    }


    public boolean checkIfPinExists(long pin){
        Lobby lobbyByPin = lobbyRepository.findByPin(pin);
        return lobbyByPin != null;
    }

    public Lobby findLobby(long lobbyPin){
        return lobbyRepository.findByPin(lobbyPin);
    }


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

        lobbyByPin.addPlayer(newPlayer);
        Lobby newLobby = lobbyRepository.save(lobbyByPin);
        lobbyRepository.flush();
        return newLobby;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }
}
