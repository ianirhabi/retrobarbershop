package com.example.irhabi.retrobarbershop.barbermen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.ResetPasswordActivity;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.ResourceBundle;

public class ControlStylish extends AppCompatActivity {
    private Button karyawan, Edit, Laporan, tambah;
    private ImageView foto;
    private SessionManager sesi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_stylish);
        karyawan = (Button)findViewById(R.id.karyawan);
        Edit = (Button)findViewById(R.id.rubahakun);
        foto =(ImageView)findViewById(R.id.imageseperadmin) ;
        tambah = (Button)findViewById(R.id.tambahakun);
        Laporan = (Button)findViewById(R.id.penjualan);

        Laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Maaf menu ini masih dalam tahap pengembangan ",Toast.LENGTH_LONG ).show();
            }
        });

        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        foto.startAnimation(rotate);
        karyawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesi = new SessionManager(getApplicationContext());
                String a = "1" ;
                sesi.createControlKaryawaan(a);
                Intent i = new Intent(ControlStylish.this, Barbermen.class);
                startActivity(i);
                finish();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesi = new SessionManager(getApplicationContext());
                String a = "2" ;
                sesi.createControlKaryawaan(a);
                Intent i = new Intent(ControlStylish.this, Barbermen.class);
                startActivity(i);
                finish();
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlStylish.this, TambahKaryawan.class);
                startActivity(i);
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent i = new Intent(ControlStylish.this, KonekMaps.class);
        startActivity(i);
        return true;
    }
}
