package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class KeywordsDTO {

    private String keywords;

    private String environment;

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeywords(){
        return keywords;
    }

    public void setEnvironment(String environment) {this.environment = environment;}
    public String getEnvironment() {return environment;}
}
