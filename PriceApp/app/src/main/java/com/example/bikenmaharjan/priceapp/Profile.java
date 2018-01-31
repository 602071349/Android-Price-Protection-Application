package com.example.bikenmaharjan.priceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private TextView username1, email1, item1,value1;
    String name = "";
    String email = "";
    int value, item;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        username1 = (TextView)findViewById(R.id.username1);
        email1 = (TextView)findViewById(R.id.email1);
        item1 = (TextView)findViewById(R.id.item1);
        value1 = (TextView)findViewById(R.id.value1);

        Log.i("19960302111111",userID+"");

        myRef.child("Users").child(userID).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.getValue(String.class);

                username1.setText(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("Users").child(userID).child("Email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("19960302111111",dataSnapshot.getValue(String.class));

                email = dataSnapshot.getValue(String.class);
                email1.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.child("Products").child(userID).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item = (int)(dataSnapshot.getChildrenCount());
                item1.setText(item+"");
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    value += Integer.parseInt((ds.getValue(UserProductInformation.class).getPrice()).replace("$",""));

                }
                value1.setText(value+"");




            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        username1.setText(name);
////        email1.setText(email);
//        item1.setText(item + "");
//        value1.setText(value+ "");
    }

    public void signout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);

    }
}
