package ch.uzh.ifi.hase.soprafs23.rest.dto;


public class RoundDTO {

    private int roundNumber;

    private GameDTO game;

    // getters and setters
    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }
}

