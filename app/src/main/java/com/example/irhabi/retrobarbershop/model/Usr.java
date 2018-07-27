package com.example.irhabi.retrobarbershop.model;

/**
 * Created By Programmer Jalan on January 2018
 */
public class Usr {
    @com.google.gson.annotations.SerializedName("Id")
    private int Id;
    @com.google.gson.annotations.SerializedName("Usergup")
    private String  usergrup;
    @com.google.gson.annotations.SerializedName("User")
    private String User;
    @com.google.gson.annotations.SerializedName("Name")
    private String name;
    @com.google.gson.annotations.SerializedName("Pass")
    private String Pass ;
    @com.google.gson.annotations.SerializedName("NamaFoto")
    private String foto ;
    @com.google.gson.annotations.SerializedName("Latitude")
    private String lati ;
    @com.google.gson.annotations.SerializedName("Longtitude")
    private String longt ;

    public Usr(int id, String User, String name, String Pass, String usrgrup
    ,String latit, String longt){
        this.Id = id;
        this.User = User;
        this.Pass = Pass;
        this.usergrup = usrgrup;
        this.lati = latit;
        this.longt = longt;
        this.name= name;
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
    public String getname(){
        return name;
    }
    public String getLat(){
        return lati;
    }
    public String getLongt(){
        return longt;
    }
    public String getnamefoto(){
        return foto;
    }

}
