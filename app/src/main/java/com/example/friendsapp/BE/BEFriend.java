package com.example.friendsapp.BE;


import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Date;

public class BEFriend implements Serializable {
    String name, address, email, phoneNumber, website, imgPath;
    Date birthdate;
    long id = -1;
    Double homeLat = 0.0, homeLng = 0.0;

    public BEFriend(String name, String address, String email, String phoneNumber, String website, Date birthdate, LatLng home, String imgPath) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.birthdate = birthdate;
        this.homeLat = home.latitude;
        this.homeLng = home.longitude;
        this.imgPath = imgPath;
    }

    public BEFriend(String name, String address, String email, String phoneNumber, String website, Date birthdate, LatLng home) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.birthdate = birthdate;
        this.homeLat = home.latitude;
        this.homeLng = home.longitude;
    }

    public BEFriend() {
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
        return new LatLng(homeLat, homeLng);
    }

    public void setHome(LatLng home) {
        this.homeLat = home.latitude;
        this.homeLng = home.longitude;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
