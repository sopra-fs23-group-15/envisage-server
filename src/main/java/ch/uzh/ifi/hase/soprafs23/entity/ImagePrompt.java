package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.ImageType;

import javax.persistence.*;

@Entity
public class ImagePrompt {
    @Id
    @GeneratedValue
    private Long id;

    private ImageType imageType;

    private String image;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageType (ImageType imageType){
        this.imageType = imageType;
    }

    public ImageType getImageType(){
        return imageType;
    }

    public void setImage(String image){
        this.image=image;
    }

    public String getImage(){
        return image;
    }

}
