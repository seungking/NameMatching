package com.e.namematching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.continuouscrollable.ContinuousScrollableImageView;
import com.e.namematching.model.GameData;
import com.e.namematching.model.RankUser;
import com.e.namematching.model.Set;
import com.e.namematching.model.Solution;
import com.e.namematching.model.functions;
import com.e.namematching.model.mAnimationDialog;
import com.example.animationdialog.AnimationDialog;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
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
    TextView play_question;

    SoundPool soundPool_play;	//작성
    int sound_show;
    int sound_correct;
    int sound_wrong;
    int sound_next;
    int sound_start;
    int sound_end;
    int waitLimit = 1000;
    int waitCounter = 0;
    int throttle = 10;

//    @Nullable
//    @BindView(R.id.play_window1)
//    ContinuousScrollableImageView window1;
//
//    @Nullable
//    @BindView(R.id.play_window2)
//    ContinuousScrollableImageView window2;

//    @Nullable
//    @BindView(R.id.play_show)
//    Button show_bgm;
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
    private Set curset;
    private int w_res;

    Button[] dialog_buttons;

    SharedPreferences sharedPreferences;

    private AdView mAdView;
    private RewardedAd rewardedAd;
    private RewardedAd rewardedAd2;
    private RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    RewardedAdLoadCallback adLoadCallback;
    RewardedAdLoadCallback adLoadCallback2;

    //database
    private FirebaseDatabase mFirebaseDatase;
    private DatabaseReference mUser;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_play);

        mFirebaseDatase = FirebaseDatabase.getInstance();
        mUser = mFirebaseDatase.getReference("list");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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

        //전면광고
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1992325656759505/5558954060");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //

        //리워드 광고
        rewardedAd = new RewardedAd(this,
                "ca-app-pub-1992325656759505/2809695318");
        adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        //

        //리워드 광고
        rewardedAd2 = new RewardedAd(this,
                "ca-app-pub-1992325656759505/2809695318");
        adLoadCallback2 = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
        rewardedAd2.loadAd(new AdRequest.Builder().build(), adLoadCallback2);
        //

        initdata();
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void initdata(){

        //데이터 set
        gameData = new GameData();
        solution = new Solution();

        for (int i=1; i < solution.getSolution().length+1; i++) {
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
        play_question = findViewById(R.id.play_question);
        soundPool_play = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        sound_show = soundPool_play.load(this,R.raw.show_bgm,1);	//작성, (mp3 파일 이름이 click_sound이다.)
        sound_correct = soundPool_play.load(this,R.raw.correct_bgm,2);	//작성, (mp3 파일 이름이 click_sound이다.)
        sound_wrong = soundPool_play.load(this,R.raw.wrong_bgm,3);	//작성, (mp3 파일 이름이 click_sound이다.)
        sound_next = soundPool_play.load(this,R.raw.wrong_bgm,4);	//작성, (mp3 파일 이름이 click_sound이다.)
        sound_start = soundPool_play.load(this,R.raw.play_start_bgm,5);	//작성, (mp3 파일 이름이 click_sound이다.)
        sound_end = soundPool_play.load(this,R.raw.play_end_bgm,5);	//작성, (mp3 파일 이름이 click_sound이다.)

        //db
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //dialog 버튼 설정
        dialog_buttons= mAnimationDialog.init(this,false);
        dialog_buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnimationDialog.close();
                if(setlist.size()==0) finsh_button();
            }
        });

        //설명 다이얼로그
        mAnimationDialog.create("Press the blue button","It's going fast!","Start");
        mAnimationDialog.set_image(getResources().getIdentifier("@drawable/play_button_img", "drawable", this.getPackageName()));
        mAnimationDialog.show();

        //값 넣어줌
        setnext();

        //소리체크
        soundcheck = sharedPreferences.getInt("sound",1);
        if(soundcheck==1) sound.setImageResource(R.drawable.ic_baseline_volume_up_24);
        else if(soundcheck==0)sound.setImageResource(R.drawable.ic_baseline_volume_off_24);

        sound.setOnClickListener(v->{
            if(soundcheck==1) sound.setImageResource(R.drawable.ic_baseline_volume_off_24);
            else if(soundcheck==0)sound.setImageResource(R.drawable.ic_baseline_volume_up_24);
            soundcheck = (soundcheck==1)? 0:1;
        });

        //보이기
        show.setOnClickListener(v->{
            if (gameData.getHp() == 0) finsh_button();
            else {
                if (soundcheck == 1) {
                    while (soundPool_play.play(sound_show, 1, 1, 0, 0, 1) == 0 && waitCounter < waitLimit) {
                        waitCounter++;
                        SystemClock.sleep(throttle);
                    }
                }

                window1.setVisibility(View.VISIBLE);
                window2.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        window1.setVisibility(View.INVISIBLE);
                        window2.setVisibility(View.INVISIBLE);
                    }
                }, 30);
                gameData.setShow(gameData.getShow() - 1);
                show_count.setText(String.valueOf(gameData.getShow()));
                if (gameData.getShow() <= 0) {
                    show.setClickable(false);
                }
            }
        });

        telescope_plus.setOnClickListener(v->{
            Button[] buttons;
            buttons=AnimationDialog.init(this,false);
            AnimationDialog.create("Do you want to add three views?","Opportunity Remaining : 1","Not Ok!");
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
                    Log.d("log1", "ok");
                    if (rewardedAd2.isLoaded()) {
                        rewardedAd2.loadAd(new AdRequest.Builder().build(), adLoadCallback2);
                        Activity activityContext = PlayActivity.this;
                        RewardedAdCallback adCallback = new RewardedAdCallback() {
                            @Override
                            public void onRewardedAdOpened() {
                                // Ad opened.
                                AnimationDialog.close();
                            }

                            @Override
                            public void onRewardedAdClosed() {
                                // Ad closed.
                                AnimationDialog.close();
                            }

                            @Override
                            public void onUserEarnedReward(@NonNull com.google.android.gms.ads.rewarded.RewardItem rewardItem) {
                                telescope_plus.setImageResource(R.drawable.telescope_plus_used);
                                telescope_plus.setClickable(false);
                                gameData.setChance(gameData.getChance()-1);
                                gameData.setShow(3 + gameData.getShow());
                                show.setClickable(true);
                                show_count.setText(String.valueOf(gameData.getShow()));
                                Toasty.success(PlayActivity.this, "Three more opportunities have been added.", Toast.LENGTH_SHORT, true).show();
                                AnimationDialog.close();
                            }

                            @Override
                            public void onRewardedAdFailedToShow(AdError adError) {
                                // Ad failed to display.
                                Toasty.info(PlayActivity.this, "Reward payment failed.", Toast.LENGTH_SHORT, true).show();
                                AnimationDialog.close();
                            }
                        };
                        rewardedAd2.show(activityContext, adCallback);
                    } else {
                        Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                    }
                }
            });
            AnimationDialog.show();
        });

        back.setOnClickListener(v->{
            exitnotification();
        });

        option1.setOnClickListener(v-> {
            if (gameData.getHp() == 0) finsh_button();
            else {
                if (curset.getAnswer() == 1) {
                    gameData.setScore(gameData.getScore() + 1);
                    correct(w_res, curset.getAnswerString());
                } else {
                    gameData.setHp(gameData.getHp() - 1);
                    wrong(w_res, curset.getAnswerString());
                }
            }
        });
        option2.setOnClickListener(v->{
            if (gameData.getHp() == 0) finsh_button();
            else {
                if (curset.getAnswer() == 2) {
                    gameData.setScore(gameData.getScore() + 1);
                    correct(w_res, curset.getAnswerString());
                } else {
                    gameData.setHp(gameData.getHp() - 1);
                    wrong(w_res, curset.getAnswerString());
                }
            }
        });
        option3.setOnClickListener(v->{
            if (gameData.getHp() == 0) finsh_button();
            else {
                if (curset.getAnswer() == 3) {
                    gameData.setScore(gameData.getScore() + 1);
                    correct(w_res, curset.getAnswerString());
                } else {
                    gameData.setHp(gameData.getHp() - 1);
                    wrong(w_res, curset.getAnswerString());
                }
            }
        });
        option4.setOnClickListener(v->{
            if (gameData.getHp() == 0) finsh_button();
            else {
                if (curset.getAnswer() == 4) {
                    gameData.setScore(gameData.getScore() + 1);
                    correct(w_res, curset.getAnswerString());
                } else {
                    gameData.setHp(gameData.getHp() - 1);
                    wrong(w_res, curset.getAnswerString());
                }
            }
        });

        while(soundPool_play.play(sound_start,1,1,0,0,1) == 0 && waitCounter < waitLimit){
            waitCounter++; SystemClock.sleep(throttle);
        }
    }

    public void setnext(){

        if(gameData.getHp()==2) hp3.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        if(gameData.getHp()==1) hp2.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        if(gameData.getHp()==0) {
            hp1.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            if(gameData.getHp_chance()==1) {
                Button[] buttons;
                buttons = AnimationDialog.init(this, false);
                AnimationDialog.create("No life\n add one more life?", "Opportunity Remaining : 1", "Not Ok!");
                AnimationDialog.add_secend_button("OK!");
                AnimationDialog.setCanceledOnTouchOutside(false);
                buttons[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finsh_button();
                        AnimationDialog.close();
                    }
                });
                buttons[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("log1", "ok");
                        if (rewardedAd.isLoaded()) {
                            rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
                            Activity activityContext = PlayActivity.this;
                            RewardedAdCallback adCallback = new RewardedAdCallback() {
                                @Override
                                public void onRewardedAdOpened() {
                                    // Ad opened.
                                    AnimationDialog.close();
                                }

                                @Override
                                public void onRewardedAdClosed() {
                                    // Ad closed.
                                    AnimationDialog.close();
                                }

                                @Override
                                public void onUserEarnedReward(@NonNull com.google.android.gms.ads.rewarded.RewardItem rewardItem) {
                                    gameData.setHp_chance(0);
                                    gameData.setHp(1);
                                    hp1.setImageResource(R.drawable.hearticon);
                                    Toasty.success(PlayActivity.this, "Life has been added.", Toast.LENGTH_SHORT, true).show();
                                    AnimationDialog.close();
                                }

                                @Override
                                public void onRewardedAdFailedToShow(AdError adError) {
                                    // Ad failed to display.
                                    finsh_button();
                                    Toasty.info(PlayActivity.this, "Reward payment failed.", Toast.LENGTH_SHORT, true).show();
                                    AnimationDialog.close();
                                }
                            };
                            rewardedAd.show(activityContext, adCallback);
                        } else {
                            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                        }
                    }
                });
                AnimationDialog.show();
            }
            else{
               finsh_button();
            }
        }

        idx = (int) (Math.random() * setlist.size() - 1);
        curset = setlist.get(idx);
        w_res = imglist.get(idx);
        gameData.setStage(gameData.getStage() + 1);
        window1.setResourceId(imglist.get((idx)));
        window2.setResourceId(imglist.get((idx)));
        if(curset.geto1().length()>13)option1.setTextSize(13);
        else option1.setTextSize(18);
        if(curset.geto2().length()>13)option2.setTextSize(13);
        else option2.setTextSize(18);
        if(curset.geto3().length()>13)option3.setTextSize(13);
        else option3.setTextSize(18);
        if(curset.geto4().length()>13)option4.setTextSize(13);
        else option4.setTextSize(18);
        option1.setText(curset.geto1());
        option2.setText(curset.geto2());
        option3.setText(curset.geto3());
        option4.setText(curset.geto4());
        play_question.setText(curset.getQuestion());
        stage.setText("Stage " + gameData.getStage());
        score.setText(" Score : " + gameData.getScore());
        gameData.setShow(gameData.getShow() + 3);
        show_count.setText(String.valueOf(gameData.getShow()));
        show.setClickable(true);


    }

    public void exitnotification() {
        Button[] buttons;
        buttons=AnimationDialog.init(this,false);
        AnimationDialog.create("Do you want to exit?","Scores are not saved.","Not Ok!");
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
        if(soundcheck==1) {
            while(soundPool_play.play(sound_correct,1,1,0,0,1) == 0 && waitCounter < waitLimit){
                waitCounter++; SystemClock.sleep(throttle);
            }
        }
        Log.d("log1",android.os.Build.SERIAL);
        setlist.remove(idx);
        imglist.remove(idx);
        if(setlist.size()>0)setnext();
        mAnimationDialog.create("That's correct!!","Answer : " + answer,"Next");
        mAnimationDialog.set_image(res);
        mAnimationDialog.show();
    }

    public void wrong(int res, String answer){
        if(soundcheck==1) {
            while(soundPool_play.play(sound_wrong,1,1,0,0,1) == 0 && waitCounter < waitLimit){
                waitCounter++; SystemClock.sleep(throttle);
            }
        }
        Log.d("log1",android.os.Build.SERIAL);
        setlist.remove(idx);
        imglist.remove(idx);
        if(setlist.size()>0)setnext();
        mAnimationDialog.create("Incorrect!","Answer : " + answer,"Next");
        mAnimationDialog.set_image(res);
        mAnimationDialog.show();
    }

    public void finsh_button(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        int prev_score = sharedPreferences.getInt("score",0);
        if(prev_score<gameData.getScore()) {
            mUser.child(sharedPreferences.getString("id","")).setValue(new RankUser(sharedPreferences.getString("photo",""),sharedPreferences.getString("name",""),gameData.getScore()));
            editor.putInt("score", gameData.getScore());
            editor.commit();
        }

        Button[] buttons;
        buttons=mAnimationDialog.init(this,false);
        mAnimationDialog.create("Score : " + gameData.getScore(),"Please update the ranking.","OK");
        mAnimationDialog.setCanceledOnTouchOutside(false);
        mAnimationDialog.set_image(getResources().getDrawable(R.drawable.gameover));
//        mAnimationDialog.setCanceledOnTouchOutside(false);
//        AnimationDialog.set_parent_background("#7171ff");
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
//                AnimationDialog.close();
                finish();
            }
        });

        mAnimationDialog.show();
        while(soundPool_play.play(sound_end,1,1,0,0,1) == 0 && waitCounter < waitLimit){
            waitCounter++; SystemClock.sleep(throttle);
        }
    }

}