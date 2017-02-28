package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by chin- on 1/17/2017.
 */

public class RequestData {
    public String date,event,service,specialrequest,status,time,location,reqpic,username,userprofile,customerid,currenttime;
    public int numberofperson,maxprice;

    public RequestData(){

    }

    public RequestData(String date, String event,String service,String specialrequest,String status,String currenttime,
                       String time, Integer numberofperson,Integer maxprice,String location,String reqpic,String username,String userprofile,String customerid){

        this.date=date;
        this.event=event;
        this.service=service;
        this.specialrequest=specialrequest;
        this.status=status;
        this.time=time;
        this.numberofperson=numberofperson;
        this.maxprice=maxprice;
        this.location=location;
        this.reqpic = reqpic;
        this.username = username;
        this.userprofile = userprofile;
        this.customerid = customerid;
        this.currenttime = currenttime;

    }

    public void setCurrenttime(String currenttime){
        this.currenttime = currenttime;
    }

    public String getCurrenttime() {
        return this.currenttime;
    }

}
