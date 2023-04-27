package ch.uzh.ifi.hase.soprafs23.exceptions;

public class PlayerImageDoesNotExistException extends RuntimeException{
    public PlayerImageDoesNotExistException(long imageId){
        super(String.format("No image with imageId %s  exists", imageId));
    }
}
