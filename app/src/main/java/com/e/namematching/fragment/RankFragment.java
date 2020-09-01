package com.e.namematching.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.e.namematching.R;

public class RankFragment extends Fragment {

    private View view;
//    private MaterialToolbar toolbar;
//    private CircleImageView imgProfile;
//    private TextView txtName,txtPostsCount;
//    private Button btnEditAccount;
//    private RecyclerView recyclerView;
//    private ArrayList<Post> arrayList;
//    private SharedPreferences preferences;
//    private AccountPostAdapter adapter;
//    private String imgUrl = "";

    public RankFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_rank,container,false);
        init();
        return view;
    }

    private void init() {

    }

}
