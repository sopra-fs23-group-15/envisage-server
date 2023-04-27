package ch.uzh.ifi.hase.soprafs23.exceptions;

public class PlayerImageDoesNotExist extends RuntimeException{
    public PlayerImageDoesNotExist(long imageId){
        super(String.format("No image with imageId %s  exists", imageId));
    }
}
