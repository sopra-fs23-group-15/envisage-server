package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
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
import java.util.UUID;

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


    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(Player newPlayer, long lobbyId) {
        checkIfUserNameUnique(newPlayer, lobbyId);
        newPlayer.setIsLobbyCreator(newPlayer.getIsLobbyCreator());
        newPlayer.setLobbyId(lobbyId);
        log.debug(lobbyId + "_______________________________");
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newPlayer = playerRepository.save(newPlayer);
        playerRepository.flush();

        log.debug("Created Information for Player: {}", newPlayer);
        return newPlayer;
    }

    private void checkIfUserNameUnique(Player player, long lobbyId){
        Player playerFound = playerRepository.findByUserNameAndAndLobbyId(player.getUserName(), lobbyId);

        String baseErrorMessage = "Username %s is not unique";
        if (playerFound != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(baseErrorMessage, player.getUserName()));
        }
    }
}
