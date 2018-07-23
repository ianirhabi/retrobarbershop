package com.example.irhabi.retrobarbershop.barbermen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.irhabi.retrobarbershop.R;

public class ControlStylish extends AppCompatActivity {
private Button karyawan, Edit, Laporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_stylish);
        karyawan = (Button)findViewById(R.id.karyawan);

        karyawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlStylish.this, Barbermen.class);
                startActivity(i);
                finish();
            }
        });
    }
}
