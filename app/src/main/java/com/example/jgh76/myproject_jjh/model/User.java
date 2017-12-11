package com.example.jgh76.myproject_jjh.model;

import java.io.Serializable;

/**
 * Created by jgh76 on 2017-12-05.
 */

public class User implements Serializable{
    String uid;
    String phone;
    String name;
    String location;
    String profileUrl;

    public User(){

    }

    public User(String uid, String phone, String location, String name, String profileUrl) {
        this.uid = uid;
        this.phone = phone;
        this.location = location;
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
