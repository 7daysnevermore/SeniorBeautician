package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by NunePC on 18/1/2560.
 */

public class DataGallery {

    public String image,status,name,uid;

    public DataGallery() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public DataGallery(String image,String status,String name,String uid) {

        this.image = image;
        this.status = status;
        this.name = name;
        this.uid = uid;

    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){ return this.image;}

    public void setStatus(String status){ this.status = status; }

    public String getStatus(){ return this.status; }

    public void setName(String name){ this.name = name; }

    public String getName(){ return this.name; }

    public void setUid(String uid){this.uid = uid;}

    public String getUid(){ return this.uid;}
}