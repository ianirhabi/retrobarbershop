package com.example.irhabi.retrobarbershop.barang;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Barang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Programmer Jalanan on 16/11/17.
 */

public class BarangsAdapter extends RecyclerView.Adapter<BarangsAdapter.MyViewHolder>
        implements Filterable {

    private Context mContext;
    private List<Barang> dataList;
    private List<Barang> barangListFiltered;
    private BarangAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, Code, created;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            Code = view.findViewById(R.id.code);
            created = view.findViewById(R.id.created);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onBarangSelected(barangListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public BarangsAdapter(Context context, List<Barang> dataList, BarangAdapterListener listener) {
        this.mContext = context;
        this.dataList = dataList;
        this.listener = listener;
        this.barangListFiltered = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.barang_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Barang data = barangListFiltered.get(position);
        data.getId();
        holder.name.setText(data.getItem_catagory());
        holder.Code.setText(data.getItem_code());
        holder.created.setText(data.getCreated());
//        Glide.with(context)
//                .load(contact.getImage())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        //kalau data tidak ada di database tidak akan null pointer
        if (barangListFiltered == null){
            return 0;
        }else {
            return barangListFiltered.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    barangListFiltered = dataList;
                } else {
                    List<Barang> filteredList = new ArrayList<>();
                    for (Barang row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getItem_catagory().toLowerCase().contains(charString.toLowerCase()) || row.getItem_code().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    barangListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = barangListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                barangListFiltered = (ArrayList<Barang>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface BarangAdapterListener {
        void onBarangSelected(Barang barang);
    }
}