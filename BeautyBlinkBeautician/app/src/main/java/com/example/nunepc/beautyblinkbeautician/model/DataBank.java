package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by NunePC on 11/4/2560.
 */

public class DataBank {

    public String bank,number;

    public DataBank() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public DataBank(String bank,String number) {

        this.bank = bank;
        this.number = number;

    }
}
