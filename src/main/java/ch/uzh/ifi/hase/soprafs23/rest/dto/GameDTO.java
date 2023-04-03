package ch.uzh.ifi.hase.soprafs23.rest.dto;


import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;

import java.util.List;

public class GameDTO {

    private List<RoundDTO> rounds;

    private List<PlayerScoreDTO> playerScores;

    private Long lobbyPin;

    private GameStatus status;

    // getters and setters
    public List<RoundDTO> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundDTO> rounds) {
        this.rounds = rounds;
    }

    public List<PlayerScoreDTO> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(List<PlayerScoreDTO> playerScores) {
        this.playerScores = playerScores;
    }

    public Long getLobbyPin() {
        return lobbyPin;
    }

    public void setLobbyPin(Long lobbyPin) {
        this.lobbyPin = lobbyPin;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
