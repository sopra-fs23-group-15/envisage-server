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
    void getImageFromMetMuseum_random(){
        assertNotNull(metMuseumAPIService.getImageFromMetMuseum("random"));
    }


    @Test
    void getImageFromMetMuseum_abstract_art(){
        assertNotNull(metMuseumAPIService.getImageFromMetMuseum("abstract art"));
    }

    @Test
    void getImageFromMetMuseum_still_life(){
        assertNotNull(metMuseumAPIService.getImageFromMetMuseum("still_life"));
    }

    @Test
    void getImageFromMetMuseum_postcard(){
        assertNotNull(metMuseumAPIService.getImageFromMetMuseum("postcard"));
    }

    @Test
    void getImageFromMetMuseum_portrait(){
        assertNotNull(metMuseumAPIService.getImageFromMetMuseum("portrait"));
    }

    @Test
    void getImageFromMetMuseum_landscape(){
        assertNotNull(metMuseumAPIService.getImageFromMetMuseum("landscape"));
    }

}