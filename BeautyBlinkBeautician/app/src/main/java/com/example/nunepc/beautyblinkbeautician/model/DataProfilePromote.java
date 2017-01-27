package com.example.nunepc.beautyblinkbeautician.model;

import com.google.firebase.database.Exclude;


/**
 * Created by NunePC on 19/1/2560.
 */

public class DataProfilePromote {

    public String address,name,picture1,picture2,picture3,profile,rating,uid;
    public Integer S01,S02,S03,S04;

    @Exclude
    public String ignoreThisField;

    public DataProfilePromote(){

    }

    public DataProfilePromote(Integer S01,Integer S02,Integer S03,Integer S04,String address,String name,String picture1,String picture2,String picture3, String profile, String rating, String uid){

        this.S01 = S01;
        this.S02 = S02;
        this.S03 = S03;
        this.S04 = S04;
        this.address = address;
        this.name = name;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
        this.profile = profile;
        this.rating = rating;
        this.uid = uid;
    }



}
