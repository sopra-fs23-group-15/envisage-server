package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String userName;

    // true == lobbyCreator
    // false == not LobbyCreator
    @Column(nullable = false)
    private boolean lobbyCreator;

    @ManyToOne
    @JoinColumn(name="lobby_id")
    private Lobby lobby;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerImage> playerImages = new ArrayList<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLobbyCreator() {
        return lobbyCreator;
    }

    public void setLobbyCreator (boolean isLobbyCreator) {
        this.lobbyCreator = isLobbyCreator;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public List<PlayerImage> getPlayerImages() {
        return playerImages;
    }

    public void setPlayerImages(List<PlayerImage> playerImages) {
        this.playerImages = playerImages;
    }

    public void addPlayerImage(PlayerImage playerImage){
        playerImages.add(playerImage);
    }


}
