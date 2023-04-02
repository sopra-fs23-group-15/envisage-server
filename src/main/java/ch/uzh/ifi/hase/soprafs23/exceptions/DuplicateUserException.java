package ch.uzh.ifi.hase.soprafs23.exceptions;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String userName){
        super(String.format("Username %s is not unique", userName));
    }

}
