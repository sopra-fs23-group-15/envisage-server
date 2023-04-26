package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;

@Entity
public class Challenge {
    @Id
    @GeneratedValue
    private Long id;

    private int durationInSeconds;

    @OneToOne
    private Round round;

    @OneToOne
    private StyleRequirement styleRequirement;

    @OneToOne
    private ImagePrompt imagePrompt;

    // getters and setters
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

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
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

    public void start(){}
}
