package ch.uzh.ifi.hase.soprafs23.controller;

import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
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

import static org.hamcrest.Matchers.hasSize;
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

    @Test
    public void createLobby_success() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        lobby.setNumberOfRounds(EnvisageConstants.DEFAULT_NO_OF_ROUNDS);
        lobby.setRoundDuration(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setNoOfRounds(5);
        lobbyPostDTO.setRoundDurationInSeconds(50);

        given(lobbyService.createLobby()).willReturn(lobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.numberOfRounds", is(lobbyPostDTO.getNoOfRounds())))
                .andExpect(jsonPath("$.roundDuration", is(lobbyPostDTO.getRoundDurationInSeconds())));
    }

}