package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ROUND")
public class Round implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private int roundNumber;

    @ManyToOne
    @JoinColumn(name="game_id")
    private Game game;

//    @ManyToOne
//    @JoinColumn(name="lobby_id")
//    private Lobby lobby;

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

//    public Lobby getLobby() {
//        return lobby;
//    }
//
//    public void setLobby(Lobby lobby) {
//        this.lobby = lobby;
//    }
}
