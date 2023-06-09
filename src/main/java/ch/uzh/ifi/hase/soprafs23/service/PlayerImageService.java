package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.Image;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.*;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerImageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlayerImageService {

    private final PlayerImageRepository playerImageRepository;

    private final PlayerRepository playerRepository;

    private final DalleAPIService dalleAPIService;

    private final MetMuseumAPIService metMuseumAPIService;

    private final LobbyService lobbyService;

    private final GameRepository gameRepository;

    private final RoundRepository roundRepository;



    @Autowired
    public PlayerImageService(@Qualifier ("playerImageRepository")PlayerImageRepository playerImageRepository, PlayerRepository playerRepository, DalleAPIService dalleAPIService, MetMuseumAPIService metMuseumAPIService, LobbyService lobbyService, GameRepository gameRepository, RoundRepository roundRepository) {
        this.playerImageRepository = playerImageRepository;
        this.playerRepository = playerRepository;
        this.dalleAPIService = dalleAPIService;
        this.metMuseumAPIService = metMuseumAPIService;
        this.lobbyService = lobbyService;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
    }


    public PlayerImage createImage(Keywords keywords, long lobbyId, int roundId, String username) {
        Game gameFound = gameRepository.findByLobbyPin(lobbyId);
        if (gameFound == null) {
            throw new GameDoesNotExistException(lobbyId);
        }
        Round roundFound = roundRepository.findByRoundNumberAndGame_Id(roundId, gameFound.getId());

        if (roundFound == null){
            throw new RoundDoesNotExistException(roundId);
        }

        Player playerFound = playerRepository.findPlayerByUserNameAndAndLobby_Pin(username, lobbyId);
        if (playerFound == null){
            throw new PlayerDoesNotExistException(username);
        }

        if (playerImageRepository.findByPlayerAndRound(playerFound, roundFound) != null){
            throw  new PlayerImageDuplicateException(playerFound.getUserName());
        }

        String generatedImage;
        if (keywords.getKeywords().equals("")){
            generatedImage = Image.WHITE;
        }
        else if (keywords.getEnvironment().equals("development")) {
           generatedImage = metMuseumAPIService.getImageFromMetMuseum("random");
        }
        else{
            JSONObject jsonObject = dalleAPIService.getImageFromDALLE(keywords);
            try {
                generatedImage = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
            } catch (JSONException e){
                generatedImage = Image.DALL_E;
            }
        }



        PlayerImage playerImage = new PlayerImage();
        playerImage.setPlayer(playerFound);
        playerImage.setKeywords(keywords.getKeywords());
        playerImage.setImage(generatedImage);
        playerImage.setRound(roundFound);
        playerFound.addPlayerImage(playerImage);

        playerImageRepository.save(playerImage);
        playerImageRepository.flush();

        return playerImage;
    }

    public List<PlayerImage> getImagesFromRound(long lobbyId, int roundNr){
        Game gameFound = gameRepository.findByLobbyPin(lobbyId);
        if (gameFound == null) {
            throw new GameDoesNotExistException(lobbyId);
        }
        Round roundFound = roundRepository.findByRoundNumberAndGame_Id(roundNr, gameFound.getId());

        if (roundFound == null){
            throw new RoundDoesNotExistException(roundNr);
        }
        List<PlayerImage>  playerImageList = playerImageRepository.findAllByRound(roundFound);

        if (playerImageList.isEmpty()){
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

    public void updatesVotesImages(long id, Player player){
        PlayerImage playerImage = playerImageRepository.findById(id);
        if (playerImage==null){
            throw new PlayerImageDoesNotExistException(id);
        }
        if (!playerImage.getPlayer().getUserName().equals(player.getUserName())){
            throw  new ImageIdDoesNotMatchPlayerException(id, player.getUserName());
        }
        playerImage.setVotes(playerImage.getVotes()+1);
        playerImageRepository.save(playerImage);
        playerImageRepository.flush();
    }

    public List<PlayerImage> getImagesOfPlayer(long lobbyId, String username){
        List<Player> players = lobbyService.findLobby(lobbyId).getPlayers();
        for(int i = 0; i<players.size(); i++){
            Player player = players.get(i);
            if(player.getUserName().equals(username)){
                return player.getPlayerImages();
            }
        }
        throw new PlayerDoesNotExistException(username);
    }

    public List<PlayerImage> getAllWinningImages (long lobbyId){
        List<PlayerImage> winningImageList = new ArrayList<>();
        Lobby lobby = lobbyService.findLobby(lobbyId);
        if (lobby == null){
            throw new LobbyDoesNotExistException(lobbyId);
        }
        int numberOfRounds = lobby.getNumberOfRounds();
        for (int currentRound = 1; currentRound <= numberOfRounds; currentRound++){
            PlayerImage winningImage = getWinningImage(lobbyId, currentRound);
            winningImageList.add(winningImage);
        }
        return winningImageList;
    }

}
