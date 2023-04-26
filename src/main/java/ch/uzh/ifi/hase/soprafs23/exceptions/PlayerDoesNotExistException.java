package ch.uzh.ifi.hase.soprafs23.exceptions;

public class PlayerDoesNotExistException extends RuntimeException {

    public PlayerDoesNotExistException(String userName){
        super(String.format("Player with username %s does not exist", userName));
    }
}

