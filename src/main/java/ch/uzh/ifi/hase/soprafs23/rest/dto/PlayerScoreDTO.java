package ch.uzh.ifi.hase.soprafs23.rest.dto;


public class PlayerScoreDTO {

    private String player;

    private int score;

    // getters and setters
    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
