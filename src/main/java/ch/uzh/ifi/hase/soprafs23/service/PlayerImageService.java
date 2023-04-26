package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.PlayerDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoundDoesNotExistException;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerImageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class PlayerImageService {

    private final PlayerImageRepository playerImageRepository;

    private final PlayerRepository playerRepository;

    private final DalleAPIService dalleAPIService;

    private final MetMuseumAPIService metMuseumAPIService;

    private final GameRepository gameRepository;

    private final RoundRepository roundRepository;

    private Random rand = new SecureRandom();




    @Autowired
    public PlayerImageService(@Qualifier ("playerImageRepository")PlayerImageRepository playerImageRepository, PlayerRepository playerRepository, DalleAPIService dalleAPIService, MetMuseumAPIService metMuseumAPIService, GameRepository gameRepository, RoundRepository roundRepository) {
        this.playerImageRepository = playerImageRepository;
        this.playerRepository = playerRepository;
        this.dalleAPIService = dalleAPIService;
        this.metMuseumAPIService = metMuseumAPIService;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
    }


    public String createImage(Keywords keywords, long lobbyId, int roundId, String username) {
        Player playerFound = playerRepository.findPlayerByUserNameAndAndLobby_Pin(username, lobbyId);
        if (playerFound == null){
            throw new PlayerDoesNotExistException(username);
        }
        Game gameFound = gameRepository.findByLobbyPin(lobbyId);
        if (gameFound == null) {
            throw new GameDoesNotExistException(lobbyId);
        }
        Round roundFound = roundRepository.findByRoundNumberAndGame_Id(roundId, gameFound.getId());

        if (roundFound == null){
            throw new RoundDoesNotExistException(roundId);
        }

        JSONObject jsonObject = dalleAPIService.getImageFromDALLE(keywords.getKeywords());
        String generatedImage = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");


        //String generatedImage = metMuseumAPIService.getImageFromMetMuseum();

        System.out.println(generatedImage);
        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(playerFound);
        playerImage.setKeywords(keywords.getKeywords());
        playerImage.setImage(generatedImage);
        playerImage.setRound(roundFound);
        playerImage.setLobbyId(lobbyId);
        playerImage.setRoundNr(roundId);

        roundFound.setPlayerImages(getImagesFromRound(lobbyId, roundId));
        roundRepository.save(roundFound);
        roundRepository.flush();
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        return generatedImage;
    }

    public List<PlayerImage> getImagesFromRound(long lobbyId, int roundNr){
      List<PlayerImage>  playerImageList = playerImageRepository.findAllByLobbyIdAndRoundNr(lobbyId, roundNr);
      return playerImageList;
    }

    public PlayerImage getWinningImage(long lobbyId, int roundNr){
        List<PlayerImage>  playerImageList = playerImageRepository.findAllByLobbyIdAndRoundNr(lobbyId, roundNr);
        PlayerImage maxImage = playerImageList.get(0);
        for (PlayerImage playerImage: playerImageList){
            if (playerImage.getVotes() > maxImage.getVotes())
                {maxImage = playerImage;
                }
            else if (playerImage.getVotes() == maxImage.getVotes()){
                List<PlayerImage> randomImage = new ArrayList<>();
                maxImage = randomImage.get(this.rand.nextInt(randomImage.size()));
            }
            else{
                maxImage = maxImage;
            }
        }
        return maxImage;
    }
}
