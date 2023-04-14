package ch.uzh.ifi.hase.soprafs23.exceptions;


public class GameInProgressException extends RuntimeException {

    public GameInProgressException(){
        super("No more players allowed, game in progress");
    }
}
