package ch.uzh.ifi.hase.soprafs23.rest.dto;


public class PlayerScoreDTO {

    private PlayerGetDTO player;

    private GameDTO game;

    private int score;

    // getters and setters
    public PlayerGetDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerGetDTO player) {
        this.player = player;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
