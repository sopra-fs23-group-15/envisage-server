package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "pin", target = "pin")
  @Mapping(source = "players", target = "players")
  @Mapping(source = "game", target = "game")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby createdLobby);


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
//  @Mapping(source = "game.id", target = "gameId")
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

}
