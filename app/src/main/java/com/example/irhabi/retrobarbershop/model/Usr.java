package com.example.irhabi.retrobarbershop.model;


public class Usr {
    @com.google.gson.annotations.SerializedName("id")
    private int Id;
    @com.google.gson.annotations.SerializedName("usergroup")
    private String  usergrup;
    @com.google.gson.annotations.SerializedName("user_retro")
    private String User;
    @com.google.gson.annotations.SerializedName("password_retro")
    private String Pass ;
    @com.google.gson.annotations.SerializedName("nama_foto")
    private String foto ;
    @com.google.gson.annotations.SerializedName("latitude")
    private String lati ;
    @com.google.gson.annotations.SerializedName("longtitude")
    private String longt ;


    public Usr(int id, String User, String Pass, String usrgrup){
        this.Id = id;
        this.User = User;
        this.Pass = Pass;
        this.usergrup = usrgrup;
    }
    public int Getid(){
        return Id;
    }
    public String getUser(){
        return User;
    }
    public String getPass(){
        return Pass;
    }
    public String getUsergrup(){
        return usergrup;
    }
}
