package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
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

}