package ch.uzh.ifi.hase.soprafs23.exceptions;

public class PlayerImageDuplicateException extends RuntimeException{

    public PlayerImageDuplicateException(String userName){
        super(String.format("An image for %s already exists in the current round.",  userName));
    }
}
