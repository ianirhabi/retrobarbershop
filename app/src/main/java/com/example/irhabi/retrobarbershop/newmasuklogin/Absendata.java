package com.example.irhabi.retrobarbershop.newmasuklogin;

/**
 * Created BY Programmer Jalanan on January 2018
 */

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.model.Absenarray;
import com.example.irhabi.retrobarbershop.model.Usr;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.zip.Inflater;

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
            try {
                Sendnotification();
            }catch (Exception e){

            }
            Absendata.this.mHandler.postDelayed(Absendata.this.m_Runnable,5000);
        }
    };

    Runnable connection;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Calendar myCalendar = Calendar.getInstance();

        View view =inflater.inflate(R.layout.fragment_absen,container,false);
        final EditText edittext= (EditText) view.findViewById(R.id.from);
        final EditText edittextto= (EditText) view.findViewById(R.id.to);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        edittext.setText(dateFormat.format(date));
        edittextto.setText(dateFormat.format(date));
        Takedata(dateFormat.format(date),dateFormat.format(date));

        final DatePickerDialog.OnDateSetListener datefrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String totanggal = edittextto.getText().toString();
                edittext.setText(sdf.format(myCalendar.getTime()));
                String fromtanggal = edittext.getText().toString();
                Toast.makeText(getContext(), "Anda Menampilkan Data Dari Tanggal " + fromtanggal + " Sampai Tanggal " + totanggal , Toast.LENGTH_LONG).show();

                Takedata(fromtanggal,totanggal);
            }
        };

        final DatePickerDialog.OnDateSetListener dateto = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String fromtanggal = edittext.getText().toString();

                edittextto.setText(sdf.format(myCalendar.getTime()));
                String totanggal = edittextto.getText().toString();
                Toast.makeText(getContext(), "Anda Menampilkan Data Dari Tanggal " + fromtanggal + " Sampai Tanggal " + totanggal  , Toast.LENGTH_LONG).show();

                Takedata(fromtanggal,totanggal);
            }
        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), datefrom, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edittextto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateto, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swype);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark);
//                android.R.color.holo_red_dark,
//                android.R.color.holo_blue_dark,
//                android.R.color.holo_orange_dark);

        this.mHandler = new Handler();
        this.mHandler.postDelayed(this.m_Runnable, 30000);
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
                sesi = new SessionManager(getActivity());
                HashMap<String, String> usersesion = sesi.getUserDetails();
                String user  = usersesion.get(SessionManager.KEY_USER);
                DateFormat dateFormat = new SimpleDateFormat("H:mm:ss");
                Date date = new Date();
                if(user.equals("superadmin")){
                    if (response.body().getNotif().equals("2")){
                    sendNotification("Notification", "Stylish Berhasil Mengambil absen pada jam " +dateFormat.format(date));
                    }
                }else {
                    if (response.body().getNotif().equals("2")) {
                        sendNotification("Notification", "Anda Berhasil Mengambil Absen");
                        String Id = String.valueOf(response.body().Getid());
                        sesi = new SessionManager(getActivity());
                        sesi.createLoginSession(response.body().getUser(),
                                response.body().getUsergrup(), Id);
                    } else  {
                        String Id = String.valueOf(response.body().Getid());
                        sesi = new SessionManager(getActivity());
                        sesi.createLoginSession(response.body().getUser(),
                                response.body().getUsergrup(), Id);
                    }
                }
                if(response.body().getIanmonitor().equals("2")){

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
                .setSmallIcon(R.drawable.iconret)
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
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                final EditText edittext= (EditText) getView().findViewById(R.id.from);
                final EditText edittextto= (EditText) getView().findViewById(R.id.to);
                edittext.setText(dateFormat.format(date));
                edittextto.setText(dateFormat.format(date));
                Takedata(dateFormat.format(date),dateFormat.format(date));
            }
        }, 1000);
    }

    public void Takedata(String from, String to){
        String id;
        sesi = new SessionManager(getActivity());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        id = usersesion.get(SessionManager.KEY_ID);
        int idDetail = Integer.parseInt(id);
        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        Call<Absenarray> call = service.rangedataabsen(idDetail,from, to );
        call.enqueue(new Callback<Absenarray>() {
            @Override
            public void onResponse(Call<Absenarray> call, Response<Absenarray> response) {
                generateAbsen(response.body().getAbsenarray());
            }
            @Override
            public void onFailure(Call<Absenarray> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal Mengambil Data Dari Server Pastikan Anda Terhubung Dengan Internet " , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
