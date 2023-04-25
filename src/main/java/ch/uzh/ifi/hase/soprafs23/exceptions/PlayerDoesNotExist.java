package ch.uzh.ifi.hase.soprafs23.exceptions;

public class PlayerDoesNotExist extends RuntimeException {

    public PlayerDoesNotExist(String userName){
        super(String.format("Player with username %s does not exist", userName));
    }
}

