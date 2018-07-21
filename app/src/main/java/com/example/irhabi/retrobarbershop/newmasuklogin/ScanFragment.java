package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;

import com.example.irhabi.retrobarbershop.error.Gagal;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.model.Absenarray;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

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
                Toast.makeText(getApplicationContext(),"deug berhasil " + result.getContents(), Toast.LENGTH_LONG).show();
                Log.e("Scan", "Scanned");
                String a = result.getContents() ;
                String[] kf = a.split("\\s");
                String hari = kf[0];
                String tanggal = kf [1];
                String waktu = kf[2];
                String hadir = kf[3];
                String iduser = kf[4];
                String lat = kf[5];
                String lon = kf[6];
                String usr = kf[7];

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
                    }
                });

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
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
