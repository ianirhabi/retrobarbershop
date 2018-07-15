package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

public class MainL extends AppCompatActivity {
    Handler mHandler ;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager FM;
    FragmentTransaction FT;
    private ActionBar toolbar;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainl);
        toolbar = getSupportActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.shitstuff);

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
                    FragmentTransaction fragmentTransaction1 = FM.beginTransaction();
                    fragmentTransaction1.replace(R.id.containerView, new TabFragment()).commit();
                }
                if (item.getItemId() == R.id.scan) {
                    Intent gf = new Intent(MainL.this, ScanFragment.class);
                    startActivity(gf);
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
