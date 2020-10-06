package com.example.runningman;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
Button createUser;
TextView MoveToLogin;
EditText userEmailEdit,userPasswordEdit,userName;
    DatabaseReference mDatabase;
    FirebaseUser user;
//FireBase Authentication Field
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    //Assign ID's
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        createUser=(Button)findViewById(R.id.loginbtn);
        MoveToLogin=(TextView)findViewById(R.id.textView15);
        userEmailEdit=(EditText)findViewById(R.id.EmailEdittext);
        userPasswordEdit=(EditText)findViewById(R.id.PasswordeditText);
        userName=(EditText)findViewById(R.id.nameEdittext);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    createUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(SignUpActivity.this, "Creating Account", Toast.LENGTH_SHORT).show();
                            final String userNameS=userName.getText().toString().toString().trim();
                            String userEmailString=userEmailEdit.getText().toString().trim();
                            String userPasswordString=userPasswordEdit.getText().toString().trim();
                            if (!TextUtils.isEmpty(userEmailString) && (!TextUtils.isEmpty(userPasswordString)) && (!TextUtils.isEmpty(userNameS))){
                                mAuth.createUserWithEmailAndPassword(userEmailString,userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                      //      Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_LONG).show();

                                        }else {
                                            Toast.makeText(SignUpActivity.this, "Account could not be created", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
        //Assign Instances
        mAuth= FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user= firebaseAuth.getCurrentUser();
                if(user!=null){
                }else{
                    createUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String userNameS=userName.getText().toString().toString().trim();
                            String userEmailString=userEmailEdit.getText().toString().trim();
                            String userPasswordString=userPasswordEdit.getText().toString().trim();
                            if (!TextUtils.isEmpty(userEmailString) && (!TextUtils.isEmpty(userPasswordString)) && (!TextUtils.isEmpty(userNameS))){
                                mAuth.createUserWithEmailAndPassword(userEmailString,userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                        //    Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_LONG).show();

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(userName.getText().toString()).build();
                                            mAuth.getCurrentUser().updateProfile(profileUpdates);
                                            startActivity(new Intent(SignUpActivity.this,PlayButton.class));
                                        }else {
                                            Toast.makeText(SignUpActivity.this, "Account could not be created", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        };

        //OnClick Listeners



        MoveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);

    }
}
