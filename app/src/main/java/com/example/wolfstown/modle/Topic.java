package com.example.wolfstown.modle;

public class Topic {
    String name;
    Long time;
    String content;
    String picture_uri;
    String topic;
    Integer likeNum;
    Integer resumNum;

    public Topic() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPicture_uri() {
        return picture_uri;
    }

    public void setPicture_uri(String picture_uri) {
        this.picture_uri = picture_uri;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getResumNum() {
        return resumNum;
    }

    public void setResumNum(Integer resumNum) {
        this.resumNum = resumNum;
    }
}
