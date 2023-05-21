package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.constant.ImageType;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ChallengeService {

    private final MetMuseumAPIService metMuseumAPIService;

    private final PlayerImageService playerImageService;

    private Random rand = new SecureRandom();

    @Autowired
    public ChallengeService(MetMuseumAPIService metMuseumAPIService, PlayerImageService playerImageService){
        this.metMuseumAPIService = metMuseumAPIService;
        this.playerImageService = playerImageService;
    }


    public Challenge createChallengeForRound(long lobbyPin, int roundNumber, String category){
        Challenge newChallenge = new Challenge();
        newChallenge.setDurationInSeconds(EnvisageConstants.DEFAULT_ROUND_DURATION_IN_SECONDS);
        ImagePrompt imagePrompt = getPromptImage(roundNumber, lobbyPin, category);
        newChallenge.setImagePrompt(imagePrompt);
        newChallenge.setCategory(category);
        newChallenge.setRoundNr(roundNumber);
        newChallenge.setStyleRequirement(getStyleRequirement());

        return newChallenge;
    }


    private ImagePrompt getPromptImage(int roundNumber, long lobbyPin, String category){
        ImagePrompt imagePrompt = new ImagePrompt();
        if(roundNumber==1){
            String imageUrl = metMuseumAPIService.getImageFromMetMuseum(category);
            imagePrompt.setImage(imageUrl);
            imagePrompt.setImageType(ImageType.URL);
        }
        else{
            PlayerImage winningImage = playerImageService.getWinningImage(lobbyPin, (roundNumber-1));
            imagePrompt.setImage(winningImage.getImage());
            imagePrompt.setImageType(ImageType.URL);
        }
        return imagePrompt;
    }

    private StyleRequirement getStyleRequirement(){
        StyleRequirement styleRequirement = new StyleRequirement();
        List<String> imagePrompt = readPromptFile();
        String prompt = imagePrompt.get(this.rand.nextInt(imagePrompt.size()));
        styleRequirement.setStyle(prompt);
        return styleRequirement;
    }


    private List<String> readPromptFile() {
        String fileName ="src/main/resources/imagePromptIdeas";
        List<String> result = new ArrayList<>();
        try {
            // remove all empty lines in metMuseumObjectID file, else it won't work
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String curLine;
            while ((curLine = bufferedReader.readLine()) != null){
                result.add(curLine);
            }
            bufferedReader.close();
            return result;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
