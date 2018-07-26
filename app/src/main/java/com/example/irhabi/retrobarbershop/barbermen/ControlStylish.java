package com.example.irhabi.retrobarbershop.barbermen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.ResetPasswordActivity;

public class ControlStylish extends AppCompatActivity {
    private Button karyawan, Edit, Laporan;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_stylish);
        karyawan = (Button)findViewById(R.id.karyawan);
        Edit = (Button)findViewById(R.id.rubahakun);
        foto =(ImageView)findViewById(R.id.imageseperadmin) ;
        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        foto.startAnimation(rotate);
        karyawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlStylish.this, Barbermen.class);
                startActivity(i);
                finish();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlStylish.this, ResetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
