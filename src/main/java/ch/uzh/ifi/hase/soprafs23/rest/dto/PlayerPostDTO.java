package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class PlayerPostDTO {

    private String userName;

    // true == lobbyCreator
    // false == not LobbyCreator

    private boolean isLobbyCreator;


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

    }

