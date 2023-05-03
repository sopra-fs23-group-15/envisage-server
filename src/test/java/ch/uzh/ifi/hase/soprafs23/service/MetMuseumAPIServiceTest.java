package ch.uzh.ifi.hase.soprafs23.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MetMuseumAPIServiceTest {

    @Autowired
    private MetMuseumAPIService metMuseumAPIService;

    @Test
    void getImageFromMetMuseum(){
        assertNotNull(metMuseumAPIService.getImageFromMetMuseum());
    }


}