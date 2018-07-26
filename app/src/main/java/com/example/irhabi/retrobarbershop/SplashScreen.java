package com.example.irhabi.retrobarbershop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

public class SplashScreen extends Activity {
	private ImageView image;
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 8000;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		image = (ImageView)findViewById(R.id.imgLogo);
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
		image.startAnimation(animation);
		new Handler().postDelayed(new Runnable() {
			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */
			@Override
			public void run() {

				Intent i = new Intent(SplashScreen.this, WelcomeActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
