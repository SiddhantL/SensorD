package com.example.runningman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayButton extends AppCompatActivity {
    MediaPlayer mp;
    String high,str;
    TextView hscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_button);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView play=findViewById(R.id.playbtn);
        mp=MediaPlayer.create(this,R.raw.click);
        final TextView textView=findViewById(R.id.textView3);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        hscore= findViewById(R.id.textView4);
        high="0";
        str="0";
        final DatabaseReference rtd = FirebaseDatabase.getInstance().getReference();
        if (mAuth.getCurrentUser()!=null) {
            rtd.child("score").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(String.class) != null) {
                            if (Integer.parseInt(ds.getValue(String.class)) < Integer.parseInt(str)) {
                                high = str;
                            } else {
                                high = ds.getValue(String.class);
                            }
                        }
                    }
                    hscore.setText("My Highscore: " + high);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        final int[] array = {R.string.hint1,R.string.hint2,R.string.hint3,R.string.hint4,R.string.hint5};
        textView.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                textView.setText(array[i]);
                i++;
                i=i%5;
                textView.postDelayed(this, 5000);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                startActivity(new Intent(PlayButton.this,Select.class));
            }
        });
    }
}
