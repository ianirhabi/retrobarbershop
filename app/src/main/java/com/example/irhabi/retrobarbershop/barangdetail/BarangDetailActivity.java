package com.example.irhabi.retrobarbershop.barangdetail;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.alert.InputBarangDetail;
import com.example.irhabi.retrobarbershop.barang.BarangActivity;
import com.example.irhabi.retrobarbershop.model.BarangDetail;
import com.example.irhabi.retrobarbershop.model.BarangDetailArray;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.example.irhabi.retrobarbershop.utils.MyDividerItemDecoration;
import com.example.irhabi.retrobarbershop.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarangDetailActivity extends AppCompatActivity
        implements BarangDetailAdapter.BarangDetailAdapterListener {
    private ProgressBar progressBar;
    private SessionManager sesi;
    private RetrofitInstance retrofit;
    private RecyclerView recyclerView;
    private BarangDetailAdapter mAdapter;
    private SearchView searchView;
    private InputBarangDetail dl;
    private int idbarang;


    private int TOTAL_PAGES = 5;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private String kode_kategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barangdetailactivity);
        widget();
        GetBaraNgdetail();
    }

    public void widget(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbardetailbarang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        final String kode = b.getString("kode");
        final int idbarang = b.getInt("idbarang");
        this.kode_kategory = kode;
        this.idbarang = idbarang;
        getSupportActionBar().setTitle("kode category (" + kode + ")");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tambahbarangdetail);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesi = new SessionManager(getApplicationContext());
                sesi.Statusbarang("10");
                dl = new InputBarangDetail();
                dl.showinput(BarangDetailActivity.this,"Input Item",
                        BarangDetailActivity.this,idbarang,123,"",
                        "",1,2,"",0);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BarangDetailActivity.this, BarangActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void GetBaraNgdetail(){
        progressBar = (ProgressBar)findViewById(R.id.main_progress_detail_barang);

        //untuk mendapatkan usergrup
        String usergrup;
        sesi = new SessionManager(getApplicationContext());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        usergrup = usersesion.get(SessionManager.KEY_USERGRUP);

        String token = usersesion.get(SessionManager.TOKEN);
        retrofit = new RetrofitInstance(token);
        Router service = retrofit.getRetrofitInstanceall().create(Router.class);

        Call<BarangDetailArray> call = service.Get_Barang_Detail(usergrup,"100",idbarang);
        call.enqueue(new Callback<BarangDetailArray>() {
            @Override
            public void onResponse(Call<BarangDetailArray> call, Response<BarangDetailArray> response) {
                if(response.body().getDatadetail() == null ){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"belum ada data ", Toast.LENGTH_SHORT).show();
                }else{
                    generateBarangDetail(response.body().getDatadetail(),
                            response.body().getStatus(),response.body().getTotal());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BarangDetailArray> call, Throwable t) {

            }
        });
    }

    private void generateBarangDetail(ArrayList<BarangDetail> barangDetail,
                                      String respons, final String total) {
        recyclerView = findViewById(R.id.recycler_view_barangdetail);
        mAdapter = new BarangDetailAdapter(BarangDetailActivity.this, barangDetail,this);
        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNext(total);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });
    }

    private void loadNext(String total) {
        int hal = Integer.parseInt(total);
        int totalpage = hal + 25;
        String totalPage = String.valueOf(totalpage);

        progressBar = (ProgressBar)findViewById(R.id.main_progress_detail_barang);
        progressBar.setVisibility(View.VISIBLE);

        //usergrup
        String usergrup;
        sesi = new SessionManager(getApplicationContext());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        usergrup = usersesion.get(SessionManager.KEY_USERGRUP);

        String token = usersesion.get(SessionManager.TOKEN);
        retrofit = new RetrofitInstance(token);
        Router service = retrofit.getRetrofitInstanceall().create(Router.class);

        Call<BarangDetailArray> call = service.Get_Barang_Detail(usergrup,"100", idbarang);
        call.enqueue(new Callback<BarangDetailArray>() {
            @Override
            public void onResponse(Call<BarangDetailArray> call, Response<BarangDetailArray> response) {
                if(response.body().getDatadetail() == null ){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"belum ada data ", Toast.LENGTH_SHORT).show();
                }else{
                    generateBarangDetail(response.body().getDatadetail(),
                            response.body().getStatus(),response.body().getTotal());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BarangDetailArray> call, Throwable t) {

            }
        });
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
    public void onBarangdetailSelected(BarangDetail barangDetail) {
        Toast.makeText(getApplicationContext(),"INI ADALAH " +barangDetail.getDesc() , Toast.LENGTH_SHORT).show();
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
}
