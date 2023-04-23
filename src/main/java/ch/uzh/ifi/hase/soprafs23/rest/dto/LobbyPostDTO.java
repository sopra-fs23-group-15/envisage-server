package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class LobbyPostDTO {

    int roundDurationInSeconds;

    int noOfRounds;

    // getters and setters
    public int getRoundDurationInSeconds() {
        return roundDurationInSeconds;
    }

    public void setRoundDurationInSeconds(int roundDurationInSeconds) {
        this.roundDurationInSeconds = roundDurationInSeconds;
    }

    public int getNoOfRounds() {
        return noOfRounds;
    }

    public void setNoOfRounds(int noOfRounds) {
        this.noOfRounds = noOfRounds;
    }
}
