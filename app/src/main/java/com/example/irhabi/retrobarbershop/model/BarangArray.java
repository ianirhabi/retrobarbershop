package com.example.irhabi.retrobarbershop.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BarangArray {
    @SerializedName("status")
    String status;

    @SerializedName("data")
    private ArrayList<Barang> data;


    public String getRespons(){
        return status;
    }

    public ArrayList<Barang> getBarangarray() {
        return data;
    }

    public void setBarangArrayList(ArrayList<Barang> BarangArrayList) {
        this.data = BarangArrayList;
    }


}
