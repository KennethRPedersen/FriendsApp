package com.example.friendsapp.BE;


import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

public class BEFriend {
    String name, address, email, phoneNumber, website;
    Date birthdate;
    long id = -1;
    LatLng home;

    public BEFriend(String name, String address, String email, String phoneNumber, String website, Date birthdate, LatLng home) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.birthdate = birthdate;
        this.home = home;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LatLng getHome() {
        return home;
    }

    public void setHome(LatLng home) {
        this.home = home;
    }
}
