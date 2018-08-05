package com.example.irhabi.retrobarbershop.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ravi on 16/11/17.
 */

public class Barang {
    @SerializedName("status")
    String status;

    @SerializedName("id")
    int id;
    @SerializedName("item_category")
    String item_catagory;
    @SerializedName("code_item")
    String item_code;
    @SerializedName("created")
    String created;
    @SerializedName("user_id")
    int user_id ;




    public Barang(String namekategori, String code, String created, int user_id) {

        this.item_catagory = namekategori;
        this.item_code = code;
        this.created = created;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }
    public String getItem_catagory() {
        return item_catagory;
    }

    public String getItem_code() {
        return item_code;
    }
    public String getCreated(){
        return created;
    }
    public String getStatus(){
        return status;
    }


}
