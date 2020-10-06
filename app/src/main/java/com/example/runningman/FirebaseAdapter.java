package com.example.runningman;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseAdapter extends RecyclerView.Adapter<FirebaseAdapter.ViewHolder>{
    private List<ListData>listData;

    public FirebaseAdapter(List<ListData> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ranks,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListData ld=listData.get(position);
        holder.name.setText(ld.getName());
        holder.score.setText(Integer.toString(ld.getScore()));
        holder.rank.setText(Integer.toString(position+1)+". ");
        if ((position+1)%2==0){
            holder.layout.setBackgroundColor(Color.WHITE);
            holder.score.setTextColor(Color.RED);
            holder.name.setTextColor(Color.RED);
            holder.rank.setTextColor(Color.BLACK);
        }else{
            holder.layout.setBackgroundColor(Color.RED);
            holder.score.setTextColor(Color.WHITE);
            holder.name.setTextColor(Color.WHITE);
            holder.rank.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,score,rank;
        private RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            score=(TextView)itemView.findViewById(R.id.score);
            rank=itemView.findViewById(R.id.rank);
            layout=itemView.findViewById(R.id.layout);
        }
    }
}

