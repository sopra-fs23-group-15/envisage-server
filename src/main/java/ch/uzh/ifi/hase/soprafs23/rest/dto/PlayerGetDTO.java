package ch.uzh.ifi.hase.soprafs23.rest.dto;


public class PlayerGetDTO {
    private Long id;

    private String userName;

    private boolean lobbyCreator;

    // pin is lobbyId
    private long lobbyId;


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

    public void setLobbyCreator(boolean lobbyCreator) {
        this.lobbyCreator = lobbyCreator;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }
}
