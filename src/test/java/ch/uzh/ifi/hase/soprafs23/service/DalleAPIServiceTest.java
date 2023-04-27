package ch.uzh.ifi.hase.soprafs23.service;

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
    public void getImageFromMetMuseum(){
        assertNotNull(dalleAPIService.getImageFromDALLE("Envisage"));
    }

}