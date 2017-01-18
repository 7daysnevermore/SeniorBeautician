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
}