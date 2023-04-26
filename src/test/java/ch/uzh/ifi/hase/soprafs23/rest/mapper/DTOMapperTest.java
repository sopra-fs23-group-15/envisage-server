package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */

class DTOMapperTest {

    @Test
    public void test_fromEntity_toLobbyGetDTO_success() {
        Lobby lobby = new Lobby();
        List<Player> playerList = new ArrayList<>();

        lobby.setPin(1L);
        lobby.setPlayers(playerList);

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        List<Player> playerList1 = lobby.getPlayers();
        List<PlayerGetDTO> playerGetDTOS = new ArrayList<>();
        for (Player player:  playerList1){
            playerGetDTOS.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
        }


        assertEquals(lobby.getPin(), lobbyGetDTO.getPin());
        assertEquals(playerGetDTOS, lobbyGetDTO.getPlayers());
        assertEquals(DTOMapper.INSTANCE.convertEntityToGameDTO(lobby.getGame()), lobbyGetDTO.getGame());
    }

    @Test
    public void test_fromPlayerPostDTO_toEntity_success() {
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setUserName("Gertrude");

        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        assertEquals(player.getUserName(), playerPostDTO.getUserName());
    }

    @Test
    public void test_fromEntity_toPlayerGetDTO_success() {
        Player player = new Player();
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        player.setUserName("Albert");
        player.setLobbyCreator(true);
        player.setId(2L);
        player.setLobby(lobby);

        PlayerGetDTO playerGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);

        assertEquals(player.getUserName(), playerGetDTO.getUserName());
        assertEquals(player.isLobbyCreator(), playerGetDTO.isLobbyCreator());
        assertEquals(player.getId(), playerGetDTO.getId());
        assertEquals(player.getLobby().getPin(), playerGetDTO.getLobbyId());
    }

    @Test
    public void test_fromEntity_toPlayerImageDTO(){
        PlayerImage playerImage = new PlayerImage();
        Player player = new Player();
        player.setUserName("Gertrude");
        playerImage.setId(3L);
        playerImage.setPlayer(player);
        playerImage.setVotes(3);

        PlayerImageDTO playerImageDTO = DTOMapper.INSTANCE.convertEntityToPlayerImageDTO(playerImage);

        assertEquals(playerImageDTO.getId(), playerImage.getId());
        assertEquals(playerImageDTO.getPlayer(), playerImage.getPlayer().getUserName());
        assertEquals(playerImageDTO.getVotes(), playerImage.getVotes());
    }

    @Test
    public void test_fromEntity_toRoundDTO(){
        Round round = new Round();
        List<PlayerImage> playerImages = new ArrayList<>();
        round.setRoundNumber(5);
        round.setPlayerImages(playerImages);

        RoundDTO roundDTO = DTOMapper.INSTANCE.convertEntityToRoundDTO(round);

        List<PlayerImage> playerImages1 = round.getPlayerImages();
        List<PlayerImageDTO> playerImageDTOS = new ArrayList<>();
        for (PlayerImage image:  playerImages1){
            playerImageDTOS.add(DTOMapper.INSTANCE.convertEntityToPlayerImageDTO(image));
        }

        assertEquals(round.getRoundNumber(), roundDTO.getRoundNumber());
        assertEquals(playerImageDTOS, roundDTO.getPlayerImages() );

    }

    @Test
    public void test_fromEntity_toPlayerScoreDTO(){
        PlayerScore playerScore = new PlayerScore();
        Player player = new Player();
        player.setUserName("Albert");
        playerScore.setPlayer(player);
        playerScore.setScore(4);

        PlayerScoreDTO playerScoreDTO = DTOMapper.INSTANCE.convertEntityToPlayerScoreDTO(playerScore);

        assertEquals(playerScoreDTO.getPlayer(), playerScore.getPlayer().getUserName());
        assertEquals(playerScoreDTO.getScore(), playerScore.getScore());
    }

    @Test
    public void test_fromEntity_GameDTO(){
        Game game =  new Game();
        List<Round> roundList = new ArrayList<>();
        List<PlayerScore> playerScoreList = new ArrayList<>();
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        game.setRounds(roundList);
        game.setPlayerScores(playerScoreList);
        game.setLobby(lobby);
        game.setStatus(GameStatus.READY);

        GameDTO gameDTO = DTOMapper.INSTANCE.convertEntityToGameDTO(game);

        List<Round> roundList2 = game.getRounds();
        List<RoundDTO> roundDTOList = new ArrayList<>();
        for (Round round:  roundList2){
            roundDTOList.add(DTOMapper.INSTANCE.convertEntityToRoundDTO(round));
        }

        List<PlayerScore> playerScoresList = game.getPlayerScores();
        List<PlayerScoreDTO> playerScoreDTOS = new ArrayList<>();
        for (PlayerScore score:  playerScoresList){
            playerScoreDTOS.add(DTOMapper.INSTANCE.convertEntityToPlayerScoreDTO(score));
        }

        assertEquals(roundDTOList, gameDTO.getRounds());
        assertEquals(playerScoreDTOS, gameDTO.getPlayerScores());
        assertEquals(game.getLobby().getPin(), gameDTO.getLobbyPin());
        assertEquals(game.getStatus(), gameDTO.getStatus());

    }

    @Test
    public void test_fromPlayerScoreDTO_toEntity(){
        PlayerScoreDTO playerScoreDTO = new PlayerScoreDTO();
        playerScoreDTO.setPlayer("Gertrude");
        playerScoreDTO.setScore(23);

        PlayerScore playerScore = DTOMapper.INSTANCE.convertPlayerScoreDTOtoEntity(playerScoreDTO);

        assertEquals(playerScore.getPlayer().getUserName(), playerScoreDTO.getPlayer());
        assertEquals(playerScore.getScore(), playerScoreDTO.getScore());
    }


    @Test
    public void test_fromKeywordsDTO_toEntity(){
        KeywordsDTO keywordsDTO = new KeywordsDTO();
        keywordsDTO.setKeywords("Envisage");

        Keywords keywords = DTOMapper.INSTANCE.convertKeywordsDTOtoEntity(keywordsDTO);


        assertEquals(keywords.getKeywords(), keywordsDTO.getKeywords());
    }



}