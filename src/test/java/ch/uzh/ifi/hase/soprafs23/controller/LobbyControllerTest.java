package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.*;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
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
        // create lobby with default configuration
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);

        // create lobbyPostDTO to be used for POST request with
        // a different configuration of round duration and #rounds
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setNoOfRounds(4);
        lobbyPostDTO.setRoundDurationInSeconds(50);
        lobby.setRoundDuration(lobbyPostDTO.getRoundDurationInSeconds());


        // assert that a lobby is returned when calling createLobby
        // with configuration parameters
        given(lobbyService.createLobby(Mockito.anyInt(), Mockito.anyInt())).willReturn(lobby);

        // perform the POST request /lobbies to create a lobby with the lobbyPostDTO above
        MockHttpServletRequestBuilder postRequest = post("/lobbies").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // assert values and status code are correct
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.pin", is(lobby.getPin().intValue())))
                .andExpect(jsonPath("$.numberOfRounds", is(lobby.getNumberOfRounds())))
                .andExpect(jsonPath("$.roundDuration", is(lobby.getRoundDuration())))
                .andExpect(jsonPath("$.players", is(lobby.getPlayers())))
                .andExpect(jsonPath("$.game", is(lobby.getGame()))); 
    }

    @Test
    public void getAllLobbies_success () throws Exception {
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setNoOfRounds(4);
        lobbyPostDTO.setRoundDurationInSeconds(50);
        lobby.setRoundDuration(lobbyPostDTO.getRoundDurationInSeconds());
        lobby.setNumberOfRounds(lobbyPostDTO.getNoOfRounds());

        List<Lobby> allLobbies = Collections.singletonList(lobby);

        given(lobbyService.getLobbies()).willReturn(allLobbies);


        MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].pin", is(lobby.getPin().intValue())))
                .andExpect(jsonPath("$[0].numberOfRounds", is(lobby.getNumberOfRounds())))
                .andExpect(jsonPath("$[0].roundDuration", is(lobby.getRoundDuration())))
                .andExpect(jsonPath("$[0].players", is(lobby.getPlayers())))
                .andExpect(jsonPath("$[0].game", is(lobby.getGame())));
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

        given(playerService.addPlayer(Mockito.any(), anyLong())).willReturn(player);

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
                .given(playerService).addPlayer(Mockito.any(), anyLong());

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
                .given(playerService).addPlayer(Mockito.any(), anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobbies/12345678")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void getLobby_success () throws Exception {
        Lobby lobby = new Lobby();
        lobby.setPin(12345678L);
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setNoOfRounds(4);
        lobbyPostDTO.setRoundDurationInSeconds(50);
        lobby.setRoundDuration(lobbyPostDTO.getRoundDurationInSeconds());
        lobby.setNumberOfRounds(lobbyPostDTO.getNoOfRounds());

        given(lobbyService.findLobby(anyLong())).willReturn(lobby);


        MockHttpServletRequestBuilder getRequest = get("/lobbies/12345678").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.pin", is(lobby.getPin().intValue())))
                .andExpect(jsonPath("$.numberOfRounds", is(lobby.getNumberOfRounds())))
                .andExpect(jsonPath("$.roundDuration", is(lobby.getRoundDuration())))
                .andExpect(jsonPath("$.players", is(lobby.getPlayers())))
                .andExpect(jsonPath("$.game", is(lobby.getGame())));
    }

    @Test
    public void getLobby_failure_noLobbyExist () throws Exception {
        Lobby lobby = new Lobby();
        Long lobbyPin = 12345678L;
        lobby.setPin(lobbyPin);
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setNoOfRounds(4);
        lobbyPostDTO.setRoundDurationInSeconds(50);
        lobby.setRoundDuration(lobbyPostDTO.getRoundDurationInSeconds());
        lobby.setNumberOfRounds(lobbyPostDTO.getNoOfRounds());

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with pin %s does not exist", lobbyPin)))
                .given(playerService).addPlayer(Mockito.any(), anyLong());

        MockHttpServletRequestBuilder getRequest = get("/lobbies/87654321").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test
    public void startGame_success () throws Exception {
        Game createdGame = new Game();
        Lobby lobby = new Lobby();
        List<PlayerScore> playerScoreList = new ArrayList<>();
        List<Round> roundList = new ArrayList<>();
        lobby.setPin(12345678L);
        createdGame.setId(1L);
        createdGame.setStatus(GameStatus.IN_PROGRESS);
        createdGame.setLobby(lobby);
        createdGame.setPlayerScores(playerScoreList);
        createdGame.setRounds(roundList);

        given(gameService.createGame(anyLong())).willReturn(createdGame);


        MockHttpServletRequestBuilder postRequest = post("/lobbies/12345678/games").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.rounds", is(createdGame.getRounds())))
                .andExpect(jsonPath("$.playerScores", is(createdGame.getPlayerScores())))
                .andExpect(jsonPath("$.lobbyPin", is(createdGame.getLobby().getPin().intValue())))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    public void startGame_failure_noLobby () throws Exception {
        Game createdGame = new Game();
        Lobby lobby = new Lobby();
        List<PlayerScore> playerScoreList = new ArrayList<>();
        List<Round> roundList = new ArrayList<>();
        Long lobbyPin = 12345678L;
        lobby.setPin(lobbyPin);
        createdGame.setId(1L);
        createdGame.setStatus(GameStatus.IN_PROGRESS);
        createdGame.setLobby(lobby);
        createdGame.setPlayerScores(playerScoreList);
        createdGame.setRounds(roundList);

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with pin %s does not exist", lobbyPin)))
                .given(gameService).createGame(anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobbies/87654321/games").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest).andExpect(status().isNotFound());
    }

    @Test
    public void startGame_failure_notEnoughPlayers () throws Exception {
        Game createdGame = new Game();
        Lobby lobby = new Lobby();
        List<PlayerScore> playerScoreList = new ArrayList<>();
        List<Round> roundList = new ArrayList<>();
        Long lobbyPin = 12345678L;
        lobby.setPin(lobbyPin);
        createdGame.setId(1L);
        createdGame.setStatus(GameStatus.IN_PROGRESS);
        createdGame.setLobby(lobby);
        createdGame.setPlayerScores(playerScoreList);
        createdGame.setRounds(roundList);

        willThrow(new ResponseStatusException(HttpStatus.CONFLICT, String.format("Game cannot be started yet, you need at least %s players", EnvisageConstants.MIN_PLAYERS)))
                .given(gameService).createGame(anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobbies/12345678/games").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest).andExpect(status().isConflict());
    }

    @Test
    public void getGame_success () throws Exception {
        Game createdGame = new Game();
        Lobby lobby = new Lobby();
        List<PlayerScore> playerScoreList = new ArrayList<>();
        List<Round> roundList = new ArrayList<>();
        lobby.setPin(12345678L);
        createdGame.setId(1L);
        createdGame.setStatus(GameStatus.IN_PROGRESS);
        createdGame.setLobby(lobby);
        createdGame.setPlayerScores(playerScoreList);
        createdGame.setRounds(roundList);

        given(gameService.getGame(anyLong())).willReturn(createdGame);


        MockHttpServletRequestBuilder getRequest = get("/lobbies/12345678/games").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.rounds", is(createdGame.getRounds())))
                .andExpect(jsonPath("$.playerScores", is(createdGame.getPlayerScores())))
                .andExpect(jsonPath("$.lobbyPin", is(createdGame.getLobby().getPin().intValue())))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    public void getGame_failure_noLobby () throws Exception {
        Game createdGame = new Game();
        Lobby lobby = new Lobby();
        List<PlayerScore> playerScoreList = new ArrayList<>();
        List<Round> roundList = new ArrayList<>();
        Long lobbyPin = 12345678L;
        lobby.setPin(lobbyPin);
        createdGame.setId(1L);
        createdGame.setStatus(GameStatus.IN_PROGRESS);
        createdGame.setLobby(lobby);
        createdGame.setPlayerScores(playerScoreList);
        createdGame.setRounds(roundList);

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with pin %s does not exist", lobbyPin)))
                .given(gameService).getGame(anyLong());

        MockHttpServletRequestBuilder getRequest = get("/lobbies/87654321/games").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test
    public void startRound_success() throws Exception{
        Round round = new Round();
        Game game = new Game();
        round.setId(1L);
        round.setRoundNumber(1);
        round.setGame(game);

        given(roundService.createRound(anyLong())).willReturn(round);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/12345678/games/rounds").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.roundNumber", is(round.getRoundNumber())))
                .andExpect(jsonPath("$.playerImages", is(round.getPlayerImages())));
    }

    @Test
    public void startRound_failure_noLobby() throws Exception{
        Round round = new Round();
        Game game = new Game();
        round.setId(1L);
        round.setRoundNumber(1);
        round.setGame(game);
        Long lobbyPin = 12345678L;

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with pin %s does not exist", lobbyPin)))
                .given(roundService).createRound(anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobbies/87654321/games/rounds").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(postRequest).andExpect(status().isNotFound());
    }


    @Test
    public void startRound_failure_noGame() throws Exception{
        Round round = new Round();
        Game game = new Game();
        round.setId(1L);
        round.setRoundNumber(1);
        round.setGame(game);
        Long lobbyPin = 12345678L;

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Game in lobby with lobbyPin %s does not exist", lobbyPin)))
                .given(roundService).createRound(anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobbies/87654321/games/rounds").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(postRequest).andExpect(status().isNotFound());
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