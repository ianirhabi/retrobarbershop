package com.example.irhabi.retrobarbershop.model;

/**
 * Created BY Progrmmer Jalan on January 2018
 */
import java.util.ArrayList;


public class Absenarray{



    @com.google.gson.annotations.SerializedName("res")
    String res;

    @com.google.gson.annotations.SerializedName("data")
    private ArrayList<Absen> data;

    public ArrayList<Absen> getAbsenarray() {
        return data;
    }
    public void setabsenArrayList(ArrayList<Absen> AbsenArrayList) {
        this.data = AbsenArrayList;
    }

    public String getRespons(){
        return res;
    }
}
