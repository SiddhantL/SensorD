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

public class LoginActivity extends AppCompatActivity {
    //FireBase Authentication Field
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
TextView SignUpText,skip;
    Button loginbtn;
    EditText userEmailEdit,UserPasswordEdit;
    FirebaseUser user;
    //String Fields
    public static String userEmailString;
    String userPasswordString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//Assign ID's
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SignUpText=(TextView)findViewById(R.id.textView15);
loginbtn=(Button)findViewById(R.id.loginbtn);
skip=(TextView)findViewById(R.id.textView14);
skip.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
startActivity(new Intent(LoginActivity.this,PlayButton.class));
    }
});
userEmailEdit=(EditText)findViewById(R.id.EmailEdittext);
        UserPasswordEdit=(EditText)findViewById(R.id.PasswordeditText);
SignUpText.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent SignUpIntent=new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(SignUpIntent);
        finish();
    }
});
        //Assign Instances
        mAuth= FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                        startActivity(new Intent(LoginActivity.this,PlayButton.class));
                        finish();
                }else{

                }
            }
        };
//OnCreateListener
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Perform Login Operation
               userEmailString=userEmailEdit.getText().toString().trim();
                userPasswordString=UserPasswordEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPasswordString)){
                    mAuth.signInWithEmailAndPassword(userEmailString,userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                user=mAuth.getCurrentUser();
                                if (user.getDisplayName()==null){
                                   /* final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                                    View mView = getLayoutInflater().inflate(R.layout.activity_display_name, null);
                                    mBuilder.setView(mView);
                                    final AlertDialog dialog = mBuilder.create();
                                    dialog.show();*/
                                }else {
                                    startActivity(new Intent(LoginActivity.this, PlayButton.class));
                                }
                                }else {
                                Toast.makeText(LoginActivity.this, "User Login Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
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

