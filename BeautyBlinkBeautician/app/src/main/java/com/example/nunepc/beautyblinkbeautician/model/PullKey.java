package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by chin- on 2/8/2017.
 */

public class PullKey {
    private String tkey,key;
    public PullKey(){

    }
    public PullKey(String key,String tkey){
        this.tkey = tkey;
        this.key = key;
    }
    public String getTkey(){return this.tkey;}
    public void setTkey(String tkey){this.tkey=tkey;}
    public String getKey(){return this.key;}
    public void setKey(String key){this.key=key;}
}
