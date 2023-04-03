package ch.uzh.ifi.hase.soprafs23.rest.dto;


import java.util.List;

public class RoundDTO {

    private int roundNumber;

    private Long gameId;

    private List<PlayerImageDTO> playerImages;

    // getters and setters
    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<PlayerImageDTO> getPlayerImages() {
        return playerImages;
    }

    public void setPlayerImages(List<PlayerImageDTO> playerImages) {
        this.playerImages = playerImages;
    }
}

