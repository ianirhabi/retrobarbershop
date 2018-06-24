package com.example.irhabi.retrobarbershop.masukLogin.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.irhabi.retrobarbershop.AppConfig;
import com.example.irhabi.retrobarbershop.LoginActivity;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.SignupActivity;
import com.example.irhabi.retrobarbershop.masukLogin.UserNomorAntrian;
import com.example.irhabi.retrobarbershop.masukLogin.app.MyApplication;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class GiftsFragment extends Fragment {

    private Button ambilantrian ;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private TextView menampilkannourut, tampilmenyimpan;
    ArrayList<HashMap<String, String>> list_data;
    SessionManager session;
    String Email_simpansesion;
    ProgressDialog pd;
    ImageView gambaratas;

    public GiftsFragment() {
        // Required empty public constructor
    }

    public static GiftsFragment newInstance(String param1, String param2) {
        GiftsFragment fragment = new GiftsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gifts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Animation animTranslate = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_translate);
        final Animation animAlpha = AnimationUtils.loadAnimation(getActivity(), R.anim.annim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate);

        gambaratas = (ImageView) view.findViewById(R.id.gambarretro);
        session = new SessionManager(getActivity()); // untuk session di fragment harus pakai getActivity()

        String url = "http://bimsalabim.xyz/retrobarbershop/nomorantrian.php";

        menampilkannourut = (TextView) view.findViewById(R.id.antrian);
        tampilmenyimpan = (TextView) view.findViewById(R.id.menyimpantampil);
        ambilantrian = (Button) view.findViewById(R.id.ambilnomor);

        //session
        HashMap<String, String> usersesion = session.getUserDetails();
        Email_simpansesion = usersesion.get(SessionManager.KEY_EMAIL);
        requestQueue = Volley.newRequestQueue(getActivity());

        list_data = new ArrayList<HashMap<String, String>>();

        Animation animation2 = AnimationUtils.loadAnimation(getActivity(),R.anim.pindah_keatas);
        gambaratas.startAnimation(animation2);

        Animation animation3 = AnimationUtils.loadAnimation(getActivity(),R.anim.pindah_keatas);
        menampilkannourut.startAnimation(animation3);

        ambilantrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
                fungsi_ambilnomor(Email_simpansesion);
            }
        });

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("nomorurutan");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("idnya_retro", json.getString("idnya_retro"));
                        map.put("nomorurutan", json.getString("nomorurutan"));

                        //kalau ada  data yg ingin di ambil dari server lagi tiggal masukan kode yang sama
                        //sesuai data kolomnya
                        list_data.add(map);
                    }
                    menampilkannourut.setText(list_data.get(0).get("nomorurutan"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    //untuk mengambil nomor antrian
    public void fungsi_ambilnomor(final String name){

        String url_ambilnomor ="http://bimsalabim.xyz/retrobarbershop/tampilkanemail.php";

        String tag_json = "tag_json";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_ambilnomor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                try {
                    JSONObject jObject = new JSONObject(response);
                    String pesan = jObject.getString("pesan");
                    String hasil = jObject.getString("result");
                    if (hasil.equalsIgnoreCase("true")) {

                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                        //parsing variabel ke UserNomorAntrian klass
                        Intent i = null;
                        i = new Intent(getActivity(), UserNomorAntrian.class);
                        Bundle b = new Bundle();
                        b.putString("parsing_nourut", pesan);
                        i.putExtras(b);
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Anda Sudah Mengambil nomor urut", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("email", name); // sesuaaikan dengan nama $_POST

                return param;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json);

    }
}