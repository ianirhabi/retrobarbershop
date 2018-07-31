package com.example.irhabi.retrobarbershop.model;

/**
 * Created BY Progremmer Jalan on January 2018
 */
public class Absen{
    @com.google.gson.annotations.SerializedName("res")
    private String status;
    @com.google.gson.annotations.SerializedName("user")
    private String usr;
    @com.google.gson.annotations.SerializedName("Tanggal")
    private String tanggal;
    @com.google.gson.annotations.SerializedName("Waktu")
    private String  waktu;
    @com.google.gson.annotations.SerializedName("Hadir")
    private String hadir;
    @com.google.gson.annotations.SerializedName("ID_USER")
    private int id_user;
    @com.google.gson.annotations.SerializedName("Hari")
    private String hari;
    @com.google.gson.annotations.SerializedName("Lat")
    private String lat;
    @com.google.gson.annotations.SerializedName("Long")
    private String lon;
    @com.google.gson.annotations.SerializedName("alasan")
    private String alasan;
    @com.google.gson.annotations.SerializedName("Pesan")
    private String Pesan;

    public Absen(String hari, String tanggal, String waktu, String hadir, int id_user,
                 String lat, String lon, String usr, String alasan){
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.hadir = hadir;
        this.id_user= id_user;
        this.hari= hari;
        this.lat = lat;
        this.lon = lon;
        this.usr = usr;
        this.alasan = alasan;

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
    public int getId_user(){return id_user;}
    public  String getHari(){return hari;}
    public String getLat(){return lat;}
    public String getLon(){return lon;}
    public  String getPesan(){
        return Pesan;
    }
    public String getStatus() {
        return status;
    }
}
