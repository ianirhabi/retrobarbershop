package com.example.irhabi.retrobarbershop.barbermen;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.alert.ViewDialog;
import com.example.irhabi.retrobarbershop.barang.BarangActivity;
import com.example.irhabi.retrobarbershop.newmasuklogin.Setting;
import com.example.irhabi.retrobarbershop.pembayaran.Pembayaran;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ControlStylish extends AppCompatActivity {
    private Button inputbarang, Edit, Laporan, tambah, order;
    private ImageView foto, shutdown;
    private SessionManager sesi;
    private ViewDialog alert;
    private TextView tanggal;
    private  String arrayName []= {"Absen Karyawan", "Edit Akun"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_stylish);

        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.menucircle);
        inputbarang = (Button)findViewById(R.id.inputbarang);
        Edit = (Button)findViewById(R.id.rubahakun);
        foto =(ImageView)findViewById(R.id.imageseperadmin) ;
        tambah = (Button)findViewById(R.id.tambahakun);
        Laporan = (Button)findViewById(R.id.penjualan);
        tanggal = (TextView)findViewById(R.id.date) ;
        shutdown = (ImageView)findViewById(R.id.logout) ;
        order = (Button)findViewById(R.id.Order);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.iconret, R.drawable.ic_settings_white_24dp)
                .addSubMenu(Color.parseColor("#258cff"),R.drawable.buttomnbiru)
                .addSubMenu(Color.parseColor("#6d4c41"),R.drawable.button_merah)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        Toast.makeText(getApplicationContext(),"Anda memilih menu " + arrayName[i], Toast.LENGTH_LONG).show();
                        if(arrayName[i].equals("Absen Karyawan")){
                            sesi = new SessionManager(getApplicationContext());
                            String a = "1" ;
                            sesi.createControlKaryawaan(a);
                            Intent pindah = new Intent(ControlStylish.this, Barbermen.class);
                            startActivity(pindah);
                            finish();
                        }else if((arrayName[i].equals("Edit Akun"))){
                            sesi = new SessionManager(getApplicationContext());
                            String a = "2" ;
                            sesi.createControlKaryawaan(a);
                            Intent pindah = new Intent(ControlStylish.this, Barbermen.class);
                            startActivity(pindah);
                            finish();
                        }
                    }
                });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlStylish.this, Pembayaran.class);
                startActivity(i);
                finish();
            }
        });
        shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesi = new SessionManager(getApplicationContext());
                sesi.logoutUser();
                finish();
            }
        });

        alert = new ViewDialog();
        alert.showDialog(ControlStylish.this, "Welcome SuperAdmin", ControlStylish.this);

        DateFormat dateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
        Date date = new Date();

        String waktu = dateFormat.format(date);
        tanggal.setText(waktu);
        Laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Maaf menu ini masih dalam tahap pengembangan ",Toast.LENGTH_LONG ).show();
            }
        });

        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        foto.startAnimation(rotate);
        inputbarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sesi = new SessionManager(getApplicationContext());
//                String a = "1" ;
//                sesi.createControlKaryawaan(a);
                Intent i = new Intent(ControlStylish.this, BarangActivity.class);
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
