package ch.uzh.ifi.hase.soprafs23.entity;

public class Challenge {
    private int durationInSeconds;
    private StyleRequirement styleRequirement;

    private ImagePrompt imagePrompt;

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public void setStyleRequirement(StyleRequirement styleRequirement) {
        this.styleRequirement = styleRequirement;
    }

    public StyleRequirement getStyleRequirement() {
        return styleRequirement;
    }

    public void setImagePrompt(ImagePrompt imagePrompt){
        this.imagePrompt = imagePrompt;
    }

    public ImagePrompt getImagePrompt(){
        return imagePrompt;
    }

    public void start(){;}
}
