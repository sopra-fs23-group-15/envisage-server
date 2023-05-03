package ch.uzh.ifi.hase.soprafs23.exceptions;

public class KeywordsLimitException extends RuntimeException{

    public KeywordsLimitException(int keywordsLength){
        super(String.format("The entered keywords exceed the character limit of 400 by %s.", keywordsLength-400));
    }
}
