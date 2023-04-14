package ch.uzh.ifi.hase.soprafs23.exceptions;

public class RoundDoesNotExistException extends RuntimeException {

    public RoundDoesNotExistException(long id){
        super(String.format("Round with id %s does not exist", id));
    }
}
