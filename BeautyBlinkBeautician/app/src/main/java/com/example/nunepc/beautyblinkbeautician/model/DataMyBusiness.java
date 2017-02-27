package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by chin- on 2/26/2017.
 */

public class DataMyBusiness {
    String service,afterofferstatus,afteroffercolor,beauticianoffer,currenttime,date,location,maxprice,namecus,
    numberofcustomer,specialcus,time,event;
    public DataMyBusiness(){

    }
    public DataMyBusiness(String service,String afterofferstatus,String afteroffercolor,String beauticianoffer,String currenttime,String date,
                          String location,String event,String maxprice,String numberofcustomer,String specialcus,String time,String namecus){
        this.service=service;
        this.afterofferstatus=afterofferstatus;
        this.afteroffercolor=afteroffercolor;
        this.beauticianoffer=beauticianoffer;
        this.currenttime=currenttime;
        this.date=date;
        this.location=location;
        this.maxprice=maxprice;
        this.numberofcustomer=numberofcustomer;
        this.specialcus=specialcus;
        this.time=time;
        this.namecus=namecus;
        this.event=event;

    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getNamecus() {
        return namecus;
    }

    public void setNamecus(String namecus) {
        this.namecus = namecus;
    }

    public String getBeauticianoffer() {
        return beauticianoffer;
    }

    public void setBeauticianoffer(String beauticianoffer) {
        this.beauticianoffer = beauticianoffer;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public String getNumberofcustomer() {
        return numberofcustomer;
    }

    public String getSpecialcus() {
        return specialcus;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public void setNumberofcustomer(String numberofcustomer) {
        this.numberofcustomer = numberofcustomer;
    }

    public void setSpecialcus(String specialcus) {
        this.specialcus = specialcus;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAfteroffercolor(){
        return this.afteroffercolor;
    }

    public void setAfteroffercolor(String afteroffercolor) {
        this.afteroffercolor = afteroffercolor;
    }

    public String getAfterofferstatus(){
        return this.afterofferstatus;
    }

    public void setAfterofferstatus(java.lang.String afterofferstatus) {
        this.afterofferstatus = afterofferstatus;
    }

    public String getService(){
        return this.service;
    }
    public void setService(String service){
        this.service=service;
    }
}
