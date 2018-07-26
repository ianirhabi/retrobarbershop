package com.example.irhabi.retrobarbershop.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AlluserRespons {
    @SerializedName("status")
        String status;

    @SerializedName("data")
        ArrayList<Usr> data;


    public String getStatus() {
        return status;
    }

    public ArrayList<Usr> getALluser(){
        return data;
    }
}
