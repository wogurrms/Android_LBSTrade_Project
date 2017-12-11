package com.example.jgh76.myproject_jjh.model;

/**
 * Created by jgh76 on 2017-12-10.
 */

public class Chat {
    String nickname;
    String content;
    String time;
    String profile;

    public Chat(String nickname, String content, String time, String profile) {
        this.nickname = nickname;
        this.content = content;
        this.time = time;
        this.profile = profile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
