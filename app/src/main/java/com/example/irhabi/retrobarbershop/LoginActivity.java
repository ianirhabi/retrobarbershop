package com.example.irhabi.retrobarbershop;

/**
*Created by Programmer Jalanan on 27/07/2017
*/

import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.model.User;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.rest.Router;

import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.irhabi.retrobarbershop.rest.AppConfig.URL;

public class LoginActivity extends AppCompatActivity implements ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ImageView logingambar;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private String kau;
    private TextView IMEI;
    TelephonyManager telephonyManager;
    SessionManager session;
    private LoadingButton button;
    private boolean loading = false;
    private java.util.ArrayList<com.example.irhabi.retrobarbershop.model.Usr> data;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // set the view now
        setContentView(R.layout.activity_login);

        //menyimpan sesi dari inputan
        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logingambar = (ImageView)findViewById(R.id.imagelogin);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        button = (LoadingButton) findViewById(R.id.loading_button);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        checkLocationPermission();



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.annim_alpha);
        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        logingambar.startAnimation(rotate);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                button.startLoading();
                loading = true;
                if (TextUtils.isEmpty(email)) {
                    if (loading) {
                        button.stopLoading();
                        loading = false;
                    }
                    inputEmail.setError(getString(R.string.masuka_em));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    if (loading) {
                        button.stopLoading();
                        loading = false;
                    }
                    inputEmail.setError(getString(R.string.masukan_pass));
                    return;
                }
                if (password.length() < 6) {
                   if (loading) {
                          button.stopLoading();
                          loading = false;
                    }
                    inputPassword.setError(getString(R.string.minimum_password));
                 }
                    User user = new User(email,password);
                    Sending(user, email);
                 }
        });
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {
                    // Izin ditolak.
                    Toast.makeText(this, "tidak bisa melanjutkan karena anda tidak memberikan akses aplikasi ini", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Sending(User user, final String b){
        retrofit2.Retrofit.Builder builder = new retrofit2.Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        Router client  = retrofit.create(Router.class);
        Call<User> call = client.Login(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response ) {
                try {
                   if(response.body().getStatus().equals("sukses")){
                        Integer id = new Integer(response.body().getResponsdata().getUsr().Getid());
                        String di = id.toString();
                        session.createLoginSession(response.body().getResponsdata().getUsr().getUser(),
                                response.body().getResponsdata().getUsr().getUsergrup(), di);
                        Intent v = new Intent(LoginActivity.this, KonekMaps.class);
                        startActivity(v);
                        if (loading) {
                          button.stopLoading();
                          loading = false;
                        }
                    } else{
                        Toast.makeText(LoginActivity.this, "Tidak Berhasil Login Status " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        if (loading) {
                          button.stopLoading();
                          loading = false;
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Server Masih Dalam Perbaikan", Toast.LENGTH_SHORT).show();
                    if (loading) {
                          button.stopLoading();
                          loading = false;
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "error",Toast.LENGTH_SHORT).show();
                if (loading) {
                          button.stopLoading();
                          loading = false;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}

