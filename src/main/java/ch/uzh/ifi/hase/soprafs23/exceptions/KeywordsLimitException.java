package ch.uzh.ifi.hase.soprafs23.exceptions;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;

public class KeywordsLimitException extends RuntimeException{

    public KeywordsLimitException(int keywordsLength){
        super(String.format("The entered keywords exceed the character limit of %s by %s.",
                EnvisageConstants.MAX_KEYWORDS_LENGTH, keywordsLength-EnvisageConstants.MAX_KEYWORDS_LENGTH));
    }
}
