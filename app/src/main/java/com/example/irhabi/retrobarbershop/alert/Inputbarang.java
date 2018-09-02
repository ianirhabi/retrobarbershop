package com.example.irhabi.retrobarbershop.alert;

/**
 * Created By Programmer Jalanan on 06/08/2018
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.barang.BarangActivity;
import com.example.irhabi.retrobarbershop.model.Barang;
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


public class Inputbarang {

    MediaMetadata musicMetadata;
    private RetrofitInstance retrofit;
    private SessionManager sesi;
    private Barang goods;
    private BarangActivity getbarang;

    public void showinput(Activity activity, String msg, final Context mContext, final int idbarang){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_inputbarang);

        final EditText kategory = (EditText) dialog.findViewById(R.id.kategory);
        final EditText code = (EditText) dialog.findViewById(R.id.kode);
        final RelativeLayout Batal = (RelativeLayout) dialog.findViewById(R.id.batal);
        final RelativeLayout Submit = (RelativeLayout) dialog.findViewById(R.id.submit);

        final RelativeLayout edit = (RelativeLayout) dialog.findViewById(R.id.Edit);
        final RelativeLayout hapus = (RelativeLayout) dialog.findViewById(R.id.delete);
        final RelativeLayout lihat = (RelativeLayout) dialog.findViewById(R.id.lihatdetail);
        final RelativeLayout batal2 = (RelativeLayout) dialog.findViewById(R.id.batal2);

        final String status ;
        sesi = new SessionManager(mContext);
        HashMap<String, String> statussesi = sesi.getUserDetails();
        status = statussesi.get(SessionManager.STATUS_BARANG);


        if(status.equals("10") || status.equals("11")) {

            Batal.setVisibility(View.VISIBLE);
            Submit.setVisibility(View.VISIBLE);
            kategory.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String namecategory = kategory.getText().toString();
                    final String codename = code.getText().toString();
                    kirimdata(mContext, namecategory, codename);
                    dialog.dismiss();
                }
            });

            Batal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        } else if(status.equals("12")){

            edit.setVisibility(View.VISIBLE);
            hapus.setVisibility(View.VISIBLE);
            lihat.setVisibility(View.VISIBLE);
            batal2.setVisibility(View.VISIBLE);

            batal2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hapuscategory(mContext, idbarang);
                    dialog.dismiss();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    sesi = new SessionManager(mContext);
                    sesi.Statusbarang("11");
                    edit.setVisibility(View.GONE);
                    hapus.setVisibility(View.GONE);
                    lihat.setVisibility(View.GONE);
                    batal2.setVisibility(View.GONE);
                    Batal.setVisibility(View.VISIBLE);
                    Submit.setVisibility(View.VISIBLE);
                    kategory.setVisibility(View.VISIBLE);
                    code.setVisibility(View.VISIBLE);

                    Submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String namecategoryupdate = kategory.getText().toString();
                            final String codenameupdate = code.getText().toString();
                            update(mContext, namecategoryupdate, codenameupdate, idbarang);
                            dialog.dismiss();
                        }
                    });

                    Batal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
        dialog.show();
    }

    public void hapuscategory(final Context mContext, int idbarang){

        String usergrup,  token;
        sesi = new SessionManager(mContext);
        HashMap<String, String> usersesi = sesi.getUserDetails();
        usergrup = usersesi.get(SessionManager.KEY_USERGRUP);
        token = usersesi.get(SessionManager.TOKEN);
        retrofit = new RetrofitInstance(token);
        Router service = retrofit.getRetrofitInstanceall().create(Router.class);
        Call<Barang> call =service.Deletebarang(usergrup, idbarang);
        getbarang = new BarangActivity();

        call.enqueue(new Callback<Barang>() {

            @Override
            public void onResponse(Call<Barang> call, Response<Barang> response) {
                Toast.makeText(mContext,"status " + response.body().getStatus(), Toast.LENGTH_LONG);
                Intent i = new Intent(mContext, BarangActivity.class);
                mContext.startActivity(i);
            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                Toast.makeText(mContext,"gagal memasukan data", Toast.LENGTH_LONG);
            }
        });
    }

    public void kirimdata(final Context mContext, String namecategory, String Code){

          String usergrup, id, token;
          sesi = new SessionManager(mContext);
          HashMap<String, String> usersesi = sesi.getUserDetails();
          usergrup = usersesi.get(SessionManager.KEY_USERGRUP);
          id = usersesi.get(SessionManager.KEY_ID);
          Integer user_id = Integer.parseInt(id);
          token = usersesi.get(SessionManager.TOKEN);
          DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          Date date = new Date();
          goods = new Barang(namecategory, Code, dateFormat.format(date),user_id);
          retrofit = new RetrofitInstance(token);

          Router service = retrofit.getRetrofitInstanceall().create(Router.class);
          Call<Barang> call =service.postbarang(goods, usergrup, id);
          getbarang = new BarangActivity();

          call.enqueue(new Callback<Barang>() {

              @Override
              public void onResponse(Call<Barang> call, Response<Barang> response) {
                Toast.makeText(mContext,"status " + response.body().getStatus(), Toast.LENGTH_LONG);
                Intent i = new Intent(mContext, BarangActivity.class);
                mContext.startActivity(i);
              }

              @Override
              public void onFailure(Call<Barang> call, Throwable t) {
                Toast.makeText(mContext,"gagal memasukan data", Toast.LENGTH_LONG);
              }
          });
    }

    public void update(final Context mContext, String namecategory, String Code, int idbarang){

        String usergrup, id, token;
        sesi = new SessionManager(mContext);
        HashMap<String, String> usersesi = sesi.getUserDetails();
        usergrup = usersesi.get(SessionManager.KEY_USERGRUP);
        id = usersesi.get(SessionManager.KEY_ID);
        Integer user_id = Integer.parseInt(id);
        token = usersesi.get(SessionManager.TOKEN);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        goods = new Barang(namecategory, Code, dateFormat.format(date),user_id);

        retrofit = new RetrofitInstance(token);
        Router service = retrofit.getRetrofitInstanceall().create(Router.class);
        Call<Barang> call =service.Updatebarang(goods, usergrup, id, idbarang);
        getbarang = new BarangActivity();

        call.enqueue(new Callback<Barang>() {

            @Override
            public void onResponse(Call<Barang> call, Response<Barang> response) {
                Toast.makeText(mContext,"status " + response.body().getStatus(), Toast.LENGTH_LONG);
                Intent i = new Intent(mContext, BarangActivity.class);
                mContext.startActivity(i);
            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                Toast.makeText(mContext,"gagal update data", Toast.LENGTH_LONG);

            }
        });
    }
}
