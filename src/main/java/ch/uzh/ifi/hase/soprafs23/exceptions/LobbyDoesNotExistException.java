package ch.uzh.ifi.hase.soprafs23.exceptions;

public class LobbyDoesNotExistException extends RuntimeException {

    public LobbyDoesNotExistException(long lobbyPin){
        super(String.format("Lobby with pin %s does not exist", lobbyPin));
    }
}
