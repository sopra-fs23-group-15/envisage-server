package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal Player Representation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */

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
    private boolean isLobbyCreator;

    // pin is lobbyId
    @Column(nullable = false)
    private long lobbyId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public boolean getIsLobbyCreator() {
        return isLobbyCreator;
    }

    public void setIsLobbyCreator(boolean isLobbyCreator) {
        this.isLobbyCreator = isLobbyCreator;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

}
