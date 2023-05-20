package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Keywords;
import ch.uzh.ifi.hase.soprafs23.exceptions.KeywordsLimitException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
class DalleAPIServiceTest {

    @Autowired
    private DalleAPIService dalleAPIService;

    @Test
    void getImageFromMetMuseum(){
        Keywords keywords = new Keywords();
        keywords.setKeywords("Envisage");
        assertNotNull(dalleAPIService.getImageFromDALLE(keywords));
    }

    @Test
    void getImageFromMetMuseum_exception_inputTooLong(){
        Keywords keywords = new Keywords();
        String prompt = "";
        for(int i = 0; i<=EnvisageConstants.MAX_KEYWORDS_LENGTH; i++){
            prompt += "a";
        }
        keywords.setKeywords(prompt);
        assertThrows(KeywordsLimitException.class, () -> dalleAPIService.getImageFromDALLE(keywords));
    }

}