package ch.uzh.ifi.hase.soprafs23.exceptions;

public class GameAlreadyExistsException extends RuntimeException{

    public GameAlreadyExistsException(long lobbyPin){
        super(String.format("A game in lobby with lobbyPin %s already exists", lobbyPin));
    }
}
