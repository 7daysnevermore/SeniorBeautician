package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by chin- on 2/21/2017.
 */

public class DataNoti {
    private String name,service,currenttime,status,event;
    public DataNoti(){

    }
    public DataNoti(String name,String service, String currenttime,String status,String event){
        this.name=name;
        this.service=service;
        this.currenttime=currenttime;
        this.status=status;
        this.event=event;

    }
    public String getName(){return this.name;}
    public void setName(String name){this.name=name;}
    public String getService(){return this.service;}
    public void setService(String service){this.service=service;}
    public String getCurrenttime(){return this.currenttime;}
    public void setCurrenttime(String currenttime){this.currenttime=currenttime;}
    public String getStatus(){return this.status;}
    public void setStatus(String status){this.status=status;}
    public String getEvent(){return this.event;}
    public void setEvent(String event){this.event=event;}

}
