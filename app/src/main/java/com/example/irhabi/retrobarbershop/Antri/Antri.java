package com.example.irhabi.retrobarbershop.Antri;

/**
 * Created by Programmer Jalanan 15/07/2018
 */

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;


import im.delight.android.webview.AdvancedWebView;

public class Antri extends Activity implements AdvancedWebView.Listener,SwipeRefreshLayout.OnRefreshListener{

    private AdvancedWebView mWebView;
    SwipeRefreshLayout layoutswipe;
    WebView webview ;
    EditText masukan;
    private boolean timerHasStarted = false;
    private CountDownTimer countDownTimer;
    private final long startTime = 3 * 1000;
    private final long interval = 1 * 1000;
    private AudioManager myAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antri);
        layoutswipe = (SwipeRefreshLayout)findViewById(R.id.swype);
        layoutswipe.setOnRefreshListener(this);
        masukan = (EditText)findViewById(R.id.masuk);
        String mas = masukan.getText().toString();
        onLoadweb("http://google.com");
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new MyCountDownTimer(startTime, interval);
                layoutswipe.setRefreshing(false);
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.antrian);
                mp.start();

                countDownTimer.start();
                timerHasStarted = true;


            }
        }, 1000);
    }
    public void onLoadweb(String url){
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(Antri.this, Antri.this);
        mWebView.loadUrl(url);
    }
    // jarsigner -verify -verbose -certs nama apk

    public boolean onKeyDown(int keyCode, KeyEvent event) {
       Intent i = new Intent(Antri.this, KonekMaps.class);
        startActivity(i);
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    myAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    myAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);           }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            try{
                webview = (WebView) findViewById(R.id.webview);
                masukan = (EditText) findViewById(R.id.masuk);
                String mas = masukan.getText().toString();
                String a = mas;
                String[] kf = a.split("\\s");
                String noantrian = kf[0];
                String tujuan = kf[1];
                String berapa = kf[2];
                onLoadweb("https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&q=nomor%20antrian%20" + noantrian + "%20di%20" + tujuan + "%20" + berapa + "&tl=id&total=1&idx=0&textlen=33");
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "tolong masukan huruf dengan benar ", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


}