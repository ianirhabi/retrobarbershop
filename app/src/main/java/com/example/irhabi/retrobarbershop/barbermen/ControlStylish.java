package com.example.irhabi.retrobarbershop.barbermen;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
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
import com.example.irhabi.retrobarbershop.newmasuklogin.ScanFragment;
import com.example.irhabi.retrobarbershop.pembayaran.Pembayaran;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControlStylish extends AppCompatActivity {
    private Button inputbarang, Edit, Laporan, tambah, order;
    private ImageView foto, shutdown;
    private SessionManager sesi;
    private ViewDialog alert;
    private TextView tanggal;
    private boolean timerHasStarted = false;
    private CountDownTimer countDownTimer;
    private final long startTime = 1 * 1000;
    private final long interval = 1 * 1000;

    private  String arrayName []= {"Absen Karyawan", "Edit Akun",
            "tambah", "scan", "barang", "order"};

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
                .addSubMenu(Color.parseColor("#258CFF"),R.drawable.anggota)
                .addSubMenu(Color.parseColor("#8B0000"),R.drawable.edit)
                .addSubMenu(Color.parseColor("#FF8C00"),R.drawable.ic_add)
                .addSubMenu(Color.parseColor("#53933f"),R.drawable.scan)
                .addSubMenu(Color.parseColor("#43923f"),R.drawable.baranginput)
                .addSubMenu(Color.parseColor("#54733f"),R.drawable.order)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        countDownTimer = new MyCountDownTimer(startTime, interval, arrayName[i]);
                        countDownTimer.start();
                        timerHasStarted = true;
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

    class MyCountDownTimer extends CountDownTimer {
        String status;
        public MyCountDownTimer(long startTime, long interval, String status) {
            super(startTime, interval);
            this.status = status;
        }

        @Override
        public void onFinish() {
            if(status.equals("Absen Karyawan")){
                sesi = new SessionManager(getApplicationContext());
                String a = "1" ;
                sesi.createControlKaryawaan(a);
                Intent pindah = new Intent(ControlStylish.this, Barbermen.class);
                startActivity(pindah);
                finish();
            }else if((status.equals("Edit Akun"))){
                sesi = new SessionManager(getApplicationContext());
                String a = "2" ;
                sesi.createControlKaryawaan(a);
                Intent pindah = new Intent(ControlStylish.this, Barbermen.class);
                startActivity(pindah);
                finish();
            }else if((status.equals("tambah"))){
                Intent pindah = new Intent(ControlStylish.this, TambahKaryawan.class);
                startActivity(pindah);
                finish();
            }else if((status.equals("scan"))){
                Intent pindah = new Intent(ControlStylish.this, ScanFragment.class);
                startActivity(pindah);
                finish();
            }else if(status.equals("barang")){
                Intent pindah = new Intent(ControlStylish.this, BarangActivity.class);
                startActivity(pindah);
                finish();
            }else if(status.equals("order")){
                Intent pindah = new Intent(ControlStylish.this, Pembayaran.class);
                startActivity(pindah);
                finish();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
           //give the code
        }
    }
}
