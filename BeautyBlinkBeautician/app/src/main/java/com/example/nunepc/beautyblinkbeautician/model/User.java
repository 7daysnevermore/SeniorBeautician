package com.example.nunepc.beautyblinkbeautician.model;

/**
 * Created by NunePC on 12/12/2559.
 */

public class User {
<<<<<<< HEAD
    public String email;
    public String name;
=======

    public String email, firstname, lastname, phone, address_num,
            address_sub_district, address_district, address_province, address_code, birthday, gender;
>>>>>>> develop

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

<<<<<<< HEAD
    public User(String email, String name) {
        this.name = name;
        this.email = email;
=======
    public User(String email,String firstname,String lastname,String phone,String address_num,
                String address_sub_district,String address_district,String address_province,String address_code,
                String birthday,String gender) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.address_num = address_num;
        this.address_sub_district = address_sub_district;
        this.address_district = address_district;
        this.address_province = address_province;
        this.address_code = address_code;
        this.birthday = birthday;
        this.gender = gender;
>>>>>>> develop
    }
}
