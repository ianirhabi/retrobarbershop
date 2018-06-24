package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irhabi.retrobarbershop.R;

public class Absen extends Fragment {
    public  Absen(){
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_absen,container,false);
        return view;
    }
}
