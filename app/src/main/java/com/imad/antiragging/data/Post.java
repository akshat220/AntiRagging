package com.imad.antiragging.data;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("name")
    private String name;
    @SerializedName("userid")
    private String userid;
    @SerializedName("image")
    private String image;
    @SerializedName("post")
    private String post;
    @SerializedName("date")
    private Timestamp date;

    public Post(){}

    public Post(String name, Timestamp date, String post, String image, String userid) {
        this.name = name;
        this.userid = userid;
        this.image = image;
        this.post = post;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
