package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;

@Entity
public class PlayerScore {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Player player;

    @ManyToOne()
    @JoinColumn(name="game_id")
    private Game game;

    private int score;


    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
