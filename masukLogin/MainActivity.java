package com.example.irhabi.retrobarbershop.masukLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.irhabi.retrobarbershop.MainActivityControlAkun;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.masukLogin.fragment.CartFragment;
import com.example.irhabi.retrobarbershop.masukLogin.fragment.GiftsFragment;
import com.example.irhabi.retrobarbershop.masukLogin.fragment.ProfileFragment;
import com.example.irhabi.retrobarbershop.masukLogin.fragment.StoreFragment;
import com.example.irhabi.retrobarbershop.masukLogin.helper.BottomNavigationBehavior;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    SessionManager session;
    String Email_simpansesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utama_buttomnavigasi);

        toolbar = getSupportActionBar();

        session = new SessionManager(getApplicationContext());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default

        HashMap<String, String> usersesion = session.getUserDetails();

        Email_simpansesion = usersesion.get(SessionManager.KEY_EMAIL);
        toolbar.setTitle(Email_simpansesion);
        loadFragment(new StoreFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    toolbar.setTitle("Aktivitas");
                    fragment = new StoreFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_gifts:
                    toolbar.setTitle("Nomor Antrian");
                    fragment = new GiftsFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_cart:
                    toolbar.setTitle("Cart");
                    fragment = new CartFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    Intent intent = new Intent(MainActivity.this, MainActivityControlAkun.class);
                    startActivity(intent);
                    return true;
            }

            return false;
        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //fungsi untuk ketika menekan tombol kembali langsung keluar

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Ketika disentuh tombol back pada Android

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            moveTaskToBack(true);
            return true;
        }

        // Jika tidak ada halaman yang pernah dibuka
        // Maka akan keluar dari activity (tutup aplikasi)

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cartList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(MainActivity.this, UserNomorAntrian.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
