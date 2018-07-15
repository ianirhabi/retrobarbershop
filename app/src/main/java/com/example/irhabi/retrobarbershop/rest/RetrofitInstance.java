package com.example.irhabi.retrobarbershop.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.irhabi.retrobarbershop.rest.AppConfig.URL;

public class RetrofitInstance {

    private static Retrofit retrofit;
    /**
     * Create an instance of Retrofit object
     * */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}