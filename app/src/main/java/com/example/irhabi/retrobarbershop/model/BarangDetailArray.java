package com.example.irhabi.retrobarbershop.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BarangDetailArray {

    @SerializedName("data_detail")
    private ArrayList<BarangDetail> datadetail;
    @SerializedName("status")
    String status;
    @SerializedName("total")
    String total;


    public String getStatus() {
        return status;
    }

    public String getTotal() {
        return total;
    }

    public ArrayList<BarangDetail> getDatadetail() {
        return datadetail;
    }

}
