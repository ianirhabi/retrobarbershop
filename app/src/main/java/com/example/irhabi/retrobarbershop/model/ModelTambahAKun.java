package com.example.irhabi.retrobarbershop.model;

import com.google.gson.annotations.SerializedName;

public class ModelTambahAKun {
    @SerializedName("nama")
    String nama;
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("usergrup")
    String usergrup;
    @SerializedName("status")
    String status;


    public ModelTambahAKun(String nama,String gender, String alamat, String nik, String usergrup, String username, String password){
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.usergrup = usergrup;
    }

    public String getstatus(){
        return status;
    }
}
