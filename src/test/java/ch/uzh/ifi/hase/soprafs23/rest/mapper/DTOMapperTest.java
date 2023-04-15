package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
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
    public void testCreateLobby_fromEntity_toLobbyGetDTO_success() {
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
    public void testPlayerInput_fromPlayerPostDTO_toEntity_success() {
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setUserName("Gertrude");

        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        assertEquals(player.getUserName(), playerPostDTO.getUserName());
    }

    @Test
    public void testPlayer_fromEntity_toPlayerGetDTO_success() {
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


}