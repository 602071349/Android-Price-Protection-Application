package com.example.bikenmaharjan.priceapp;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by alan on 11/26/17.
 */

public class UserProductInformation{

    private String name;
    private String price;
    private String date;
    private String bestprice;
    private String url;
    private String barcode;
    private String image;
    private String id;
    private String cc;

    public UserProductInformation(){

    }

    public UserProductInformation(String name, String price, String date,String bestprice,String url,String barcode
    ,String image,String id,String cc) {
        this.price = price;
        this.name = name;
        this.date = date;
        this.bestprice = bestprice;
        this.url = url;
        this.barcode = barcode;
        this.image = image;
        this.id = id;
        this.cc = cc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBestprice() {
        return bestprice;
    }

    public void setBestprice(String bestprice) {
        this.bestprice = bestprice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getImage(){return image;}
    public void setImage(String image){this.image = image;};

    public String getId(){return id;}
    public void setId(String id){this.id = id;};

    public String getCc(){return  cc;}
    public void setCc(String cc){this.cc = cc;};


}
