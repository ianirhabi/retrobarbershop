package com.example.irhabi.retrobarbershop.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BarangArray {
    @SerializedName("status")
    String status;

    @SerializedName("data")
    private ArrayList<Barang> data;

    @SerializedName("total")
    String total;


    public String getRespons(){
        return status;
    }

    public ArrayList<Barang> getBarangarray() {
        return data;
    }

    public  String getTotal(){
        return total;
    }
}
