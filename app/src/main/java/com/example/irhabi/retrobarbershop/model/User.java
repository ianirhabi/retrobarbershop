package com.example.irhabi.retrobarbershop.model;
import com.google.gson.annotations.SerializedName;

public class User {

    /**
     * harus sesuaikan dengan jsonnya
     */

    private String Res ;
    private String username;
    private String password;
    @com.google.gson.annotations.SerializedName("Usr")
    private Usr usr ;



    public User (String namaa, String passs){
        this.username = namaa;
        this.password = passs;
    }

    public String getsucs(){
        return username ;
    }
    public String getStatus(){
        return Res;
    }
    public Usr getUsr(){
        return usr ;
    }
}
