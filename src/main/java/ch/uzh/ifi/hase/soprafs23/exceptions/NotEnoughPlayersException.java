package ch.uzh.ifi.hase.soprafs23.exceptions;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;

public class NotEnoughPlayersException extends RuntimeException {

    public NotEnoughPlayersException(){
        super(String.format("Game cannot be (re)started, you need at least %s players", EnvisageConstants.MIN_PLAYERS));
    }
}
