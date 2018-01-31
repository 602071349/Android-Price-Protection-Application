package edu.sfsu.cs.orange.ocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button btn = (Button)findViewById(R.id.btn);
        Intent intent = new Intent(getBaseContext(),CaptureActivity.class);
        startActivity(intent);
    }
}
