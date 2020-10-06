package com.example.runningman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShowScore extends AppCompatActivity {
RecyclerView rv;
ArrayList<ListData> listData;
FirebaseAdapter adapter;
TextView hscore;
String high="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);
        Intent intent = getIntent();
        final String str = intent.getStringExtra("score");
        TextView scoretv = findViewById(R.id.textView);
        hscore= findViewById(R.id.textView2);
        scoretv.setText(str);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
        adapter = new FirebaseAdapter(listData);
        rv.setAdapter(adapter);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        high=str;
        final DatabaseReference rtd = FirebaseDatabase.getInstance().getReference();
        if (mAuth.getCurrentUser()!=null) {
            rtd.child("score").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(String.class) != null) {
                            if (Integer.parseInt(ds.getValue(String.class)) < Integer.parseInt(str)) {
                                rtd.child("score").child(mAuth.getUid()).child(mAuth.getCurrentUser().getDisplayName()).setValue(str);
                             high=str;
                            } else {
                                high=ds.getValue(String.class);
                            }
                        }
                    }
                    hscore.setText("My Highscore: "+high);
                    rtd.child("score").child(mAuth.getUid()).child(mAuth.getCurrentUser().getDisplayName()).setValue(high);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        rtd.child("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss:dataSnapshot.getChildren()){
rtd.child("score").child(dss.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot ps:dataSnapshot.getChildren()){
        listData.add(new ListData(ps.getKey(),Integer.parseInt(ps.getValue().toString())));
            // Descending Order
            Collections.sort(listData, new Comparator<ListData>() {

                @Override
                public int compare(ListData o1, ListData o2) {
                    return (int)(o2.getScore()-o1.getScore());
                }
            });
        adapter.notifyDataSetChanged();
        }}

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
