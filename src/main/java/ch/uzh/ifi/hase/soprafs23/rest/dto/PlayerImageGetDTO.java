package ch.uzh.ifi.hase.soprafs23.rest.dto;


public class PlayerImageGetDTO {

    private Long id;

    private String player;

    private String image;

    private String keywords;

    private int round;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}
