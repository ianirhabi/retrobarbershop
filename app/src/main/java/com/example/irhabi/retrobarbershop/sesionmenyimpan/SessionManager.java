package com.example.irhabi.retrobarbershop.sesionmenyimpan;

/**
 * Created by irhabi on 10/31/2017.
 */


/**
 * Created by irhabi on 4/24/2017.
 */
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.irhabi.retrobarbershop.LoginActivity;
import com.example.irhabi.retrobarbershop.SplashScreen;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // nama sharedpreference
    private static final String PREF_NAME = "Sesi";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_USER = "nama";
    public static final String KEY_ID = "id";
    public  static  final  String KEY_USERGRUP = "grup";
    public static  final String LONGTITUDE = "long";
    public static  final String LATITUDE = "lat";
    public static  final String KEY_IMAGE = "IMAGE";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    // hanya menyimpan email
    public void createLoginSession(String user,String usrgrup, String id){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_USER, user);
        editor.putString(KEY_USERGRUP, usrgrup);
        editor.commit();
    }
    public void createLongtiLati(String la, String lo){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(LONGTITUDE, lo);
        editor.putString(LATITUDE, la);
        editor.commit();
    }
    // hanya menyimpan email
    public void createImage(String image){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_IMAGE, image);
        editor.commit();
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            //((Activity)_context).finish();
        }
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, pref.getString(KEY_ID,null));
        user.put(KEY_USER, pref.getString(KEY_USER, null));
        user.put(KEY_USERGRUP, pref.getString(KEY_USERGRUP, null));
        user.put(LONGTITUDE, pref.getString(LONGTITUDE, null));
        user.put(LATITUDE, pref.getString(LATITUDE, null));
        user.put(KEY_IMAGE,pref.getString(KEY_IMAGE,null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public void checkSession() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Katalog Activity
            Intent i = new Intent(_context, SplashScreen.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}