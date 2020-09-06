package com.e.namematching.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.e.namematching.BuildConfig;
import com.e.namematching.R;
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

public class AccountFragment extends Fragment {

    private View view;

//    private MaterialToolbar toolbar;
    @BindView(R.id.account_profile)
    CircleImageView imgProfile;
    @BindView(R.id.account_name)
    TextView name;
    @BindView(R.id.account_score)
    TextView score;
    @BindView(R.id.account_rank)
    TextView rank;
    @BindView(R.id.setting_edit_account)
    LinearLayout editaccount;
    @BindView(R.id.setting_sound)
    LinearLayout sound;
    @BindView(R.id.setting_feedback)
    LinearLayout feedback;
    @BindView(R.id.setting_donation)
    LinearLayout donation;

    private SharedPreferences preferences;

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

        preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        imgProfile.setImageBitmap(StringToBitmap(preferences.getString("photo","")));
        name.setText(preferences.getString("name", "WELLCOME!"));

    }

    @Override
    public void onResume() {
        super.onResume();
        score.setText(String.valueOf(preferences.getInt("score",0)));
        rank.setText(String.valueOf(preferences.getInt("rank",0)));
    }

    public Bitmap StringToBitmap(String str) {
        try {
            byte[] decode = Base64.decode(str, 0);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
