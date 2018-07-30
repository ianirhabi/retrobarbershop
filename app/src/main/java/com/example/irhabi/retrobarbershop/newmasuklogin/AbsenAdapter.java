package com.example.irhabi.retrobarbershop.newmasuklogin;

/**
 * Created by Programmer Jalanan on January 2018
 */

import android.content.Context;
import android.graphics.Color;
import android.se.omapi.Session;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

public class AbsenAdapter extends RecyclerView.Adapter<AbsenAdapter.AbsenViewHolder> {

    private Context mContext;
    private ArrayList<Absen> dataList;
    private SessionManager session;
    public AbsenAdapter(ArrayList<Absen> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public AbsenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_absen, parent, false);
        return new AbsenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AbsenViewHolder holder, int position) {

        session = new SessionManager(mContext);
        final HashMap<String, String> usersesion = session.getUserDetails();
        String usr = usersesion.get(SessionManager.KEY_USER);
        holder.txtTanggal.setText("Tanggal Pengambilan Absen : "+dataList.get(position).gettangal());

        if(usr.equals("superadmin")) {
            holder.txtWaktu.setText("Jam Masuk : " + dataList.get(position).getwaktu());
        }
        holder.txtHari.setText("Hari Masuk : " + dataList.get(position).getHari());
        holder.txtstatus.setText("Status Kehadiran : " );
        if(dataList.get(position).gethadir().equals("hadir")) {
            holder.txtKehadiran.setTextColor(Color.rgb(0, 255, 255));
            // setBackgroundColor(Color.rgb(126, 190, 80));
            holder.txtKehadiran.setText(dataList.get(position).gethadir());
        }else if(dataList.get(position).gethadir().equals("izin")){
            holder.txtKehadiran.setTextColor(Color.rgb(255, 0, 0));
            holder.txtKehadiran.setText(dataList.get(position).gethadir());
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null){
            return 0;
        }else
            return dataList.size();
    }

    class AbsenViewHolder extends RecyclerView.ViewHolder {

        TextView txtTanggal, txtWaktu, txtKehadiran, txtHari, txtNotif, txtstatus;

        AbsenViewHolder(View itemView) {
            super(itemView);
            txtHari = itemView.findViewById(R.id.hari);
            txtTanggal =  itemView.findViewById(R.id.tanggal_absen);
            txtWaktu =  itemView.findViewById(R.id.waktu);
            txtKehadiran =  itemView.findViewById(R.id.kehadiran);
            txtNotif = itemView.findViewById(R.id.notif);
            txtstatus = itemView.findViewById(R.id.status);
        }
    }

}