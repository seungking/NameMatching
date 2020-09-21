package com.e.namematching.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.e.namematching.BuildConfig;
import com.e.namematching.MainActivity;
import com.e.namematching.R;
import com.e.namematching.SetAccount;
import com.e.namematching.model.functions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class AccountFragment extends Fragment implements BillingProcessor.IBillingHandler{

    private View view;

//    private MaterialToolbar toolbar;
    @BindView(R.id.account_profile)
    CircleImageView imgProfile;
    @BindView(R.id.account_name)
    TextView name;
//    @BindView(R.id.account_score)
//    TextView score;
//    @BindView(R.id.account_rank)
//    TextView rank;
    @BindView(R.id.setting_edit_account)
    LinearLayout editaccount;
    @BindView(R.id.setting_sound)
    LinearLayout sound;
    @BindView(R.id.setting_feedback)
    LinearLayout feedback;
    @BindView(R.id.setting_donation)
    LinearLayout donation;
    @BindView(R.id.setting_sound_img)
    ImageView sound_img;
    @BindView(R.id.setting_sound_text)
    TextView sound_txt;

    private BillingProcessor bp;

    int soundcheck=1;
    int in_account_sound=0;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    int waitLimit = 1000;
    int waitCounter = 0;
    int throttle = 10;

    //Sound
    public static SoundPool soundPool;	//작성
    public static int soundID;		//작성

    public AccountFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account,container,false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {

        bp = new BillingProcessor(getContext(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkH/6s6gO++pn+14KRtrxVwp67/uextdJAmFOVlDkYepJzq4uiM+4MPf+vO0RGyw00KCUlVPdi06rmwnA2QHrMYTXUdc6tqhET72tHePcCJu3CfhAyelb4fnwN84JCziTNZ58NWXPZkWUF0anDGB6ylLGl/ipMadvKq3KmoRUunE+WOPXeppTFk9xqHYT7D3bUt28prv6AP/1ywquEYyn87su2+6b9DWASSM89Hwx79NmEQHCtTBPk5W4LAcfShsJUcgZH7ZfozMcO4xKdKRpiWnNCOd68ZwZo1Ym5BhxdY9pizFDnKv9+Jw91jiOCCgUw8ODpArRouqDCNGpfX8sbwIDAQAB", this);

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);	//작성
        soundID = soundPool.load(getContext(),R.raw.bgm_ha0,1);	//작성, (mp3 파일 이름이 click_sound이다.)

        preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        editor = preferences.edit();
        editor.putInt("sound",1);
        editor.commit();

        editaccount.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), SetAccount.class);
            intent.putExtra("from",1);
            startActivity(intent);
        });

        donation.setOnClickListener(v->{
            bp.purchase((Activity) getContext(), "donation");
        });

        sound.setOnClickListener(v->{
            if(soundcheck==1) {
                editor.putInt("sound", 0);
                sound.setBackground(getResources().getDrawable(R.drawable.corners_setting_off));
                sound_img.setImageResource(R.drawable.ic_baseline_volume_off_24);
                sound_txt.setText("Sound OFF");

                if(in_account_sound==0)
                    ((MainActivity)getActivity()).soundPool.stop(((MainActivity)getActivity()).soundID);
                else {
                    soundPool.stop(soundID);
                    soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);	//작성
                    soundID = soundPool.load(getContext(),R.raw.bgm_ha0,1);	//작성, (mp3 파일 이름이 click_sound이다.)
                }
                ((MainActivity)getActivity()).soundcheck=0;
                soundcheck=0;
                in_account_sound=1;
            }
            else {
                editor.putInt("sound", 1);
                sound.setBackground(getResources().getDrawable(R.drawable.corners_setting));
                sound_img.setImageResource(R.drawable.ic_baseline_volume_up_24);
                sound_txt.setText("Sound ON");

                while(soundPool.play(soundID,1,1,0,-1,1) == 0 && waitCounter < waitLimit){
                    waitCounter++; SystemClock.sleep(throttle);
                }
                soundcheck=1;
            }

            ((MainActivity)getActivity()).soundcheck=0;
            editor.commit();
        });

        feedback.setOnClickListener(v->{
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("plain/text");
            String[] address = {"ahnseungkl@gmail.com"};
            email.putExtra(Intent.EXTRA_EMAIL, address);
            email.putExtra(Intent.EXTRA_SUBJECT, "[FROM 1 Secound Quiz]");
            startActivity(email);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        imgProfile.setImageBitmap(new functions().StringToBitmap(preferences.getString("photo","")));
        name.setText(preferences.getString("name", "WELLCOME!"));

//        score.setText(String.valueOf(preferences.getInt("score",0)) + "점");
//        rank.setText(String.valueOf(preferences.getInt("rank",0)) + "위");
    }

    @Override
    public void onPause() {
        super.onPause();
        soundPool.stop(soundID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.stop(soundID);
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        if (productId.equals("donation")) {
            // TODO: 구매 해 주셔서 감사합니다! 메세지 보내기
            bp.isPurchased("donation");
            Toasty.success(getContext(), "Thank you!", Toast.LENGTH_SHORT, true).show();
            bp.consumePurchase("donation");
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            Toasty.info(getContext(), "Billing Error.", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onBillingInitialized() {
    }

}
