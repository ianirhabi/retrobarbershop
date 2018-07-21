package com.example.irhabi.retrobarbershop.model;

public class Absen{
    @com.google.gson.annotations.SerializedName("Tanggal")
    private String tanggal;
    @com.google.gson.annotations.SerializedName("Waktu")
    private String  waktu;
    @com.google.gson.annotations.SerializedName("Hadir")
    private String hadir;
    @com.google.gson.annotations.SerializedName("ID_USER")
    private String id_user;
    @com.google.gson.annotations.SerializedName("Hari")
    private String hari;
    @com.google.gson.annotations.SerializedName("Lat")
    private String lat;
    @com.google.gson.annotations.SerializedName("Long")
    private String lon;

    public Absen(String T, String W, String H,String J, String L,
                 String K, String F){
        this.tanggal = T;
        this.waktu = W;
        this.hadir = H;
        this.id_user= J;
        this.hari= L;
        this.lat = K;
        this.lon=F;
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
    public String getId_user(){return id_user;}
    public  String getHari(){return hari;}
    public String getLat(){return lat;}
    public String getLon(){return lon;}

}
