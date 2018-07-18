package com.example.irhabi.retrobarbershop.model;

public class Absen{
    @com.google.gson.annotations.SerializedName("Tanggal")
    private String tanggal;
    @com.google.gson.annotations.SerializedName("Waktu")
    private String  waktu;
    @com.google.gson.annotations.SerializedName("Hadir")
    private String hadir;

    public Absen(String T, String W, String H){
        this.tanggal = T;
        this.waktu = W;
        this.hadir = H;
    }

    public String gettangal(){
        return tanggal;
    }
    public String getwaktu(){
        return waktu;
    }
    public String gethadir(){
        return hadir;
    }

}
