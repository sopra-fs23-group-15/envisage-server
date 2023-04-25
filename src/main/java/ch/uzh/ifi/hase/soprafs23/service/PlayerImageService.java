package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.LobbyDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.PlayerDoesNotExist;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoundDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerImageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerImageService {

    private final PlayerImageRepository playerImageRepository;

    private final PlayerRepository playerRepository;

    private final DalleAPIService dalleAPIService;

    private final GameRepository gameRepository;

    private final RoundRepository roundRepository;
    private final Logger log = LoggerFactory.getLogger(LobbyService.class);




    @Autowired
    public PlayerImageService(@Qualifier ("playerImageRepository")PlayerImageRepository playerImageRepository, PlayerRepository playerRepository, DalleAPIService dalleAPIService, GameRepository gameRepository, RoundRepository roundRepository) {
        this.playerImageRepository = playerImageRepository;
        this.playerRepository = playerRepository;
        this.dalleAPIService = dalleAPIService;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
    }


    public void createImage(Keywords keywords, long lobbyId, int roundId, String username) {
        Player playerFound = playerRepository.findPlayerByUserNameAndAndLobby_Pin(username, lobbyId);
        if (playerFound == null){
            throw new PlayerDoesNotExist(username);
        }
        Game gameFound = gameRepository.findByLobbyPin(lobbyId);
        if (gameFound == null) {
            throw new GameDoesNotExistException(lobbyId);
        }
        Round roundFound = roundRepository.findByRoundNumberAndGame_Id(roundId, gameFound.getId());

        if (roundFound == null){
            throw new RoundDoesNotExistException(roundId);
        }

        JSONObject base64encodedStringImage = dalleAPIService.getImageFromDALLE(keywords.getKeywords());
        String generatedImage = base64encodedStringImage.toString();
        log.info(generatedImage);
        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(playerFound);
        playerImage.setImage(generatedImage);
        playerImage.setRound(roundFound);

        playerImageRepository.save(playerImage);
        playerImageRepository.flush();
    }
}
