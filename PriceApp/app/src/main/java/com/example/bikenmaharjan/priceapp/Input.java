package com.example.bikenmaharjan.priceapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import com.transitionseverywhere.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;


public class Input extends AppCompatActivity {

    FloatingActionButton scanner,ocrscan,barcodescan;
    RelativeLayout inputlayout;


    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private Button mb, mbar;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    static final int request_code = 1;
    Spinner customSpinner;

    String barcode;
    String bestprice;
    String price;
    String date;
    String url;
    String image;
    String cc = "Bank Of China";

    private DatabaseReference myRef;
    private String userID;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText txtbarcode, txtdate, txtprice, txtbestprice, txtname, txturl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        customSpinner = (Spinner) findViewById(R.id.custom_spinner);
        inputlayout = (RelativeLayout)findViewById(R.id.inputlayout);
        scanner = (FloatingActionButton)inputlayout.findViewById(R.id.scanner);
        ocrscan = (FloatingActionButton)inputlayout.findViewById(R.id.ocrscan);
        barcodescan = (FloatingActionButton)inputlayout.findViewById(R.id.barcodescan);


        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MM/dd/YYYY", Locale.ENGLISH);
        date = simpleDateFormat.format(calendar.getTime());


        mb = (Button) findViewById(R.id.btndone);
//        mbar = (Button) findViewById(R.id.scan_barcode);
        txtdate = (EditText) findViewById(R.id.txtdate);
        txtprice = (EditText) findViewById(R.id.txtprice);
        txtbarcode = (EditText) findViewById(R.id.txtbarcode);
        txtdate = (EditText) findViewById(R.id.txtdate);
        txtname = (EditText) findViewById(R.id.txtname);

        txtdate.setText(date);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(inputlayout);
                ocrscan.setVisibility(View.VISIBLE);
                barcodescan.setVisibility(View.VISIBLE);
            }
        });
        barcodescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Input.this, BarcodeCaptureActivity.class);
                startActivityForResult(i,2);
            }
        });
        ocrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goocr(view);
            }
        });
        inputlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(inputlayout);
                ocrscan.setVisibility(View.GONE);
                barcodescan.setVisibility(View.GONE);

            }
        });

//        mbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent i = new Intent(Input.this, BarcodeCaptureActivity.class);
////                startActivityForResult(i,2);
////
////            }
////        });

        mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String product = "test";
//              String product = txtname.getText().toString();

                try {


                    url = "www.google.com";
                    bestprice = "";
                    price = txtprice.getText().toString();
                    //date = txtdate.getText().toString();
                    barcode = txtbarcode.getText().toString();


                    new ReadPlacesFeedTask().execute();
                    Log.i("Error1", "Empty field false");

                    //finish();

                } catch (Exception err) {

                    Log.i("Error1", "Empty field");

                }


            }
        });

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Input.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
//                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                txtdate.setText(date);
            }
        };
                        /* Setting up the CUSTOM SPINNER 1 (with ImageView)
            Here we need to make you of a Custom Class to hold the data - SpinnerData
            Then instead of using the simple adapter as used above, we create another class - a Custom Adapter Class (CustomSpinnerAdapter)
            Create an object of the adapter class and add it to the custom spinner
         */
        //Prepare the custom list
        final List<SpinnerData> customList1 = new ArrayList<>();//the customList1 holds a list of objects of the class SpinnerData.
        customList1.add(new SpinnerData("Bank Of China", R.drawable.boc));
        customList1.add(new SpinnerData("American Express", R.drawable.ae2));
        customList1.add(new SpinnerData("Bank Of America", R.drawable.boa));
        customList1.add(new SpinnerData("Citi", R.drawable.citi));
        customList1.add(new SpinnerData("Chase", R.drawable.chase2));
        customList1.add(new SpinnerData("Wells Fargo", R.drawable.wf));


        //Instantiate out custom spinner class
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(Input.this, R.layout.spinner_layout, customList1);
        customSpinner.setAdapter(customSpinnerAdapter);

        //Adding a listener to the custom spinner when an item is selected from the spinner
        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Displaying the selected icon name in a Toast
                cc = customList1.get(i).getIconName();
//                Toast.makeText(Input.this, customList1.get(i).getIconName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private class ReadPlacesFeedTask extends AsyncTask<String, Void, String> {

        String url;
        String product;
        Double bestPrice;
        boolean flag = true;

        protected String doInBackground(String... urls) {

            try {

                JSONObject json = (JSONObject) new JSONTokener(IOUtils.toString(new URL("http://18.217.62.106/upc/" + barcode))).nextValue();
                JSONArray items = json.getJSONArray("items");


                for (int i = 0; i < items.length(); i++) {

                    JSONObject c = items.getJSONObject(i);
                    // pass these values!!!
                    url = c.getString("productUrl");
                    bestPrice = c.getDouble("salePrice");
                    product = c.getString("name");

                    // default image for now
                    image = c.getString("largeImage");


                }

            } catch (Exception err) {

                // error values
                url = "www.google.com";
                bestprice = "$404";
                product = "Error";
                Log.i("Items_error", err + "");
                flag = false;


            }

            return "";


        }

        // image get from database
        protected void onPostExecute(String result) {

            //GETTINGS DATA FROM JSON ARRAY//
            // check fo rerror case!

            try {

                if (flag) {
                    // name is set
                    String key = myRef.child("Products").child(userID).push().getKey();
                    UserProductInformation userProductInformation = new UserProductInformation(product, price, date, String.valueOf(bestPrice), url, barcode
                            , image,key,cc);
                    myRef.child("Products").child(userID).child(key).setValue(userProductInformation);


                    // when done is clicked -- barcode is right
                    // show toast  --- see this
                    if (true) {


                        // doesn't go here
                        Log.i("BEST", bestprice + "");

                        Toast.makeText(getBaseContext(), "Best price:" + bestPrice,
                                Toast.LENGTH_SHORT).show();

                    }

                    finish();

                } else {

                    Toast.makeText(getBaseContext(), "Invalid input, Try Again",
                            Toast.LENGTH_SHORT).show();

                }


            } catch (Exception e) {

                Log.d("ReadPlacesFeedTask", e.getLocalizedMessage());


            }


        }


    }

    public void goocr(View v) {
        Intent iii = new Intent(getBaseContext(), edu.sfsu.cs.orange.ocr.CaptureActivity.class);
        startActivityForResult(iii, request_code);
    }

    //    public void gobarcode(View v) {
//        Intent iii=new Intent(getBaseContext(),barcodescanner.class);
//        startActivity(iii);
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                txtprice.setText(data.getStringExtra("price"));
                txtname.setText(data.getStringExtra("name"));
                Log.d("kangzhaoyaohahaha",data.getStringExtra("name"));
            }
        }
        if (requestCode == 2) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String x = data.getStringExtra("barcode");
                    txtbarcode.setText(x);
//                    barcodeValue.setText(barcode.displayValue);
//                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
//                } else {
//                    statusMessage.setText(R.string.barcode_failure);
//                    Log.d(TAG, "No barcode captured, intent data is null");
//                }
//            } else {
//                statusMessage.setText(String.format(getString(R.string.barcode_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
//            }
                }
            }//onActivityResult

        }
    }
}
