package com.e.namematching;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cunoraz.continuouscrollable.ContinuousScrollableImageView;
import com.e.namematching.model.GameData;
import com.e.namematching.model.Set;
import com.e.namematching.model.Solution;
import com.example.animationdialog.AnimationDialog;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import stream.custombutton.CustomButton;

public class PlayActivity extends AppCompatActivity {

    ContinuousScrollableImageView window1;
    ContinuousScrollableImageView window2;
    CustomButton show;
    CustomButton option1;
    CustomButton option2;
    CustomButton option3;
    CustomButton option4;
    ImageView back;
    ImageView hp1;
    ImageView hp2;
    ImageView hp3;
    ImageView telescope_plus;
    ImageView sound;
    int soundcheck=1;
    TextView stage;
    TextView score;
    TextView show_count;
//    @Nullable
//    @BindView(R.id.play_window1)
//    ContinuousScrollableImageView window1;
//
//    @Nullable
//    @BindView(R.id.play_window2)
//    ContinuousScrollableImageView window2;

//    @Nullable
//    @BindView(R.id.play_show)
//    Button show;
//
//    @Nullable
//    @BindView(R.id.option1)
//    Button option1;
//
//    @Nullable
//    @BindView(R.id.option2)
//    Button option2;
//
//    @Nullable
//    @BindView(R.id.option3)
//    Button option3;
//
//    @Nullable
//    @BindView(R.id.option4)
//    Button option4;
//
//    @Nullable
//    @BindView(R.id.play_back)
//    ImageView back;
//
//    @Nullable
//    @BindView(R.id.play_stage)
//    TextView stage;
//
//    @Nullable
//    @BindView(R.id.play_score)
//    TextView score;

    private ArrayList<Integer> imglist = new ArrayList<>();
    private ArrayList<Set> setlist = new ArrayList<>();
    private int idx=-1;
    private GameData gameData;
    private Solution solution;

