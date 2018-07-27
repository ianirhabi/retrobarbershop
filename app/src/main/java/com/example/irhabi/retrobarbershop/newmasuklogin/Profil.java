package com.example.irhabi.retrobarbershop.newmasuklogin;
/**
 * Created by Programmer Jalanan on 17/07/2018
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoViewAttacher;


import static com.example.irhabi.retrobarbershop.rest.AppConfig.URL;

public class Profil extends Fragment {
    public final static int QRcodeWidth = 350 ;
    private Bitmap bitmap ;
    private SessionManager sesi;
    private String usr, usrgrup, id, lo, la;
    private ImageView barber, BarbermenPhoto, BarbermenPhoto1;
    private boolean timerHasStarted = false;
    private CountDownTimer countDownTimer;
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    private Button btnStart;
    private TextView text, nameretro, userretro;
    PhotoViewAttacher photoAttacher;
    private ImageButton close;
    private LinearLayout layout;
    public Profil(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profil_dua,container,false);

        sesi = new SessionManager(getActivity());
        final HashMap<String, String> usersesion = sesi.getUserDetails();
        usr = usersesion.get(SessionManager.KEY_USER);
        usrgrup = usersesion.get(SessionManager.KEY_USERGRUP);
        id = usersesion.get(SessionManager.KEY_ID);
        la = usersesion.get(SessionManager.LATITUDE);
        lo = usersesion.get(SessionManager.LONGTITUDE);
        final int Barberid = Integer.parseInt(id);
        text = (TextView) view.findViewById(R.id.timer);
      //  Toast.makeText(getActivity(),usr + " " + usrgrup + " " + Barberid +" " + la + " "+ lo, Toast.LENGTH_LONG).show();
        barber = (ImageView)view.findViewById(R.id.codeQR);
        btnStart = (Button) view.findViewById(R.id.buttonStart);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        nameretro = (TextView)view.findViewById(R.id.Barbermenname);
        userretro = (TextView)view.findViewById(R.id.nohp);
        BarbermenPhoto = (ImageView)view.findViewById(R.id.proffoto);
        BarbermenPhoto1 = (ImageView)view.findViewById(R.id.proffotobesar);

        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        int idDetail = Integer.parseInt(id);
        Call<Usr> call = service.retro(idDetail);
        call.enqueue(new Callback<Usr>() {
            @Override
            public void onResponse(Call<Usr> call, final Response<Usr> response) {
                nameretro.setText(response.body().getname());
                userretro.setText(response.body().getUser());
                String[] kf = response.body().getnamefoto().split("\\.");
                final String first = kf[0];
                sesi = new SessionManager(getActivity());
                sesi.createImage(first);
                Log.d("DEBUG ", "BARU " + first);
                Glide
                        .with(Profil.this)
                        .load(URL + "upload/" + first)
                        .into(BarbermenPhoto);
                String Id = String.valueOf(response.body().Getid());
                sesi.createLoginSession(response.body().getUser(),
                        response.body().getUsergrup(),Id);

                BarbermenPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BarbermenPhoto1.setVisibility(View.VISIBLE);
                        Glide
                                .with(getActivity())
                                .load(URL + "upload/" + first)
                                .into(BarbermenPhoto1);
                        String Id = String.valueOf(response.body().Getid());
                        sesi.createLoginSession(response.body().getUser(),
                                response.body().getUsergrup(),Id);
                        BarbermenPhoto1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BarbermenPhoto1.setVisibility(View.GONE);

                            }
                        });
                    }
                });

                try {
                    DateFormat dateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                    Date date = new Date();
                    bitmap = TextToImageEncode(dateFormat.format(date)  + " hadir " + Barberid  +  " " + la + " "+ lo + " " + usr);
                    barber.setImageBitmap(bitmap);

                }catch (WriterException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Usr> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal Mengambil Data Dari Server Pastikan Anda Terhubung Dengan Internet " , Toast.LENGTH_SHORT).show();
                barber.setImageResource(R.drawable.retroo);
            }
        });

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
