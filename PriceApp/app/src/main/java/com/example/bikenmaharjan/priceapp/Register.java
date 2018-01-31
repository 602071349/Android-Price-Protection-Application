package com.example.bikenmaharjan.priceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by alan on 11/26/17.
 */

public class Register extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = Register.class.getSimpleName();

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    private FirebaseDatabase mFirebaseDatabase;


    private FirebaseAuth mAuth;

    private Button btnSU;
    private EditText txtName, txtEmail, txtPW, txtCPW;
    private TextView txtlogin;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSU = (Button)findViewById(R.id.btnregister);
        txtName = (EditText)findViewById(R.id.txtname);
        txtEmail = (EditText)findViewById(R.id.txtemail);
        txtPW = (EditText)findViewById(R.id.txtpw);
        txtCPW = (EditText)findViewById(R.id.txtcpw);
        txtlogin = (TextView)findViewById(R.id.txtlogin);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPleaseWait = (TextView) findViewById(R.id.pleaseWait);
        mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);


//
        txtlogin.setOnClickListener(this);
        btnSU.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

    }

    @Override
    public void onClick(View view) {

        if (view == txtlogin) {
            Intent intent = new Intent(Register.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        if (view == btnSU) {
            UserRegister();

//            Intent intent = new Intent(Register.this, Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);



        }

    }
//    private void createNewUser() {
//        final String name = txtName.getText().toString().trim();
//        final String email = txtEmail.getText().toString().trim();
//        String password = txtPW.getText().toString().trim();
//        String confirmPassword = txtCPW.getText().toString().trim();
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "Authentication successful");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            userID = user.getUid();
//                            myRef.child("Users").child(userID).child("Email").setValue(email);
//                            myRef.child("Users").child(userID).child("Name").setValue(name);
//                            Intent loginIntent = new Intent(Register.this, HomeActivity.class);
//                            startActivity(loginIntent);
//                        } else {
//                            Toast.makeText(Register.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
    private void UserRegister() {
        final String Name = txtName.getText().toString().trim();
        final String Email = txtEmail.getText().toString().trim();
        String Password = txtPW.getText().toString().trim();
        mPleaseWait.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        {if (TextUtils.isEmpty(Email)){
            Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
            mPleaseWait.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            return;
        }else if (TextUtils.isEmpty(Name)){
            Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
            mPleaseWait.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            return;
        }else if (TextUtils.isEmpty(Password)){
            Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
            mPleaseWait.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            return;
        }else if (Password.length()<6){
            Toast.makeText(Register.this,"Passwor must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            mPleaseWait.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            return;
        }}

//        mDialog.setMessage("Creating User please wait...");
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.show();

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendEmailVerification();
//                    mDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    userID = user.getUid();
                    myRef.child("Users").child(userID).child("Email").setValue(Email);
                    myRef.child("Users").child(userID).child("Name").setValue(Name);

                    mAuth.signOut();
                    mPleaseWait.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);

                }else{
                    Toast.makeText(Register.this,"error on creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Register.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();

                    }
                }
            });
        }
    }

}
