package com.example.irhabi.retrobarbershop.newmasuklogin;

/**
 * Created BY Progrmmer Jalan on January 2018
 */

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.Antri.Antri;
import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.MapsActivity;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.barbermen.ControlStylish;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.HashMap;

public class MainL extends AppCompatActivity {
    Handler mHandler ;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager FM;
    FragmentTransaction FT;
    private ActionBar toolbar;
    private SessionManager session;
    private MenuItem scan;
    private SessionManager sesi;
    String usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainl);
        toolbar = getSupportActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.shitstuff);

        sesi = new SessionManager(getApplicationContext());
        final HashMap<String, String> usersesion = sesi.getUserDetails();
        usr = usersesion.get(SessionManager.KEY_USER);
        String admin = usr;
        if(admin.equals("admin")) {
            Menu scan_Menu = navigationView.getMenu();
            scan_Menu.findItem(R.id.scan).setVisible(true);
            scan_Menu.findItem(R.id.antri).setVisible(true);
        }else if(admin.equals("superadmin")){
            Menu scan_Menu = navigationView.getMenu();
            scan_Menu.findItem(R.id.scan).setVisible(true);
            scan_Menu.findItem(R.id.antri).setVisible(true);
            scan_Menu.findItem(R.id.style).setVisible(true);
        }else{
            Menu scan_Menu = navigationView.getMenu();
            scan_Menu.findItem(R.id.scan).setVisible(false);
        }
        FM = getSupportFragmentManager();
        FT = FM.beginTransaction();
        FT.replace(R.id.containerView, new TabFragment()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                if (item.getItemId() == R.id.set) {
                    Intent i = new Intent(MainL.this,Setting.class);
                    startActivity(i);
                   // FragmentTransaction fragmentTransaction = FM.beginTransaction();
                   // fragmentTransaction.replace(R.id.containerView, new Setting()).commit();
                }
                if (item.getItemId() == R.id.prof) {
                    Intent i = new Intent(getApplicationContext(), KonekMaps.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.scan) {
                    Intent gf = new Intent(MainL.this, ScanFragment.class);
                    startActivity(gf);
                }
                if (item.getItemId() == R.id.lokasi) {
                    Intent gf = new Intent(MainL.this, MapsActivity.class);
                    startActivity(gf);
                }
                if (item.getItemId() == R.id.antri) {
                    Intent i = new Intent(MainL.this,Antri.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.style) {
                    Intent i = new Intent(MainL.this,ControlStylish.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.keluar) {
                    signOut();
                }
                return false;
            }
        });

        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDUA);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    public void signOut() {
        session = new SessionManager(getApplicationContext());
        session.logoutUser();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
