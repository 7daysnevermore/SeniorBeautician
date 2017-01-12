package com.example.nunepc.beautyblinkbeautician;

/**
 * Created by chin- on 1/12/2017.
 */

public class DataPromotion {
    private String Topic;
    private String Desc;
    private String Image;

    public DataPromotion(){

    }
    public DataPromotion(String detail,String image,String topic){
        this.Desc=detail;
        this.Image=image;
        this.Topic=topic;

    }
    public String getTopic(){
        return this.Topic;
    }

    public void setTopic(String topic) {
        this.Topic = topic;
    }

    public String getDesc(){
        return this.Desc;
    }

    public void setDesc(String detail) {
        this.Desc = detail;
    }

    public String getImage(){
        return this.Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}
