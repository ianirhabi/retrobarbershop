package com.example.irhabi.retrobarbershop.error;

/**
 * Created BY Progrmmer Jalan on January 2018
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.newmasuklogin.ScanFragment;

public class Gagal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gagal);
        Intent i = new Intent(Gagal.this,KonekMaps.class);
        startActivity(i);
        finish();

    }
}
