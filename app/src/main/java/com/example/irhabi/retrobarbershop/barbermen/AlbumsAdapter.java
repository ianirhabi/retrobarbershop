package com.example.irhabi.retrobarbershop.barbermen;

/**
 * Created by Programmer Jalanan 26/07/2018 at cengkareng jakarta barat
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.irhabi.retrobarbershop.MapsActivity;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Usr;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;


import java.util.ArrayList;

import static com.example.irhabi.retrobarbershop.rest.AppConfig.URL;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Usr> dataList;
    public SessionManager session;

    public AlbumsAdapter(ArrayList<Usr> alluser, Context mContext) {
        this.dataList = alluser;
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Namastylish, status;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            Namastylish = (TextView) view.findViewById(R.id.title);
            status = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.Namastylish.setText(dataList.get(position).getname());
        holder.status.setText(dataList.get(position).getUser());
        // loading album cover using Glide library
        String a = dataList.get(position).getnamefoto();
        final int b = dataList.get(position).Getid();
        final String lat = dataList.get(position).getLat();
        final String lon = dataList.get(position).getLongt();

        String[] kf = a.split("\\.");
        final String first = kf[0];
        Log.d("DEBUG ", "NAMA FOTO USER ADAPTER " + first);
        Glide.with(mContext).load(URL+"upload/"+ first).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, b ,lat, lon);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList == null){
            return 0;
        }else
            return dataList.size();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int b , String lat, String lon) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(b, lat, lon));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private String  lat, lon ;
        private int b;

        public MyMenuItemClickListener(int b, String lat, String lon) {
            this.b = b;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_lihat_absen:
                    try {
                        session = new SessionManager(mContext);
                        String id = String.valueOf(b);
                        session.createidStylish(id);
                        Log.d("disini ======== ", " " + b);
                        Toast.makeText(mContext, "id usernya " + b, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext, LaporanAbsenStylish.class);
                        Bundle ambil_id = new Bundle();
                        ambil_id.putInt("parse_id", b);
                        //b.putString("parse_alamat", var_alamat);
                        //b.ambilid.putString("parse_tgllahir", var_tgllahir);
                        i.putExtras(ambil_id);
                        mContext.startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(mContext, "Ada Masalah dengan Server", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.action_play_next:
                    try {
                        session = new SessionManager(mContext);
                        session.createLongtiLatiStylish(lat,lon);
                        Toast.makeText(mContext, "Lokasi Stylish " + lat + " " + lon, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext, MapsActivity.class);
                        mContext.startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(mContext, "Ada Masalah dengan Server", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                default:
            }
            return false;
        }

    }

}
