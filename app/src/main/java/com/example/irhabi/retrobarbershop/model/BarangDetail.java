package com.example.irhabi.retrobarbershop.model;

import com.google.gson.annotations.SerializedName;

public class BarangDetail {
    @SerializedName("id")
    int id;
    @SerializedName("code")
    String kode;
    @SerializedName("stock")
    String stock;
    @SerializedName("harga_pokok")
    int hp;
    @SerializedName("harga_jual")
    int hj;
    @SerializedName("category_code")
    int cate_code;
    @SerializedName("deskripsi")
    String desc;
    @SerializedName("created")
    String created;
    @SerializedName("updateDate")
    String updateDate;
    @SerializedName("status")
    String status;
    @SerializedName("data")
    int statusData;
    @SerializedName("userid")
    int userid;

    public BarangDetail(String kode, String stock, int hp, int hj, int cate_code, String desc,
                        String created, String updateDate, int userid) {
        this.kode = kode;
        this.stock = stock;
        this.hp = hp;
        this.hj = hj;
        this.cate_code = cate_code;
        this.desc = desc;
        this.created = created;
        this.updateDate = updateDate;
        this.userid = userid;
    }

    public int getStatusData() {
        return statusData;
    }

    public String getStatus() {
        return status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHj() {
        return hj;
    }

    public void setHj(int hj) {
        this.hj = hj;
    }

    public int getCate_code() {
        return cate_code;
    }

    public void setCate_code(int cate_code) {
        this.cate_code = cate_code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