    Button[] dialog_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_play);

        initdata();
        init();
    }

    public void initdata(){

        gameData = new GameData();
        solution = new Solution();

        for (int i=1; i < 49; i++) {
            imglist.add(getResources().getIdentifier("@drawable/img_" + i, "drawable", this.getPackageName()));
            setlist.add(solution.getSolution()[i-1]);
        }

    }

    public void init(){

        window1 = findViewById(R.id.play_window1);
        window2 = findViewById(R.id.play_window2);
        show = findViewById(R.id.play_show);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        back = findViewById(R.id.play_back);
        hp1 = findViewById(R.id.hp1);
        hp2 = findViewById(R.id.hp2);
        hp3 = findViewById(R.id.hp3);
        sound = findViewById(R.id.play_sound);
        telescope_plus = findViewById(R.id.play_telescope_plus);
        stage = findViewById(R.id.play_stage);
        score = findViewById(R.id.play_score);
        show_count = findViewById(R.id.play_show_change_count);

        dialog_buttons= mAnimationDialog.init(this,false);
        dialog_buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnimationDialog.close();
            }
        });

        setnext();

        sound.setOnClickListener(v->{
            if(soundcheck==1) sound.setImageResource(R.drawable.soundoff);
            else if(soundcheck==0)sound.setImageResource(R.drawable.soundon);
            soundcheck = (soundcheck==1)? 0:1;
        });

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
            },20);
            gameData.setShow(gameData.getShow()-1);
            show_count.setText(String.valueOf(gameData.getShow()));
            if(gameData.getShow()<=0) {
                show.setClickable(false);
            }
        });

        telescope_plus.setOnClickListener(v->{
            Button[] buttons;
            buttons=AnimationDialog.init(this,false);
            AnimationDialog.create("3회 보기를 추가하시겠습니까?","남은 기회 : 1번","NotOk!");
            AnimationDialog.add_secend_button("OK!");
            buttons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnimationDialog.close();
                }
            });
            buttons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telescope_plus.setImageResource(R.drawable.telescope_plus_used);
                    telescope_plus.setClickable(false);
                    gameData.setChance(gameData.getChance()-1);
                    gameData.setShow(3 + gameData.getShow());
                    show.setClickable(true);
                    show_count.setText(String.valueOf(gameData.getShow()));
                    AnimationDialog.close();
                }
            });
            AnimationDialog.show();
        });

        back.setOnClickListener(v->{
            exitnotification();
        });

        option1.setOnClickListener(v->{
            if(solution.getSolution()[idx].getAnswer()==1) {
                gameData.setScore(gameData.getScore()+1);
                correct(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
            else{
                gameData.setHp(gameData.getHp()-1);
                wrong(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
        });
        option2.setOnClickListener(v->{
            if(solution.getSolution()[idx].getAnswer()==2) {
                gameData.setScore(gameData.getScore()+1);
                correct(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
            else{
                gameData.setHp(gameData.getHp()-1);
                wrong(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
        });
        option3.setOnClickListener(v->{
            if(solution.getSolution()[idx].getAnswer()==3) {
                gameData.setScore(gameData.getScore()+1);
                correct(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
            else{
                gameData.setHp(gameData.getHp()-1);
                wrong(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
        });
        option4.setOnClickListener(v->{
            if(solution.getSolution()[idx].getAnswer()==4) {
                gameData.setScore(gameData.getScore()+1);
                correct(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
            else{
                gameData.setHp(gameData.getHp()-1);
                wrong(imglist.get(idx), solution.getSolution()[idx].getAnswerString());
            }
        });
    }

    public void setnext(){

        if(gameData.getHp()==2) hp3.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        if(gameData.getHp()==1) hp2.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        if(gameData.getHp()==0) {
            hp1.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            if(gameData.getHp_chance()==1) {
                Button[] buttons;
                buttons = AnimationDialog.init(this, false);
                AnimationDialog.create("생명이 없습니다.\n 생명을 1 추가하시겠습니까?", "남은 기회 : 1번", "Not Ok!");
                AnimationDialog.add_secend_button("OK!");
                buttons[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        AnimationDialog.close();
                    }
                });
                buttons[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameData.setHp_chance(0);
                        gameData.setHp(1);
                        hp1.setImageResource(R.drawable.hearticon);
                        AnimationDialog.close();
                    }
                });
                AnimationDialog.show();
            }
            else{
                finish();
            }
        }

        idx++;
        gameData.setStage(gameData.getStage()+1);
        window1.setResourceId(imglist.get((idx)));
        window2.setResourceId(imglist.get((idx)));
        option1.setText(solution.getSolution()[idx].geto1());
        option2.setText(solution.getSolution()[idx].geto2());
        option3.setText(solution.getSolution()[idx].geto3());
        option4.setText(solution.getSolution()[idx].geto4());
        stage.setText("STAGE " + gameData.getStage());
        score.setText("SCORE : " + gameData.getScore());
        gameData.setShow(3);
        show_count.setText(String.valueOf(gameData.getShow()));
        show.setClickable(true);
    }

    public void exitnotification() {
        Button[] buttons;
        buttons=AnimationDialog.init(this,false);
        AnimationDialog.create("종료하시겠습니까?","점수는 저장되지 않습니다.","Not Ok!");
        AnimationDialog.add_secend_button("OK!");
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationDialog.close();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationDialog.close();
                finish();
            }
        });

        AnimationDialog.show();
    }

    public void correct(int res, String answer){
        setnext();
        mAnimationDialog.create("정답입니다!","정답 : " + answer,"Next");
        mAnimationDialog.set_image(res);
        mAnimationDialog.show();
    }

    public void wrong(int res, String answer){
        setnext();
        mAnimationDialog.create("틀렸습니다.","정답 : " + answer,"Next");
        mAnimationDialog.set_image(res);
        mAnimationDialog.show();
    }
}