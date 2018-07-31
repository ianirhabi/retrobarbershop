package com.example.irhabi.retrobarbershop.barbermen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Responhapuskaryawan;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HapusKaryawan extends AppCompatActivity {
    private SessionManager sesi;
    private RetrofitInstance retro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hapus_karyawan);

        Bundle b = getIntent().getExtras();
        final int Barberid = b.getInt("parse_id");

        sesi = new SessionManager(getApplicationContext());
        final HashMap<String, String> usersesion = sesi.getUserDetails();
        String token = usersesion.get(SessionManager.TOKEN);
        Log.d("debug token ", " : " + token);
        retro = new RetrofitInstance(token);

        Router service = retro.getRetrofitInstanceall().create(Router.class);
        Call<Responhapuskaryawan> call = service.delete(Barberid);
        call.enqueue(new Callback<Responhapuskaryawan>() {
            @Override
            public void onResponse(Call<Responhapuskaryawan> call, Response<Responhapuskaryawan> response) {
                if(response.body().getstatus().equals("sukses")) {
                    Toast.makeText(getApplicationContext(), "Anda Berhasil Menghapus Data Karyawan dengan status " + response.body().getstatus(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(HapusKaryawan.this, ControlStylish.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Gagal Menghapus Data Karyawan " + response.body().getstatus(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(HapusKaryawan.this, ControlStylish.class);
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Responhapuskaryawan> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Masih Dalam Perbaikan " + t,Toast.LENGTH_LONG ).show();
            }
        });
    }
}
