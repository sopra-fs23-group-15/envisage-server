package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.ImageType;
import ch.uzh.ifi.hase.soprafs23.entity.ImagePrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ChallengeService {

    private final MetMuseumAPIService metMuseumAPIService;

    @Autowired
    public ChallengeService(MetMuseumAPIService metMuseumAPIService) {
        this.metMuseumAPIService = metMuseumAPIService;
    }


    public ImagePrompt getPromptImage(int roundNumber){
        ImagePrompt imagePrompt = new ImagePrompt();
        if(roundNumber==1){
            String imageUrl = metMuseumAPIService.getImageFromMetMuseum();
            imagePrompt.setImage(imageUrl);
            imagePrompt.setImageType(ImageType.URL);
        }
        else{
            // get winning image from database
        }
        return imagePrompt;
    }

}
