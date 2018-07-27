package com.example.irhabi.retrobarbershop.newmasuklogin;

/**
 * Created BY Programmer Jalan on January 2018
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanFragment extends AppCompatActivity {
    Bitmap bitmap ;
    public final static int QRcodeWidth = 350 ;
    private  Absen dataabsen;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scan);

        IntentIntegrator integrator = new IntentIntegrator(ScanFragment.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getApplicationContext(),result.getContents(), Toast.LENGTH_LONG).show();
                Log.e("Scan*******", "Cancelled scan");
            } else {
                Log.e("Scan", "Scanned");
                DateFormat dateFormat = new SimpleDateFormat("H:mm:ss");
                Date date = new Date();

                String a = result.getContents() ;
                String[] kf = a.split("\\s");
                String hari = kf[0];
                String tanggal = kf [1];
                String waktu = dateFormat.format(date);
                String hadir = kf[2];
                String iduser = kf[3];
                String lat = kf[4];
                String lon = kf[5];
                String usr = kf[6];

                int id_user = Integer.parseInt(iduser);
                dataabsen = new Absen(hari, tanggal, waktu, hadir,id_user , lat, lon, usr);
                Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
                Call<Absen> call = service.post(dataabsen);
                call.enqueue(new Callback<Absen>() {
                    @Override
                    public void onResponse(Call<Absen> call, Response<Absen> response) {
                        Toast.makeText(ScanFragment.this, "Berhasil di kirim ke Server", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ScanFragment.this,KonekMaps.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Absen> call, Throwable t) {
                        Toast.makeText(ScanFragment.this, "Gagal Mengirim Ke Server", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ScanFragment.this,KonekMaps.class);
                        startActivity(i);
                        finish();
                    }
                });

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent i = new Intent(ScanFragment.this,KonekMaps.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
