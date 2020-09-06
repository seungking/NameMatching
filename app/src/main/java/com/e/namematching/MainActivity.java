package com.e.namematching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.e.namematching.fragment.AccountFragment;
import com.e.namematching.fragment.HomeFragment;
import com.e.namematching.fragment.RankFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    //layout
    private FragmentManager fragmentManager;
    private BottomNavigationView navigationView;

    //admob
    private AdView mAdView;

    //database
    private FirebaseDatabase mFirebaseDatase;
    private DatabaseReference mUser;
    private FirebaseAnalytics mFirebaseAnalytics;

    //Sound
    static SoundPool soundPool;	//작성
    static int soundID;		//작성

    int maxscore=0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //배너광고
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer,new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        mFirebaseDatase = FirebaseDatabase.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mUser = mFirebaseDatase.getReference("list").child("aaa");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                editor.putInt("score", dataSnapshot.getValue(Integer.class));
                editor.commit();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        mUser.child("name").addValueEventListener(postListener);


        Log.d("log1","main resume music start");
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);	//작성
        soundID = soundPool.load(this,R.raw.bgm_ha0,1);	//작성, (mp3 파일 이름이 click_sound이다.)
//        soundPool.play(soundID,1,1,0,1,1);	//작성

        int waitLimit = 1000;
        int waitCounter = 0;
        int throttle = 10;

        while(soundPool.play(soundID,1,1,0,-1,1) == 0 && waitCounter < waitLimit){
            waitCounter++; SystemClock.sleep(throttle);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.stop(soundID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.stop(soundID);
    }

    static void stopBgm(){
        soundPool.stop(soundID);
    }

    private void init(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                Fragment rank = fragmentManager.findFragmentByTag(RankFragment.class.getSimpleName());
                Fragment home = fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());

                switch (item.getItemId()){
                    case R.id.item_home: {
                        if(account!=null)fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                        if(rank!=null)fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(RankFragment.class.getSimpleName())).commit();

                        if (account!=null | rank!=null)
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();

                        break;
                    }

                    case R.id.item_rank: {
                        if(home!=null)fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                        if(account!=null)fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();

                        if (rank!=null)
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(RankFragment.class.getSimpleName())).commit();
                        else
                            fragmentManager.beginTransaction().add(R.id.frameHomeContainer,new RankFragment(),RankFragment.class.getSimpleName()).commit();

                        break;
                    }

                    case R.id.item_account: {
                        if(home!=null)fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                        if(rank!=null)fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(RankFragment.class.getSimpleName())).commit();

                        if (account!=null)
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                        else
                            fragmentManager.beginTransaction().add(R.id.frameHomeContainer,new AccountFragment(),AccountFragment.class.getSimpleName()).commit();

                        break;
                    }
                }

                return  true;
            }
        });
    }
}