package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Usr;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.irhabi.retrobarbershop.newmasuklogin.ScanFragment.QRcodeWidth;
import static com.example.irhabi.retrobarbershop.rest.AppConfig.URL;

public class Profil extends Fragment {
    public final static int QRcodeWidth = 350 ;
    private Bitmap bitmap ;
    private SessionManager sesi;
    private String usr, usrgrup, id, lo, la;
    private ImageView barber, BarbermenPhoto;
    Handler mHandler ;
    private boolean timerHasStarted = false;
    private CountDownTimer countDownTimer;
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    private Button btnStart;
    private TextView text, nameretro, userretro;
    private SessionManager sessi;

    public Profil(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profil_dua,container,false);

        sesi = new SessionManager(getActivity());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        usr = usersesion.get(SessionManager.KEY_USER);
        usrgrup = usersesion.get(SessionManager.KEY_USERGRUP);
        id = usersesion.get(SessionManager.KEY_ID);
        la = usersesion.get(SessionManager.LATITUDE);
        lo = usersesion.get(SessionManager.LONGTITUDE);
        int Barberid = Integer.parseInt(id);
        text = (TextView) view.findViewById(R.id.timer);
        Toast.makeText(getActivity(),usr + " " + usrgrup + " " + Barberid +" " + la + " "+ lo, Toast.LENGTH_LONG).show();
        barber = (ImageView)view.findViewById(R.id.codeQR);
        btnStart = (Button) view.findViewById(R.id.buttonStart);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        nameretro = (TextView)view.findViewById(R.id.Barbermenname);
        userretro = (TextView)view.findViewById(R.id.nohp);
        BarbermenPhoto = (ImageView)view.findViewById(R.id.proffoto);
        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        int idDetail = Integer.parseInt(id);
        Call<Usr> call = service.retro(idDetail);
        call.enqueue(new Callback<Usr>() {
            @Override
            public void onResponse(Call<Usr> call, Response<Usr> response) {
                Toast.makeText(getActivity(),response.body().getname(), Toast.LENGTH_LONG).show();
                nameretro.setText(response.body().getname());
                userretro.setText(response.body().getUser());

                String[] kf = response.body().getnamefoto().split("\\.");
                String first = kf[0];
                Log.d("DEBUG ", "BARU " + first);
                Glide
                        .with(getActivity())
                        .load(URL + "upload/" + first)
                        .into(BarbermenPhoto);


            }

            @Override
            public void onFailure(Call<Usr> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        try {
            bitmap = TextToImageEncode(usr + " " + usrgrup + " " + la + " "+ lo);
            barber.setImageBitmap(bitmap);

        }catch (WriterException e) {
            e.printStackTrace();
        }
        text.setText(text.getText() + String.valueOf(startTime / 1000));
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), KonekMaps.class);
                startActivity(i);
            }
        });
        return view;
    }

//    public void barcodeAgain(String usr, String usrgrup, String la,
//                             String lo, TextView tx){
//        try {
//            tx.setText(tx.getText() + String.valueOf(startTime / 1000));
//
//            countDownTimer.start();
//            timerHasStarted = true;
//
//            bitmap = TextToImageEncode(usr + " " + usrgrup + " " + la + " "+ lo);
//            barber.setImageBitmap(bitmap);
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight); //bitMatrixWidth, bitMatrixHeight
        return bitmap;
    }


    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            text.setText("Time's up!");
            barber.setImageResource(R.drawable.retroo);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text.setText("" + millisUntilFinished / 1000);
        }
    }
}
