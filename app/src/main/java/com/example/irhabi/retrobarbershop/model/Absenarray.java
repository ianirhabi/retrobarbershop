package com.example.irhabi.retrobarbershop.model;

import java.util.ArrayList;


public class Absenarray{

    @com.google.gson.annotations.SerializedName("data")
    private ArrayList<Absen> data;

    public ArrayList<Absen> getAbsenarray() {
        return data;
    }
    public void setabsenArrayList(ArrayList<Absen> AbsenArrayList) {
        this.data = AbsenArrayList;
    }

}
