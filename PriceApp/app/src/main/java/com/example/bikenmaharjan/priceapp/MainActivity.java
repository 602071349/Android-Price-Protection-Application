package com.example.bikenmaharjan.priceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*

        Main view of the screen [1]
        color: PrimaryColor -> 49C4A1
        Button (Blue) ->



 */

public class MainActivity extends AppCompatActivity {

    View vBtn;
    TextView signTxt;
    TextView registerTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide nav-bar
        hideNavBar();

        signTxt = (TextView) findViewById(R.id.signTxt);
        registerTxt = (TextView) findViewById(R.id.registerTxt);

        /* Action to Login screen */
        signTxt.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // connect sign View here
                        Intent loginIntent = new Intent(MainActivity.this, Login.class);
                        startActivity(loginIntent);

                    }
                }


        );
        
        /* Action to sign-up screen */
        registerTxt.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent RegisterIntent = new Intent(MainActivity.this, Register.class);
                        startActivity(RegisterIntent);

                        // connect register here

                    }
                }


        );

    }

    /* Helper methods */
    protected void hideNavBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

    }
}
