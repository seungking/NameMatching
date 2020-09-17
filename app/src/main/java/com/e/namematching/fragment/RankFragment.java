package com.e.namematching.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.e.namematching.R;
import com.e.namematching.adapter.RankAdapter;
import com.e.namematching.model.RankUser;
import com.e.namematching.model.functions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<RankUser> arrayList = new ArrayList<>();
    ArrayList<RankUser> arrayList2 = new ArrayList<>();
    RankAdapter adapter;

    TextView reset;
    TextView cur_rank;
    ImageView refresh;

    private FirebaseDatabase mFirebaseDatase;
    private DatabaseReference mUser;
    private DatabaseReference mReset;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ProgressDialog dialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public RankFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_rank,container,false);

        refresh = view.findViewById(R.id.rank_refresh);
        pref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        editor = pref.edit();

        init();
        setdata();

        return view;
    }

    @SuppressLint("MissingPermission")
    public void init() {

        mFirebaseDatase = FirebaseDatabase.getInstance();
        mUser = mFirebaseDatase.getReference("list");
        mReset = mFirebaseDatase.getReference("reset");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        recyclerView = view.findViewById(R.id.rank_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reset = view.findViewById(R.id.rank_remaining_time);
        cur_rank = view.findViewById(R.id.rank_current_rank);

        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        refresh.setOnClickListener(v->{
            setdata();
        });

    }

    public void setdata(){
        XProgressDialog dialog = new XProgressDialog(getContext(), "Loading..", XProgressDialog.THEME_HORIZONTAL_SPOT);
        dialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

            }
        },2300);

        ArrayList<String> ids = new ArrayList<>();
        final int[] rank = {0};

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                arrayList2.clear();
                Iterator<DataSnapshot> memberIterator = dataSnapshot.getChildren().iterator();
                int cur_score = pref.getInt("score",0);
                int onetime=1;
                int count=0;
                while( memberIterator.hasNext()) {
                    RankUser user = memberIterator.next().getValue(RankUser.class);

                    if(count<=50) arrayList2.add(0,user);
                    arrayList.add(0,user);
                    if(user.getScore()==cur_score && onetime==1) {
                        rank[0] = arrayList.size();
                        onetime=0;
                    }
                    count++;

                }

                HomeFragment.getInstance().home_rank.setText(String.valueOf(arrayList.size()-rank[0]+1));
                cur_rank.setText("Rank : " + String.valueOf(arrayList.size()-rank[0]+1));
                editor.putInt("rank",arrayList.size()-rank[0]+1);
                editor.commit();
                adapter = new RankAdapter(getContext(),arrayList2);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        mUser.orderByChild("score").addValueEventListener(postListener);

        mReset.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reset.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
