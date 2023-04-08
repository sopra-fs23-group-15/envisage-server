package ch.uzh.ifi.hase.soprafs23.exceptions;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;

public class MaxPlayersReachedException extends RuntimeException {

    public MaxPlayersReachedException(){
        super(String.format("No more players allowed, there are already %s players", EnvisageConstants.MAX_PLAYERS));
    }
}
