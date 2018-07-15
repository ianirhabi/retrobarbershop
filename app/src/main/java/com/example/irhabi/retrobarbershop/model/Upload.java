package com.example.irhabi.retrobarbershop.model;

public class Upload {
    @com.google.gson.annotations.SerializedName("upload")
    private String  uplod ;
    @com.google.gson.annotations.SerializedName("respon")
    private String  respon ;
    @com.google.gson.annotations.SerializedName("id")
    private long  id ;

    public Upload (String uplod, long id){
        this.uplod = uplod;
        this.id = id;
    }
    public String getupload(){
        return uplod;
    }
    public String getRespon(){
        return respon;
    }
}
