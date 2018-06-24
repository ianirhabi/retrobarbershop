package com.example.irhabi.retrobarbershop.model;


public class Usr {
    @com.google.gson.annotations.SerializedName("Id")
    private int Id;
    @com.google.gson.annotations.SerializedName("Usergup")
    private String  usergrup;
    @com.google.gson.annotations.SerializedName("User")
    private String User;
    @com.google.gson.annotations.SerializedName("Pass")
    private String Pass ;

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
