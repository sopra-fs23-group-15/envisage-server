package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Round {
    @Id
    @GeneratedValue
    private Long id;
    private int roundNumber;

    @ManyToOne
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<PlayerImage> playerImages = new ArrayList<PlayerImage>();

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

    public List<PlayerImage> getPlayerImages() {
        return playerImages;
    }

    public void setPlayerImages(List<PlayerImage> playerImages) {
        this.playerImages = playerImages;
    }
    public void setPlayerImage(PlayerImage playerImage){
        this.playerImages.stream()
                .filter(_playerImage -> _playerImage.getPlayer().getUserName().equals(playerImage.getPlayer().getUserName()))
                .forEach(
                        image -> {
                            image.setId(playerImage.getId());
                            image.setImage(playerImage.getImage());
                            image.setKeywords(playerImage.getKeywords());
                            image.setRound(playerImage.getRound());
                        }
                );
    }
}
