package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by chin- on 1/17/2017.
 */

public class RequestData {
    private String date,event,location,maxprice,numberofperson,service,
    specialrequest,status,time,uid,name,image,kC,af;
    private String currenttime;
    private String color;
    public RequestData(){

    }

    public RequestData(String date,String event,String location,String maxprice,String numberofperson
            , String service,String specialrequest, String image, String status,String time,String uid, String name,String color,String currenttime,String kC,String af ){

        this.date=date;
        this.event=event;
        this.location=location;
        this.image=image;
        this.maxprice = maxprice;
        this.numberofperson = numberofperson;
        this.service = service;
        this.specialrequest = specialrequest;
        this.status=status;
        this.time=time;
        this.uid = uid;
        this.name = name;
        this.color = color;
        this.currenttime=currenttime;
        this.kC=kC;
        this.af=af;
    }
    public String getAfterofferstatus(){
        return this.af;
    }
    public void setAfterofferstatus(String af){
        this.af=af;
    }
    public String getKeyrequest(){
        return this.kC;
    }
    public void setKeyrequest(String kC){
        this.kC=kC;
    }
    public String getCurrenttime(){return this.currenttime;}
    public void setCurrenttime(String currenttime){this.currenttime=currenttime;}
    public String getDate(){
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage(){
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEvent(){
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaxprice() {
        return this.maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public String getNumberofperson() {
        return this.numberofperson;
    }

    public void setNumberofperson(String numberofperson) {
        this.numberofperson = numberofperson;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }
    public String getSpecialrequest() {
        return this.specialrequest;
    }

    public void setSpecialrequest(String specialrequest) {
        this.specialrequest = specialrequest;
    }
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {

       this.status=status+"";

    }
    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getColor() {

        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
