package ch.uzh.ifi.hase.soprafs23.rest.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class PlayerGetDTO {
    private Long id;

    private String userName;

    // true == lobbyCreator
    // false == not LobbyCreator

    private boolean isLobbyCreator;

    // pin is lobbyId
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
