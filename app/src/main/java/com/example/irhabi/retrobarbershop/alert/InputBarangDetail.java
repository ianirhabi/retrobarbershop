package com.example.irhabi.retrobarbershop.alert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.BarangDetail;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputBarangDetail {
    private RetrofitInstance retrofit;
    private SessionManager sesi;
    private BarangDetail goods;
    private String namabarang, kode,hj,hp, stock, status;
    private Context mContext;
    private int code_category;

    public void showinput(Activity activity, String msg, final Context mContext, final int idbarang,
                          final int category,
                          final String kode, String StockBarang,
                          int hp, int hj, String Namabarang, int userid) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_barang_detail);
        this.mContext=mContext;
        this.code_category =idbarang;

        final EditText namabarang = (EditText) dialog.findViewById(R.id.namabarang);
        final EditText code = (EditText) dialog.findViewById(R.id.kodebarang);
        final EditText HargaJual = (EditText) dialog.findViewById(R.id.HargaJual);
        final EditText HargaPokok = (EditText) dialog.findViewById(R.id.HargaPokok);
        final EditText Stock = (EditText) dialog.findViewById(R.id.stok);
        final RelativeLayout Batal = (RelativeLayout) dialog.findViewById(R.id.batalbarang);
        final RelativeLayout Submit = (RelativeLayout) dialog.findViewById(R.id.tambahbarang);
        final TextView statuscode = (TextView)dialog.findViewById(R.id.statuscode);

        sesi = new SessionManager(mContext);
        HashMap<String, String> statussesi = sesi.getUserDetails();
        status = statussesi.get(SessionManager.STATUS_BARANG);

        if(status.equals("10") || status.equals("11")) {
            Batal.setVisibility(View.VISIBLE);
            Submit.setVisibility(View.VISIBLE);
            namabarang.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
            HargaJual.setVisibility(View.VISIBLE);
            HargaPokok.setVisibility(View.VISIBLE);
            Stock.setVisibility(View.VISIBLE);

            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String jual= HargaJual.getText().toString();
                    String pokok = HargaPokok.getText().toString();
                    final String stock = Stock.getText().toString();
                    final String Namebarang = namabarang.getText().toString();
                    final  String kode = code.getText().toString();
                    if (Namebarang.isEmpty()){
                        namabarang.setError("tidak boleh kosong");
                        statuscode.setVisibility(view.VISIBLE);
                        statuscode.setText("Tekan tanda seru untuk melihat status errornya !");
                    }
                    if (kode.isEmpty()){
                        code.setError("tidak boleh kosong");
                        statuscode.setVisibility(view.VISIBLE);
                        statuscode.setText("Tekan tanda seru untuk melihat status errornya !");
                    }
                    if(jual.isEmpty()){
                        HargaJual.setError("tidak boleh kosong");
                        statuscode.setVisibility(view.VISIBLE);
                        statuscode.setText("Tekan tanda seru untuk melihat status errornya !");
                    }
                    if(pokok.isEmpty()){
                        HargaPokok.setError("tidak boleh kosong");
                        statuscode.setVisibility(view.VISIBLE);
                        statuscode.setText("Tekan tanda seru untuk melihat status errornya !");
                    }
                    if(stock.isEmpty()){
                        Stock.setError("tidak boleh kosong");
                        statuscode.setVisibility(view.VISIBLE);
                        statuscode.setText("Tekan tanda seru untuk melihat status errornya !");
                    }else {
                        try {
                            final int hj = Integer.parseInt(jual);
                            final int hp = Integer.parseInt(pokok);
                            kirimdata(Namebarang, kode, hj, hp, stock, dialog, code, statuscode);
                        }catch (Exception e){
                            HargaPokok.setError("panjang karakter melebihi jumlah maximum");
                            HargaJual.setError("panjang karakter melebihi jumlah maximum");
                        }
                    }

                }
            });

            Batal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private void kirimdata(String namebarang, String kode,
                           int hj, int hp, String stock,
                           final Dialog dialog, final EditText st, final TextView ts) {
        String usergrup,  token;
        sesi = new SessionManager(mContext);
        HashMap<String, String> usersesi = sesi.getUserDetails();
        usergrup = usersesi.get(SessionManager.KEY_USERGRUP);
        int id = Integer.parseInt(usersesi.get(SessionManager.KEY_ID));
        token = usersesi.get(SessionManager.TOKEN);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Log.e("code_category","hasil === "+ code_category);
        goods = new BarangDetail(kode,stock,hp,hj,
                code_category,namebarang,dateFormat.format(date),"", id);
        retrofit = new RetrofitInstance(token);

        Router service = retrofit.getRetrofitInstanceall().create(Router.class);
        Call<BarangDetail> call =service.PostBarangDetail(goods, usergrup);

        call.enqueue(new Callback<BarangDetail>() {
            @Override
            public void onResponse(Call<BarangDetail> call, Response<BarangDetail> response) {
                Log.e("debug e", "=== " + response.body().getStatus());
                if(response.body().getStatus().equals("sukses")){
                    Toast.makeText(mContext,"berhasil Memasukan Data",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else if (response.body().getStatus().equals("false")){
                    st.setError("kode sudah ada !! silahkan masukan kode yang lain");
                    ts.setVisibility(View.VISIBLE);
                    ts.setText("Tekan tanda seru untuk melihat status errornya !");
                }else if(response.body().getStatus().equals("gagal")){
                    Toast.makeText(mContext,"harus membuat data category terlebih dahulu",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BarangDetail> call, Throwable t) {
                Toast.makeText(mContext,"upssS...! error connect ke server",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
