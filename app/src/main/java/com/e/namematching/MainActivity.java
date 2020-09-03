package com.e.namematching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BottomNavigationView navigationView;
    private AdView mAdView;
    private FirebaseDatabase mFirebaseDatase;
    private DatabaseReference mChatRef;

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

        mFirebaseDatase = FirebaseDatabase.getInstance();
        mChatRef = mFirebaseDatase.getReference("users");
        mChatRef.setValue(1);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer,new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
        init();
    }

    private void init(){
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