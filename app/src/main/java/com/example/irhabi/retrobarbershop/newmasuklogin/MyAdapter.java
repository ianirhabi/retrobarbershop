package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import static com.example.irhabi.retrobarbershop.newmasuklogin.TabFragment.int_items;

public class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Profil();
            case 1:
                return new Absendata();
        }
        return null;
    }

    @Override
    public int getCount() {


        return int_items;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "MyProfil";
            case 1:
                return "Absen";
        }

        return null;
    }
}
