package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;

@Entity
public class Challenge {
    @Id
    @GeneratedValue
    private Long id;

    private int durationInSeconds;


    private int roundNr;

    private String category;

    @OneToOne
    private StyleRequirement styleRequirement;

    @OneToOne
    private ImagePrompt imagePrompt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public int getRoundNr() {
        return roundNr;
    }

    public void setRoundNr(int roundNr) {
        this.roundNr = roundNr;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory(){
        return category;
    }
}
