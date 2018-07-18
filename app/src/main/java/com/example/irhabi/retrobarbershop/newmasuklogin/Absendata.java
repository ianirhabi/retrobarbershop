package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.model.Absenarray;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Absendata extends Fragment {

    private AbsenAdapter adapterabsen;
    private RecyclerView recyclerView;
    private SessionManager sesi;

    public  Absendata(){
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_absen,container,false);
        String id;
        sesi = new SessionManager(getActivity());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        id = usersesion.get(SessionManager.KEY_ID);
        int idDetail = Integer.parseInt(id);
        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        Call<Absenarray> call = service.absenbarberman(idDetail);
        call.enqueue(new Callback<Absenarray>() {
            @Override
            public void onResponse(Call<Absenarray> call, Response<Absenarray> response) {
                generateAbsen(response.body().getAbsenarray());
            }

            @Override
            public void onFailure(Call<Absenarray> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void generateAbsen(ArrayList<Absen> Arrayabsen) {
        recyclerView = getView().findViewById(R.id.recycler_view_notice_list);
        adapterabsen = new AbsenAdapter(Arrayabsen);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterabsen);
    }
}
