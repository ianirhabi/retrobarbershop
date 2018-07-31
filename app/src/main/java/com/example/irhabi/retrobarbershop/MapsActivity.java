package com.example.irhabi.retrobarbershop;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SessionManager session ;
    private String usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> usersesion = session.getUserDetails();

        usr = usersesion.get(SessionManager.KEY_USERGRUP);
        Log.d("debug ==== usr ", "saya " + usr);
        if (usr.equals("1")){
            try {
                Bundle b = getIntent().getExtras();
                String get_lat = b.getString("parse_lat");
                String get_lon = b.getString("parse_lon");
                String la = usersesion.get(SessionManager.LATITUDEstylish);
                String lo = usersesion.get(SessionManager.LONGTITUDEstylish);
                Double latude = Double.parseDouble(get_lat);
                Double lotude = Double.parseDouble(get_lon);
                LatLng sydney = new LatLng(latude, lotude);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Latitude : " + latude + " longtitude : " + lotude));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"tidak ada data ",Toast.LENGTH_LONG ).show();
            }
        }else{
            String la =  usersesion.get(SessionManager.LATITUDE);
            String lo = usersesion.get(SessionManager.LONGTITUDE);

            Double latude = Double.parseDouble(la);
            Double lotude = Double.parseDouble(lo);

            LatLng sydney = new LatLng(latude, lotude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Latitude : " + latude + " longtitude : "+ lotude ));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent i = new Intent(getApplicationContext(), KonekMaps.class);
        startActivity(i);
        return true;
    }
}
