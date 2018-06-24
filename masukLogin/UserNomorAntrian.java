package com.example.irhabi.retrobarbershop.masukLogin;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.masukLogin.app.MyApplication;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by irhabi on 25/01/18.
 */


public class UserNomorAntrian extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private TextView menampilkannourut, tampilmenyimpan;
    ArrayList<HashMap<String, String>> list_data;
    SessionManager session;
    String useremail, nourut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usernomorantrian);
        String tag_json = "tag_json";

        menampilkannourut = (TextView) findViewById(R.id.urutuser);

        session = new SessionManager(getApplicationContext());
        //session
        HashMap<String, String> usersesion = session.getUserDetails();
        useremail = usersesion.get(SessionManager.KEY_EMAIL);

        Bundle b =getIntent().getExtras();
        nourut = b.getString("parsing_nourut");

        menampilkannourut.setText(nourut);
    }
}