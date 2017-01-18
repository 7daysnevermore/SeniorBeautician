package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by NunePC on 12/12/2559.
 */

public class User {

    public String profile,email, firstname, lastname, phone, address_number,
            address_sub_district, address_district, address_province, address_code, birthday, gender;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String profile,String email,String firstname,String lastname,String phone,String address_number,
                String address_sub_district,String address_district,String address_province,String address_code,
                String birthday,String gender) {
        this.profile = profile;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.address_number = address_number;
        this.address_sub_district = address_sub_district;
        this.address_district = address_district;
        this.address_province = address_province;
        this.address_code = address_code;
        this.birthday = birthday;
        this.gender = gender;
    }
}
