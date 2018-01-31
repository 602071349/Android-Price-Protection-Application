package com.example.bikenmaharjan.priceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import edu.sfsu.cs.orange.ocr.Start;

import static java.security.AccessController.getContext;

/**
 * Created by bikenmaharjan on 11/26/17.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> {

    CardView cv;
    int number;
    Context context;

    public  class ProductViewHolder extends RecyclerView.ViewHolder {

        //CardView cv;
        TextView productName;
        TextView amount;
        TextView barcode;
        ImageView thumbnail;
        TextView percent;


        public View view;



        ProductViewHolder(View itemView) {
            super(itemView);

            productName = (TextView)itemView.findViewById(R.id.person_name);
            amount = (TextView) itemView.findViewById(R.id.amount);
            barcode = (TextView) itemView.findViewById(R.id.barcode);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            percent = (TextView) itemView.findViewById(R.id.percent);


            itemView.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
                            // call here
                            Intent intent = new Intent(context,DetailActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            intent.putExtra("name",products.get(getPosition()).getName());
                            intent.putExtra("date",products.get(getPosition()).getDate());
                            intent.putExtra("original_price",products.get(getPosition()).getPrice());
                            intent.putExtra("best_price",products.get(getPosition()).getBestprice());
                            intent.putExtra("url",products.get(getPosition()).getUrl());
                            intent.putExtra("barcode",products.get(getPosition()).getBarcode());
                            intent.putExtra("image",products.get(getPosition()).getImage());

                            // image can be obtained


                            context.startActivity(intent);





                        }
                    }

            );


        }





    }

    // this property has the value
    List<UserProductInformation> products;



    // bind here
    RVAdapter(List<UserProductInformation> products, Context context){
        this.products = products;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);


        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
    }


    // set here similiar to call in iOS
    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {


        Log.i("Debug1","Goes In onBind");

        productViewHolder.productName.setText(products.get(i).getName());
        productViewHolder.amount.setText(products.get(i).getPrice());
        productViewHolder.barcode.setText(products.get(i).getBarcode());

        String best =  products.get(i).getBestprice().replace("$","");
        String old =  products.get(i).getPrice().replace("$","");
        DecimalFormat df = new DecimalFormat("#");
        double percent = ((Double.parseDouble(old) -  Double.parseDouble(best)) / (Double.parseDouble(old))*100);
        productViewHolder.percent.setText(df.format(percent)+"%");

        try {

            Picasso.with(context)
                    .load(products.get(i).getImage())
                    .into(productViewHolder.thumbnail);

            //////////////
            productViewHolder.view.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.i("Flag","Check");


                        }
                    }

            );
            //////////////

        }catch (Exception err){



        }

        if (i==2) {





        }else if (i == 1){



        }
        //ProductViewHolder.personAge.setText(products.get(i).age);
        //ProductViewHolder.personPhoto.setImageResource(products.get(i).photoId);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }




}
