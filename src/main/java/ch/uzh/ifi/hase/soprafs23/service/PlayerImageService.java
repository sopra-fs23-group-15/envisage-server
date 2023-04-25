package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameDoesNotExistException;
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

    private final MetMuseumAPIService metMuseumAPIService;

    private final GameRepository gameRepository;

    private final RoundRepository roundRepository;
    private final Logger log = LoggerFactory.getLogger(LobbyService.class);




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
        // TODO: FIX SAVING ISSUE
//        JSONObject jsonObject = dalleAPIService.getImageFromDALLE(keywords.getKeywords());
        JSONObject jsonObject = new
                JSONObject("{\"request\":{\"response_format\":\"url\",\"size\":\"256x256\",\"prompt\":\"A little penguin running\",\"n\":1},\"data\":[{\"url\":\"https://oaidalleapiprodscus.blob.core.windows.net/private/org-xaJlsnHavjapPBZMPHHLxpxR/user-aOslYzMERkXRnqoCd0dhhe4t/img-bV1Ce6L924cdXpsM03Y1ad6C.png?st=2023-04-25T18%3A11%3A18Z&se=2023-04-25T20%3A11%3A18Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-04-25T19%3A05%3A52Z&ske=2023-04-26T19%3A05%3A52Z&sks=b&skv=2021-08-06&sig=q45jlubQV0CDCaQ0mUJSfQVex3Pvb8331kYvbv/4EZQ%3D\"}],\"created\":1682449878,\"DUMMY_VAR\":\"this is a dummy environment variable used to check if the java code can actually access this and display the correct value or not\"}");
        String generatedImage = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");


//        String generatedImage = metMuseumAPIService.getImageFromMetMuseum();

        System.out.println(generatedImage);
        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(playerFound);
        playerImage.setKeywords(keywords.getKeywords());
        playerImage.setImage(generatedImage);
        playerImage.setRound(roundFound);

        playerImageRepository.save(playerImage);
        playerImageRepository.flush();
        return generatedImage;
    }
}
