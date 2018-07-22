package com.example.irhabi.retrobarbershop.newmasuklogin;

/**
 * Created BY Progrmmer Jalan on January 2018
 */

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.model.Absenarray;
import com.example.irhabi.retrobarbershop.model.Usr;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Absendata extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    private AbsenAdapter adapterabsen;
    private RecyclerView recyclerView;
    private SessionManager sesi;
    public  Handler mHandler;

    SwipeRefreshLayout layoutswipe;
    public  Absendata(){
        // Required empty public constructor
    }


    private  final Runnable m_Runnable = new Runnable() {


        @Override
        public void run() {
            Sendnotification();
            Absendata.this.mHandler.postDelayed(Absendata.this.m_Runnable,5000);
        }
    };

    Runnable connection;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_absen,container,false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swype);
        swipeLayout.setOnRefreshListener(this);
        Takedata();
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark);
//                android.R.color.holo_red_dark,
//                android.R.color.holo_blue_dark,
//                android.R.color.holo_orange_dark);

        this.mHandler = new Handler();
        this.mHandler.postDelayed(this.m_Runnable, 5000);
        return view;
    }

    private void generateAbsen(ArrayList<Absen> Arrayabsen) {
        recyclerView = getView().findViewById(R.id.recycler_view_notice_list);
        adapterabsen = new AbsenAdapter(Arrayabsen);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterabsen);
    }

    public void Sendnotification(){

        String id;
        sesi = new SessionManager(getActivity());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        id = usersesion.get(SessionManager.KEY_ID);
        int idDetail = Integer.parseInt(id);
        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        Call<Usr> call = service.retro(idDetail);
        call.enqueue(new Callback<Usr>() {
            @Override
            public void onResponse(Call<Usr> call, Response<Usr> response) {
                if(response.body().getUsergrup().equals("2")) {
                    sendNotification("Notification", "Anda Berhasil Mengambil Absen");
                }else{
                }
            }
            @Override
            public void onFailure(Call<Usr> call, Throwable t) {
            }
        });
    }


    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(getActivity(), Profil.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        // jika fragment, pakai variabel ini ini
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );


        // jika menggunakan activity pakai variabel ini
        // NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onRefresh() {new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            swipeLayout.setRefreshing(false);
            Takedata();
        }

    }, 1000);

    }

    public void Takedata(){
        String id;
        sesi = new SessionManager(getActivity());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        id = usersesion.get(SessionManager.KEY_ID);
        int idDetail = Integer.parseInt(id);
        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        Call<Absenarray> call = service.absenbarberman(idDetail);
        call.enqueue(new Callback<Absenarray>() {
            @Override
            public void onResponse(Call<Absenarray> call, Response<Absenarray> response) {
                generateAbsen(response.body().getAbsenarray());
            }

            @Override
            public void onFailure(Call<Absenarray> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
