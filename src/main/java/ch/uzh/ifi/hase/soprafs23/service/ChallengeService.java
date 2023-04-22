package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.ImageType;
import ch.uzh.ifi.hase.soprafs23.entity.Challenge;
import ch.uzh.ifi.hase.soprafs23.entity.ImagePrompt;
import ch.uzh.ifi.hase.soprafs23.entity.StyleRequirement;
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


    public Challenge createChallengeForRound(long lobbyPin, int roundNumber){
        Challenge newChallenge = new Challenge();
        newChallenge.setDurationInSeconds(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);
        ImagePrompt imagePrompt = getPromptImage(roundNumber, lobbyPin);
        newChallenge.setImagePrompt(imagePrompt);
        newChallenge.setStyleRequirement(getStyleRequirement());

        return newChallenge;
    }


    private ImagePrompt getPromptImage(int roundNumber, long lobbyPin){
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

    private StyleRequirement getStyleRequirement(){
        // TODO: actually implement this to get StyleRequirement
        StyleRequirement styleRequirement = new StyleRequirement();
        styleRequirement.setStyle("Salvador Dali");
        return styleRequirement;
    }

}
