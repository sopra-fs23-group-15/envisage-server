package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LobbyController.class)
class LobbyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    LobbyService lobbyService;

    @MockBean
    PlayerService playerService;

    @MockBean
    GameService gameService;

    @MockBean
    RoundService roundService;

    @MockBean
    DalleAPIService dalleAPIService;

    @MockBean
    PlayerScoreService playerScoreService;

    @MockBean
    MetMuseumAPIService metMuseumAPIService;

    @MockBean
    PlayerImageService playerImageService;

    @Test
    public void createLobby_success() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        lobby.setNumberOfRounds(EnvisageConstants.DEFAULT_NO_OF_ROUNDS);
        lobby.setRoundDuration(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setNoOfRounds(5);
        lobbyPostDTO.setRoundDurationInSeconds(50);

        // given(lobbyService.createLobby()).willReturn(lobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lobbyPostDTO));

        System.out.println("*********");
        System.out.println(mockMvc.perform(postRequest).andReturn().getResponse().getContentAsString());
        /* mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.pin", is(lobby.getPin().intValue())))
                .andExpect(jsonPath("$.numberOfRounds", is(lobbyPostDTO.getNoOfRounds())))
                .andExpect(jsonPath("$.roundDuration", is(lobbyPostDTO.getRoundDurationInSeconds())))
                .andExpect(jsonPath("$.players", is(lobby.getPlayers())))
                .andExpect(jsonPath("$.game", is(lobby.getGame()))); */
    }

    @Test
    public void createPlayer_success() throws Exception {
        Player player = new Player();
        player.setUserName("Rupert");
        player.setLobbyCreator(true);
        player.setId(1L);
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        player.setLobby(lobby);

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setUserName("Rupert");

        given(playerService.addPlayer(Mockito.any(), Mockito.anyLong())).willReturn(player);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/12345678")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(player.getId().intValue())))
                .andExpect(jsonPath("$.userName", is(player.getUserName())))
                .andExpect((jsonPath("$.lobbyCreator", is(player.isLobbyCreator()))))
                .andExpect(jsonPath("$.lobbyId", is(player.getLobby().getPin().intValue())));
    }

    @Test
    public void createPlayer_failure_noLobby() throws Exception {
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setUserName("Rupert");
        long lobbyPin = 12345678L;

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(String.format("Lobby with pin %s does not exist", lobbyPin))))
                .given(playerService).addPlayer(Mockito.any(), Mockito.anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobbies/12345678")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void createPlayer_failure_nameConflict() throws Exception {
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        String userName = "Rupert";
        playerPostDTO.setUserName(userName);

        willThrow(new ResponseStatusException(HttpStatus.CONFLICT, String.format("Username %s is not unique", userName)))
                .given(playerService).addPlayer(Mockito.any(), Mockito.anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobbies/12345678")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }


    private String asJsonString(final Object object) {
        try {
            String json =objectMapper.writeValueAsString(object);
            return json;
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

}