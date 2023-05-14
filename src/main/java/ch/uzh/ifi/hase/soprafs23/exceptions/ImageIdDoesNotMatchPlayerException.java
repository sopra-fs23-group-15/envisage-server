package ch.uzh.ifi.hase.soprafs23.exceptions;

public class ImageIdDoesNotMatchPlayerException extends RuntimeException{
    public ImageIdDoesNotMatchPlayerException(long imageId, String userName){
        super(String.format("Given image id %s does not match given player %s", imageId, userName));
    }
}
