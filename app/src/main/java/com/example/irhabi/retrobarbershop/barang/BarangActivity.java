package com.example.irhabi.retrobarbershop.barang;

/**
 * Created By Programmer Jalanan 05/08/2018
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.alert.Inputbarang;
import com.example.irhabi.retrobarbershop.barbermen.ControlStylish;
import com.example.irhabi.retrobarbershop.model.Barang;
import com.example.irhabi.retrobarbershop.model.BarangArray;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BarangActivity extends AppCompatActivity implements BarangsAdapter.BarangAdapterListener {
    private static final String TAG = BarangActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Barang> barangList;
    private BarangsAdapter mAdapter;
    private SearchView searchView;
    private SessionManager sesi;
    private RetrofitInstance retrofit;
    private FloatingActionButton tambah;
    private Inputbarang inputbarangdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barang_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tambah = (FloatingActionButton)findViewById(R.id.tambahbarang);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             sesi = new SessionManager(getApplicationContext());
             sesi.Statusbarang("10");
             inputbarangdialog = new Inputbarang();
             inputbarangdialog.showinput(BarangActivity.this, "Input Item", BarangActivity.this, 0);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BarangActivity.this, ControlStylish.class);
                startActivity(i);
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category Item");
        Getbarang();
    }

    private void generateBarang(ArrayList<Barang> Arraybarang, String a) {
        recyclerView = findViewById(R.id.recycler_view_barang);
        mAdapter = new BarangsAdapter(BarangActivity.this, Arraybarang,this);
        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));

        recyclerView.setAdapter(mAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onBarangSelected(Barang barang) {
        sesi = new SessionManager(getApplicationContext());
        sesi.Statusbarang("12");
        inputbarangdialog = new Inputbarang();
        inputbarangdialog.showinput(BarangActivity.this, "Input Item", BarangActivity.this, barang.getId());
       // Toast.makeText(getApplicationContext(), "Selected: " + barang.getItem_catagory() + ", " + barang.getItem_code() + "id " + barang.getId(), Toast.LENGTH_LONG).show();
    }

    public void Getbarang(){
        String usergrup;
        sesi = new SessionManager(getApplicationContext());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        usergrup = usersesion.get(SessionManager.KEY_USERGRUP);

        String token = usersesion.get(SessionManager.TOKEN);
        retrofit = new RetrofitInstance(token);
        Router service = retrofit.getRetrofitInstanceall().create(Router.class);

        Call<BarangArray> call = service.Getbarang(usergrup);
        call.enqueue(new Callback<BarangArray>() {
            @Override
            public void onResponse(Call<BarangArray> call, retrofit2.Response<BarangArray> response) {
                generateBarang(response.body().getBarangarray(), response.body().getRespons());
            }
            @Override
            public void onFailure(Call<BarangArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data Dari Server Pastikan Anda Terhubung Dengan Internet " , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
