package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by NunePC on 14/1/2560.
 */

public class BeauticianService {

    public Long price;

    public BeauticianService() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public BeauticianService(Long price) {
        this.price = price;
    }
}