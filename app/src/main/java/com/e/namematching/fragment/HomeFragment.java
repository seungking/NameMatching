package com.e.namematching.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.namematching.MainActivity;
import com.e.namematching.PlayActivity;
import com.e.namematching.R;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import stream.custombutton.CustomButton;

public class HomeFragment extends Fragment {

    private static HomeFragment instance = null;

    private View view;
    private CustomButton play;
    private String account_name="";
    private int score=0;
    private int rank=0;
    private SharedPreferences sharedPreferences;

    @BindView(R.id.home_rank)
    TextView home_rank;

    @BindView(R.id.home_score)
    TextView home_score;

    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home,container,false);
        ButterKnife.bind(this,view);
        init();
        instance = this;
        return view;
    }

    public static HomeFragment getInstance() {
        return instance;
    }

    private void init(){

        play = view.findViewById(R.id.play_button);
        play.setOnClickListener(v->{
            startActivity(new Intent(getActivity(),PlayActivity.class));
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        account_name = sharedPreferences.getString("name","");
        score = sharedPreferences.getInt("score",0);
        rank = sharedPreferences.getInt("rank",0);

//        home_rank.setText(String.valueOf(rank));
        home_score.setText(String.valueOf(score));

    }
}
