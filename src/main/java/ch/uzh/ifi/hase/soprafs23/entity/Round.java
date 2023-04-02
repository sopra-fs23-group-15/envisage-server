package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;

@Entity
public class Round {

    @Id
    @GeneratedValue
    private Long id;
    private int roundNumber;

    @ManyToOne
    @JoinColumn(name="game_id")
    private Game game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // getters and setters
    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
