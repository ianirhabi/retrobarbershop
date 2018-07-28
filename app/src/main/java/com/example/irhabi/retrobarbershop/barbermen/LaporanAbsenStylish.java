package com.example.irhabi.retrobarbershop.barbermen;
/**
 * Created by Programmer Jalanan 26/07/2018
 */

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.model.Absenarray;
import com.example.irhabi.retrobarbershop.newmasuklogin.AbsenAdapter;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanAbsenStylish extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SessionManager sesi;
    private AbsenAdapter adapterabsen;
    private RecyclerView recyclerView;
    private EditText editText, editTextto;
    public Handler mHandler;
    SwipeRefreshLayout swipeLayout;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_absen_stylish);

        final Calendar myCalendar = Calendar.getInstance();
         editText= (EditText) findViewById(R.id.stylishfrom);
         editTextto= (EditText) findViewById(R.id.stylishto);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        editText.setText(dateFormat.format(date));
        editTextto.setText(dateFormat.format(date));
        Bundle b = getIntent().getExtras();

        final int get_id = b.getInt("parse_id");

        Takedata(dateFormat.format(date),dateFormat.format(date),get_id);

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
                String totanggal = editTextto.getText().toString();
                editText.setText(sdf.format(myCalendar.getTime()));
                String fromtanggal = editText.getText().toString();
                Toast.makeText(getApplicationContext(), "Anda Menampilkan Data Dari Tanggal " + fromtanggal + " Sampai Tanggal " + totanggal , Toast.LENGTH_LONG).show();

                Takedata(fromtanggal,totanggal, get_id);
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
                String fromtanggal = editText.getText().toString();

                editTextto.setText(sdf.format(myCalendar.getTime()));
                String totanggal = editTextto.getText().toString();
                Toast.makeText(getApplicationContext(), "Anda Menampilkan Data Dari Tanggal " + fromtanggal + " Sampai Tanggal " + totanggal  , Toast.LENGTH_LONG).show();

                Takedata(fromtanggal,totanggal,get_id);
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LaporanAbsenStylish.this, datefrom, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editTextto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LaporanAbsenStylish.this, dateto, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swype);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark);

    }

    public void Takedata(String from, String to, int id){

        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        Call<Absenarray> call = service.rangedataabsen(id,from, to );
        call.enqueue(new Callback<Absenarray>() {
            @Override
            public void onResponse(Call<Absenarray> call, Response<Absenarray> response) {
                generateAbsen(response.body().getAbsenarray());
            }
            @Override
            public void onFailure(Call<Absenarray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data Dari Server Pastikan Anda Terhubung Dengan Internet " , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateAbsen(ArrayList<Absen> Arrayabsen) {
        recyclerView = findViewById(R.id.recycler_view_notice_list);
        adapterabsen = new AbsenAdapter(Arrayabsen);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterabsen);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                Bundle b = getIntent().getExtras();
                int get_id = b.getInt("parse_id");
                swipeLayout.setRefreshing(false);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                final EditText edittext= (EditText) findViewById(R.id.from);
                final EditText edittextto= (EditText) findViewById(R.id.to);
                editText.setText(dateFormat.format(date));
                editTextto.setText(dateFormat.format(date));
                Takedata(dateFormat.format(date),dateFormat.format(date), get_id);
            }
        }, 1000);
    }
}
