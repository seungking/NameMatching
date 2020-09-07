package com.e.namematching.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.namematching.R;
import com.e.namematching.model.RankUser;
import com.e.namematching.model.functions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//public class RankAdapter extends RecyclerView.Adapter<RankAdapter.Myholder>{
//
//    private Context context;
//    private ArrayList<RankUser> list;
//
//    public RankAdapter(Context context, ArrayList<RankUser> list) {
//        Log.d("log1", String.valueOf(list.size()));
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d("log1", "create");
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rankuser,parent,false);
//        return new Myholder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Myholder holder, int position) {
////        holder.flag.setImageResource();
//        RankUser rankUser = list.get(position);
//        Log.d("log1", "1" + rankUser.getName());
//        holder.name.setText(rankUser.getName());
//        holder.score.setText(rankUser.getScore());
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//
//    class Myholder extends RecyclerView.ViewHolder{
//
////        private CircleImageView flag;
//        private TextView name;
//        private TextView score;
//
//        public Myholder(@NonNull View itemView) {
//            super(itemView);
////            flag = itemView.findViewById(R.id.rankuser_flag);
//            name = itemView.findViewById(R.id.rankuser_name);
//            Log.d("log1",name.toString());
//            score = itemView.findViewById(R.id.rankuser_score);
//        }
//    }
//}

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.Myholder>{

    private Context context;
    private ArrayList<RankUser> arrayList;

    private functions f = new functions();

    public RankAdapter(Context context, ArrayList<RankUser> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("log1", "create");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rankuser,parent,false);
        return new Myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        RankUser rankUser = arrayList.get(position);
        holder.photo.setImageBitmap(f.StringToBitmap(rankUser.getProfile()));
        holder.name.setText(rankUser.getName());
        holder.score.setText(String.valueOf(rankUser.getScore()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class Myholder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView score;
        private ImageView photo;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.rankuser_photo);
            name = itemView.findViewById(R.id.rankuser_name);
            score = itemView.findViewById(R.id.rankuser_score);
        }
    }
}