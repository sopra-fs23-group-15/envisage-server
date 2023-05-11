package ch.uzh.ifi.hase.soprafs23.rest.dto;


import java.util.List;

public class RoundDTO {

    private int roundNumber;

    private List<PlayerImageDTO> playerImages;

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public List<PlayerImageDTO> getPlayerImages() {
        return playerImages;
    }

    public void setPlayerImages(List<PlayerImageDTO> playerImages) {
        this.playerImages = playerImages;
    }
}

