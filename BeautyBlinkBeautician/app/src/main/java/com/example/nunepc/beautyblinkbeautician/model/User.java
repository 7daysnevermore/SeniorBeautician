package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by NunePC on 12/12/2559.
 */

public class User {
    public String email;
    public String name;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String name) {
        this.name = name;
        this.email = email;
    }
}
