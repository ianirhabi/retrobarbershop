package com.example.irhabi.retrobarbershop.newmasuklogin;

/**
 * Created BY Progrmmer Jalan on January 2018
 */

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Absen;

import java.util.ArrayList;

public class AbsenAdapter extends RecyclerView.Adapter<AbsenAdapter.AbsenViewHolder> {

    private ArrayList<Absen> dataList;

    public AbsenAdapter(ArrayList<Absen> dataList) {
        this.dataList = dataList;
    }

    @Override
    public AbsenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_absen, parent, false);
        return new AbsenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AbsenViewHolder holder, int position) {
        holder.txtTanggal.setText("Tanggal Pengambilan Absen : "+dataList.get(position).gettangal());
        holder.txtWaktu.setText("Jam Masuk : " + dataList.get(position).getwaktu());
        holder.txtHari.setText("Hari Masuk : " + dataList.get(position).getHari());
        holder.txtKehadiran.setText("Kehadiran : " + dataList.get(position).gethadir());
     //   holder.txtNotif.setText("Notif : " + dataList.get(position).getNotif());
    }

    @Override
    public int getItemCount() {
        if (dataList == null){
            return 0;
        }else
            return dataList.size();
    }

    class AbsenViewHolder extends RecyclerView.ViewHolder {

        TextView txtTanggal, txtWaktu, txtKehadiran, txtHari, txtNotif;

        AbsenViewHolder(View itemView) {
            super(itemView);
            txtHari = itemView.findViewById(R.id.hari);
            txtTanggal =  itemView.findViewById(R.id.tanggal_absen);
            txtWaktu =  itemView.findViewById(R.id.waktu);
            txtKehadiran =  itemView.findViewById(R.id.kehadiran);
            txtNotif = itemView.findViewById(R.id.notif);
        }
    }
}