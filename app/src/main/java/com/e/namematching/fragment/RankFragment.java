package com.e.namematching.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.namematching.R;
import com.e.namematching.adapter.RankAdapter;
import com.e.namematching.model.RankUser;

import java.util.ArrayList;

public class RankFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<RankUser> arrayList = new ArrayList<>();
    RankAdapter adapter;

    public RankFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_rank,container,false);
        init();
        setdata();

        recyclerView = view.findViewById(R.id.rank_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RankAdapter(getContext(),arrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void init() {


    }

    public void setdata(){
        for(int i=0; i<10; i++){
            arrayList.add(new RankUser("안승기" + String.valueOf(i), i, null));
        }
    }
}
