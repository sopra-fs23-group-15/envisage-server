package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "game")
    private List<Round> rounds;

    @OneToMany(mappedBy = "game")
    private List<PlayerScore> playerScores;

    @OneToOne
    private Lobby lobby;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public void addRound(Round newRound){ rounds.add(newRound); }

    public List<PlayerScore> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(List<PlayerScore> playerScores) {
        this.playerScores = playerScores;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
