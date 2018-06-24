package com.example.irhabi.retrobarbershop.rest;

import android.app.DownloadManager;

import com.example.irhabi.retrobarbershop.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Router {

    @POST("user")
    Call<User> Login(@Body User user);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadPhoto(
            @Part("description")RequestBody description,
            @Part MultipartBody.Part photo
    );
}
