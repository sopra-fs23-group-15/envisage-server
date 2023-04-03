package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.sql.Blob;

@Entity
public class PlayerImage {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Player player;

    @Lob
    private Blob image;

    @ManyToOne()
    @JoinColumn(name="round_id")
    private Round round;

    private int votes;

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

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
