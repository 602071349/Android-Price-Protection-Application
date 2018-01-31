package com.example.bikenmaharjan.priceapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {


    private EditText txtemailinput, txtpwinput;
    private Button btnlogin;
    private TextView txtregister;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = Login.this;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPleaseWait = (TextView) findViewById(R.id.pleaseWait);
        mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

       // FirebaseAuth.getInstance().signOut();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){

//                    Intent intent = new Intent(Login.this,HomeActivity.class);
////                    intent.putExtra("UserEmail",txtemailinput.getText().toString());
//
//                    startActivity(intent);


                }
            }
        };


        btnlogin = (Button)findViewById(R.id.btnlogin);
        txtregister = (TextView)findViewById(R.id.txtregister);
        txtemailinput =  (EditText)findViewById(R.id.txtemailinput);
        txtpwinput =  (EditText)findViewById(R.id.txtpwinput);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });

        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                finish();


            }
        });


    }
    private void init() {


        //initialize the button for logging in
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick: attempting to log in.");

                String email = txtemailinput.getText().toString();
                String password = txtpwinput.getText().toString();

                if (isStringNull(email) && isStringNull(password)) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
//                                        Log.w(TAG, "signInWithEmail:failed", task.getException());

                                        Toast.makeText(Login.this, "Auth Failed!",
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        mPleaseWait.setVisibility(View.GONE);
                                    } else {
                                        try {
                                            if (user.isEmailVerified()) {
//                                                Log.d(TAG, "onComplete: success. email is verified.");
                                                Intent intent = new Intent(Login.this, HomeActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(mContext, "Email is not verified \n check your email inbox.", Toast.LENGTH_SHORT).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                mPleaseWait.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }
                                        } catch (NullPointerException e) {
//                                            Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage());
                                        }
                                    }

                                    // ...
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
    private boolean isStringNull(String string){
//        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }
//    private void startSignin(){
//        String email = txtemailinput.getText().toString();
//        String password = txtpwinput.getText().toString();
//
//
//        if(TextUtils.isEmpty(email)||(TextUtils.isEmpty(password))){
//            Toast.makeText(Login.this, "Some field is blank!", Toast.LENGTH_LONG).show();
//
//        }
//        else{
//            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(!task.isSuccessful()){
//                        Toast.makeText(Login.this, "Unsuccessful Sign In", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//
//        }
//
//
//
//    }
}
