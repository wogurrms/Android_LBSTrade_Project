package com.example.jgh76.myproject_jjh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jgh76 on 2017-11-27.
 */

public class Product implements Serializable {
    // 제목
    String title;

    // 설명
    String desc;

    // 가격
    String price;

    // 사진이름
    String imageUrl;

    // 위치
    String location;

    // 시간 차이
    String elapsedTime;

    // 유저
    String uid;

    public Product(){

    }

    public Product(String title, String desc, String price, String imageUrl, String location, String elapsedTime, String uid) {
        this.title = title;
        this.desc  = desc;
        this.price = price;
        this.imageUrl = imageUrl;
        this.location = location;
        this.elapsedTime = elapsedTime;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
