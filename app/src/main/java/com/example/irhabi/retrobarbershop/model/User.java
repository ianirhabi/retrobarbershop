package com.example.irhabi.retrobarbershop.model;


/**
 * Created By Programmer Jalan on January 2018
 */
public class User {

    private String username;
    @com.google.gson.annotations.SerializedName("password")
    private String password;
    @com.google.gson.annotations.SerializedName("status")
    private String status ;
    @com.google.gson.annotations.SerializedName("data")
    private Responsdata respons ;


    public User (String namaa, String passs){
        this.username = namaa;
        this.password = passs;
    }

    public String getStatus(){
        return status;
    }
    public Responsdata getResponsdata(){
        return respons;
    }

}
