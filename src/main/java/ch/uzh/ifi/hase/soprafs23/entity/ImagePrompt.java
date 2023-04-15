package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.ImageType;

public class ImagePrompt {
    private ImageType imageType;

    private String image;

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
