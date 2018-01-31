package com.example.bikenmaharjan.priceapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity  {



    TextView product_name_tv;
    TextView date_tv;
    TextView original_price_tv;
    TextView best_price_tv;
    TextView url_tv;
    TextView barcode_tv;
    ImageView imageView;
    Button done;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        product_name_tv = (TextView) findViewById(R.id.product_name);
        date_tv = (TextView) findViewById(R.id.date);
        original_price_tv = (TextView) findViewById(R.id.original_price);
        best_price_tv = (TextView) findViewById(R.id.best_price);
        url_tv = (TextView) findViewById(R.id.url);
        barcode_tv = (TextView) findViewById(R.id.barcode);
        done = (Button) findViewById(R.id.done);
        imageView = (ImageView) findViewById(R.id.img_detail);


        Intent intent = getIntent();


        String product_name = intent.getExtras().getString("name");
        String date = intent.getExtras().getString("date");
        String original_price = intent.getExtras().getString("original_price");
        String best_price = intent.getExtras().getString("best_price");
        url = intent.getExtras().getString("url");
        String barcode = intent.getExtras().getString("barcode");
        String image = intent.getExtras().getString("image");

        product_name_tv.setText(product_name);
        date_tv.setText(date);
        original_price_tv.setText(original_price);
        best_price_tv.setText(best_price);
        url_tv.setText("LINK");
        barcode_tv.setText(barcode);

        try {
            Picasso.with(this)
                    .load(image)
                    .into(imageView);
        }catch(Exception err){
            err.fillInStackTrace();
        }


        done.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }

        );




        url_tv.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

        );







    }


}
