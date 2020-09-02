package com.e.namematching;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import com.cunoraz.continuouscrollable.ContinuousScrollableImageView;
import com.e.namematching.model.GameData;
import com.e.namematching.model.Solution;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

public class PlayActivity extends AppCompatActivity {

    ContinuousScrollableImageView window1;
    ContinuousScrollableImageView window2;
    FButton show;
    FButton option1;
    FButton option2;
    FButton option3;
    FButton option4;
    ImageView back;

    private ArrayList<Integer> imglist = new ArrayList<>();
    private int idx=0;
    private GameData gameData;
    private Solution solution;

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

        String packName = this.getPackageName(); // 패키지명
        for (int i=1; i < 49; i++) {
            String resName = "@drawable/img_" + i;
            int resID = getResources().getIdentifier(resName, "drawable", packName);
            imglist.add(resID);
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

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),imglist.get(idx));
        Bitmap blurredBitmap1 = blur(bitmap1);
        Drawable d = new BitmapDrawable(getResources(), blurredBitmap1);

        window1.setResourceId(imglist.get((idx)));
        window2.setResourceId(imglist.get((idx)));
        option1.setText(solution.getSolution()[idx].geto1());
        option2.setText(solution.getSolution()[idx].geto2());
        option3.setText(solution.getSolution()[idx].geto3());
        option4.setText(solution.getSolution()[idx].geto4());

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
        });

        back.setOnClickListener(v->{
            finish();
        });

        option1.setOnClickListener(v->{
            setnext();
        });
        option2.setOnClickListener(v->{
            setnext();
        });
        option3.setOnClickListener(v->{
            setnext();
        });
        option4.setOnClickListener(v->{
            setnext();
        });
    }

    public void setnext(){
        idx++;
        window1.setResourceId(imglist.get((idx)));
        window2.setResourceId(imglist.get((idx)));
        option1.setText(solution.getSolution()[idx].geto1());
        option2.setText(solution.getSolution()[idx].geto2());
        option3.setText(solution.getSolution()[idx].geto3());
        option4.setText(solution.getSolution()[idx].geto4());
    }

    //Set the radius of the Blur. Supported range 0 < radius <= 25
    private static final float BLUR_RADIUS = 25f;
    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

    //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}