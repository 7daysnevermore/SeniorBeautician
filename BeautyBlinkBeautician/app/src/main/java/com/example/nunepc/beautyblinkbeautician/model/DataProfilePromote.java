package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by NunePC on 19/1/2560.
 */

public class DataProfilePromote {

    public String address,name,picture1,picture2,picture3,profile,rating,uid;
    public Long S01,S02,S03,S04;

    public DataProfilePromote(){

    }


    public void setS01(Long S01){
        this.S01 = S01;
    }

    public Long getS01(){
        return S01;
    }
    public void setS02(Long S02){
        this.S02 = S02;
    }

    public Long getS02(){
        return S02;
    }

    public void setS03(Long S03) {
        this.S03 = S03;
    }

    public Long getS03() {
        return S03;
    }

    public void setS04(Long S04) {
        this.S04 = S04;
    }

    public Long getS04() {
        return S04;
    }

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture1() {
        return picture1;
    }
    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

}
