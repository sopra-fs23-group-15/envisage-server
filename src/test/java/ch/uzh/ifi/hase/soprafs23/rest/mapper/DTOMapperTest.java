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

        assertEquals(lobby.getPin(), lobbyGetDTO.getPin());
        assertEquals(lobby.getPlayers(), lobbyGetDTO.getPlayers());
        assertEquals(lobby.getGame(), lobbyGetDTO.getGame());
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

        assertEquals(roundDTO.getRoundNumber(), round.getRoundNumber());
        assertEquals(roundDTO.getPlayerImages(), round.getPlayerImages());

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

        assertEquals(gameDTO.getRounds(), game.getRounds());
        assertEquals(gameDTO.getPlayerScores(), game.getPlayerScores());
        assertEquals(gameDTO.getLobbyPin(), game.getLobby().getPin());
        assertEquals(gameDTO.getStatus(), game.getStatus());

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



}