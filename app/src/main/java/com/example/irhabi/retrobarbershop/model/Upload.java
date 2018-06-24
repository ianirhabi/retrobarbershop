package com.example.irhabi.retrobarbershop.model;

public class Upload {
    @com.google.gson.annotations.SerializedName("upload")
    private String  uplod ;

    public Upload (String uplod){
        this.uplod = uplod;
    }
    public String getupload(){
        return uplod;
    }
}
