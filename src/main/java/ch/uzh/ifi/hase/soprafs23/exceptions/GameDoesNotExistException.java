package ch.uzh.ifi.hase.soprafs23.exceptions;

public class GameDoesNotExistException extends RuntimeException {

    public GameDoesNotExistException(long lobbyPin){
        super(String.format("Game in lobby with lobbyPin %s does not exist", lobbyPin));
    }
}
