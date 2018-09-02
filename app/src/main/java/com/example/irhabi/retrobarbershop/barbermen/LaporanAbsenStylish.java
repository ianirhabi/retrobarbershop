package com.example.irhabi.retrobarbershop.barbermen;

/**
 * Created by Programmer Jalanan 26/07/2018
 */

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.dbretro.DBRetro;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.model.Absenarray;
import com.example.irhabi.retrobarbershop.newmasuklogin.AbsenAdapter;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanAbsenStylish extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SessionManager sesi;
    private AbsenAdapter adapterabsen;
    private RecyclerView recyclerView;
    private EditText editText, editTextto;
    public Handler mHandler;
    private RetrofitInstance retro;
    private FloatingActionButton xl;
    SwipeRefreshLayout swipeLayout;
    private DBRetro dbretro;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_absen_stylish);

        final Calendar myCalendar = Calendar.getInstance();
         editText= (EditText) findViewById(R.id.stylishfrom);
         editTextto= (EditText) findViewById(R.id.stylishto);
         xl = (FloatingActionButton)findViewById(R.id.xl);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        editText.setText(dateFormat.format(date));
        editTextto.setText(dateFormat.format(date));
        Bundle b = getIntent().getExtras();

        final int get_id = b.getInt("parse_id");
        final String get_lat = b.getString("parse_lat");
        final String get_lon = b.getString("parse_lon");

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

        xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = editText.getText().toString();
                String to =editTextto.getText().toString();
                Log.e("debug",from +  " " + to);
                ExportToXl(from,to);
            }
        });
    }

    public void Takedata(String from, String to, int id){
        sesi = new SessionManager(getApplicationContext());
        final HashMap<String, String> usersesion = sesi.getUserDetails();
        String token = usersesion.get(SessionManager.TOKEN);
        retro = new RetrofitInstance(token);
        Router service = retro.getRetrofitInstanceall().create(Router.class);

        Call<Absenarray> call = service.rangedataabsen(id,from, to);
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
        adapterabsen = new AbsenAdapter(Arrayabsen, LaporanAbsenStylish.this);
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

    public void ExportToXl(String fromDate, String toDate){
        DBRetro dbretro = new DBRetro(getApplicationContext(), 1);
        Cursor temp = dbretro.getListForAbsen(fromDate,toDate);

        StringBuffer Tanggal = new StringBuffer();
        StringBuffer Jammasuk = new StringBuffer();
        StringBuffer Harimasuk = new StringBuffer();
        StringBuffer Statuskehadiran = new StringBuffer();
        StringBuffer Lat = new StringBuffer();
        StringBuffer Lont = new StringBuffer();

        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "LaporanDashboard.xls";

        File directory = new File(sd.getAbsolutePath());
        //jika file tidak ada akan membuat file baru
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try {
            //file path
            File file = new File(directory, csvFile);

            //file path
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);

            // column and row
            sheet.addCell(new Label(3, 0, "Tanggal"));
            sheet.addCell(new Label(4, 0, "Jam Masuk"));
            sheet.addCell(new Label(5, 0, "Hari Masuk"));
            sheet.addCell(new Label(6, 0, "Status Kehadiran"));
            sheet.addCell(new Label(7, 0, "Latitude"));
            sheet.addCell(new Label(8, 0, "Longtitude"));
            if (temp.moveToFirst()) {
                do {
                    Tanggal.append(temp.getString(1));
                    Harimasuk.append(temp.getString(2));
                    Jammasuk.append(temp.getString(3));
                    Statuskehadiran.append(temp.getString(4));
                    Lat.append(temp.getString(5));
                    Lont.append(temp.getString(6));

                    int i = temp.getPosition() + 1;
                    sheet.addCell(new Label(3, i, Tanggal.toString()));
                    sheet.addCell(new Label(4, i, Jammasuk.toString()));
                    sheet.addCell(new Label(5, i, Harimasuk.toString()));
                    sheet.addCell(new Label(6, i, Statuskehadiran.toString()));
                    sheet.addCell(new Label(7, i, Lat.toString()));
                    sheet.addCell(new Label(8, i, Lont.toString()));

                    Tanggal.setLength(0);
                    Jammasuk.setLength(0);
                    Harimasuk.setLength(0);
                    Statuskehadiran.setLength(0);
                    Lat.setLength(0);
                    Lont.setLength(0);

                } while (temp.moveToNext());
            }
            //closing cursor
            temp.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplicationContext(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();

        } catch (WriteException e) {
            Toast.makeText(getApplicationContext(),
                    "Can't Export", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
