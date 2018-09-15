package com.example.irhabi.retrobarbershop.barangdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.BarangDetail;

import java.util.ArrayList;
import java.util.List;


public class BarangDetailAdapter extends RecyclerView.Adapter<BarangDetailAdapter.MyViewHolder>
        implements Filterable {

    private List<BarangDetail>dataList;
    private Context mContext ;
    private BarangDetailAdapterListener listener;
    private List<BarangDetail> barangdetailListFiltered;
    private boolean isLoadingAdded = false;

    public BarangDetailAdapter(Context context, List<BarangDetail> dataList, BarangDetailAdapterListener listener) {
        this.dataList = dataList;
        this.mContext = context;
        this.listener = listener;
        this.barangdetailListFiltered = dataList;
    }

    @NonNull
    @Override
    public BarangDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_barang_detail, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        //disini kode untuk recycle view nya
        final BarangDetail data = barangdetailListFiltered.get(i);
        data.getId();
        holder.namabarang.setText("Nama Barang : " + data.getDesc());
        holder.kodenya.setText("Kode Barang : " + data.getKode());
        holder.hargaajual.setText("Harga Jual : " + data.getHj());
        holder.hargapokok.setText("Harga Pokok : " + data.getHp());
        holder.stock.setText("Stock : " + data.getStock());
    }

    @Override
    public int getItemCount() {
        if(barangdetailListFiltered == null){
            return 0;
        }else{
            return barangdetailListFiltered.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    barangdetailListFiltered = dataList;
                } else {
                    List<BarangDetail> filteredList = new ArrayList<>();
                    for (BarangDetail row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getKode().toLowerCase().contains(charString.toLowerCase()) || row.getDesc().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    barangdetailListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = barangdetailListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                barangdetailListFiltered = (ArrayList<BarangDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namabarang, kodenya, hargapokok, hargaajual, stock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namabarang = itemView.findViewById(R.id.namabarang);
            kodenya = itemView.findViewById(R.id.kode);
            hargaajual = itemView.findViewById(R.id.pricejual);
            hargapokok = itemView.findViewById(R.id.pricepokok);
            stock = itemView.findViewById(R.id.stok);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onBarangdetailSelected(barangdetailListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface BarangDetailAdapterListener{
        void onBarangdetailSelected(BarangDetail barangDetail);
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        notifyItemInserted(dataList.size() - 1);
    }
}
