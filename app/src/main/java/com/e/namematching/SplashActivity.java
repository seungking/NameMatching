package com.e.namematching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Lottie Animation
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.splash_logo);
        animationView.setAnimation("loading.json");
        animationView.loop(true);
        //Lottie Animation start
        animationView.playAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                finish();
                isFirstTime();
            }
        },1400);
    }

    private void isFirstTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getString("name","").length()!=0){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(SplashActivity.this,SetAccount.class));
            finish();
        }
    }

}