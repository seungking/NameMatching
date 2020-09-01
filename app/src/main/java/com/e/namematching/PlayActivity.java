package com.e.namematching;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cunoraz.continuouscrollable.ContinuousScrollableImageView;

import butterknife.BindView;
import info.hoang8f.widget.FButton;

public class PlayActivity extends AppCompatActivity {

    private ContinuousScrollableImageView window1;
    private ContinuousScrollableImageView window2;
    private FButton show;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        init();
    }

    public void init(){

        window1 = (ContinuousScrollableImageView)findViewById(R.id.play_window1);
        window2 = (ContinuousScrollableImageView)findViewById(R.id.play_window2);
        show = (FButton)findViewById(R.id.play_show);
        back = (ImageView)findViewById(R.id.play_back);

        show.setOnClickListener(v->{
            window1.setVisibility(View.VISIBLE);
            window2.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    window1.setVisibility(View.INVISIBLE);
                    window2.setVisibility(View.INVISIBLE);
                }
            },100);
        });

        back.setOnClickListener(v->{
            finish();
        });
    }
}