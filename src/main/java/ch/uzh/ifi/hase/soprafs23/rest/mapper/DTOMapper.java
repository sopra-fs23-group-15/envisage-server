package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "pin", target = "pin")
  @Mapping(source = "numberOfRounds", target = "numberOfRounds")
  @Mapping(source = "roundDuration", target = "roundDuration")
  @Mapping(source = "players", target = "players")
  @Mapping(source = "game", target = "game")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby createdLobby);

  @Mapping(source = "roundDurationInSeconds", target = "roundDuration")
  @Mapping(source = "noOfRounds", target = "numberOfRounds")
  Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);


  @Mapping(source = "userName", target = "userName")
  Player convertPlayerPostDTOtoEntity(PlayerPostDTO playerPostDTO);


  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "lobbyCreator", target = "lobbyCreator")
  @Mapping(source = "id", target = "id")
  @Mapping(source = "lobby.pin", target = "lobbyId")
  PlayerGetDTO convertEntityToPlayerGetDTO(Player createdPlayer);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "player.userName", target = "player")
  @Mapping(source = "votes", target = "votes")
  PlayerImageDTO convertEntityToPlayerImageDTO(PlayerImage playerImage);

  @Mapping(source = "roundNumber", target = "roundNumber")
  @Mapping(source = "playerImages", target = "playerImages")
  RoundDTO convertEntityToRoundDTO(Round round);

  @Mapping(source = "player.userName", target = "player")
  @Mapping(source = "score", target = "score")
  PlayerScoreDTO convertEntityToPlayerScoreDTO(PlayerScore playerScore);

  @Mapping(source = "rounds", target = "rounds")
  @Mapping(source = "playerScores", target = "playerScores")
  @Mapping(source = "lobby.pin", target = "lobbyPin")
  @Mapping(source = "status", target = "status")
  GameDTO convertEntityToGameDTO(Game game);

  @Mapping(source="player", target="player.userName")
  @Mapping(source="score", target="score")
  PlayerScore convertPlayerScoreDTOtoEntity(PlayerScoreDTO playerScoreDTO);

  @Mapping(source="keywords", target = "keywords")
  @Mapping(source="environment", target="environment")
  Keywords convertKeywordsDTOtoEntity(KeywordsDTO keywordsDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "player.userName", target = "player")
  @Mapping(source = "image", target = "image")
  @Mapping(source = "keywords", target = "keywords")
  @Mapping(source = "round.roundNumber", target = "round")
  PlayerImageGetDTO convertEntityToPlayerImageGetDTO(PlayerImage playerImage);
}
