package ch.uzh.ifi.hase.soprafs23.exceptions;

public class ImagesDontExistException extends RuntimeException{
    public ImagesDontExistException(long lobbyPin, int roundNr){
        super(String.format("No images for Lobby with pin %s and roundNr %s exist", lobbyPin, roundNr));
    }
}
