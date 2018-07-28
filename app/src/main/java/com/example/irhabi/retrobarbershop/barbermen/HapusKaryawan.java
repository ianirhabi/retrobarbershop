package com.example.irhabi.retrobarbershop.barbermen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.User;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HapusKaryawan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hapus_karyawan);

        Bundle b = getIntent().getExtras();
        final int Barberid = b.getInt("parse_id");

        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        Call<User> call = service.delete(Barberid);
        call.enqueue(new Callback<User>() {


            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplicationContext(), "status nya "+ response.body().getStatus(),Toast.LENGTH_LONG ).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
