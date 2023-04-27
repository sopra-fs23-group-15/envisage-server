package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.*;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerImageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONObject;


import java.util.List;

@Service
@Transactional
public class PlayerImageService {

    private final PlayerImageRepository playerImageRepository;

    private final PlayerRepository playerRepository;

    private final DalleAPIService dalleAPIService;

    private final MetMuseumAPIService metMuseumAPIService;

    private final GameRepository gameRepository;

    private final RoundRepository roundRepository;



    @Autowired
    public PlayerImageService(@Qualifier ("playerImageRepository")PlayerImageRepository playerImageRepository, PlayerRepository playerRepository, DalleAPIService dalleAPIService, MetMuseumAPIService metMuseumAPIService, GameRepository gameRepository, RoundRepository roundRepository) {
        this.playerImageRepository = playerImageRepository;
        this.playerRepository = playerRepository;
        this.dalleAPIService = dalleAPIService;
        this.metMuseumAPIService = metMuseumAPIService;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
    }


    public PlayerImage createImage(Keywords keywords, long lobbyId, int roundId, String username) {
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

        //JSONObject jsonObject = dalleAPIService.getImageFromDALLE(keywords.getKeywords());
        //String generatedImage = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");


        String generatedImage = metMuseumAPIService.getImageFromMetMuseum();

        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(playerFound);
        playerImage.setKeywords(keywords.getKeywords());
        playerImage.setImage(generatedImage);
        playerImage.setRound(roundFound);
        playerImage.setLobbyId(lobbyId);
        playerImage.setRoundNr(roundId);
        List<PlayerImage> playerImageList = playerFound.getPlayerImages();
        playerImageList.add(playerImage);
        playerFound.setPlayerImages(playerImageList);

        playerRepository.save(playerFound);

        playerRepository.flush();
        roundFound.setPlayerImages(getImagesFromRound(lobbyId, roundId));
        roundRepository.save(roundFound);
        roundRepository.flush();

        return playerImage;
    }

    public List<PlayerImage> getImagesFromRound(long lobbyId, int roundNr){
      List<PlayerImage>  playerImageList = playerImageRepository.findAllByLobbyIdAndRoundNr(lobbyId, roundNr);
      if (playerImageList.size() == 0){
          throw new ImagesDontExistException(lobbyId, roundNr);
      }
      return playerImageList;
    }

    public PlayerImage getWinningImage(long lobbyId, int roundNr){
        List<PlayerImage>  playerImageList = getImagesFromRound(lobbyId, roundNr);
        PlayerImage maxImage = playerImageList.get(0);
        for (PlayerImage playerImage: playerImageList){
            if (playerImage.getVotes() >= maxImage.getVotes())
                {maxImage = playerImage;
                }
            }
        return maxImage;
    }

    public void updatesVotesImages(long id){
        PlayerImage playerImage = playerImageRepository.findById(id);
        if (playerImage==null){
            throw new PlayerImageDoesNotExistException(id);
        }
        playerImage.setVotes(playerImage.getVotes()+1);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();
    }
}
