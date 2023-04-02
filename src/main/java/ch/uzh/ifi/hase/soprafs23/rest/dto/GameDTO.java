package ch.uzh.ifi.hase.soprafs23.rest.dto;


import java.util.List;

public class GameDTO {

    private List<RoundDTO> rounds;

    private List<PlayerScoreDTO> playerScores;

    private LobbyGetDTO lobby;

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

    public LobbyGetDTO getLobby() {
        return lobby;
    }

    public void setLobby(LobbyGetDTO lobby) {
        this.lobby = lobby;
    }
}
