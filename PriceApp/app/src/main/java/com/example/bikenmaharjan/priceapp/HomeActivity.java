package com.example.bikenmaharjan.priceapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import com.example.bikenmaharjan.priceapp.MyFirebaseInstanceIDService;


/*
*  TODO:
* + CardView in Top
* + ListView or RecycleView in Botton with Custom
* */


public class HomeActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{


    private static final String TAG = "token";
    RecyclerView rv;
//    ArrayList products = new ArrayList<UserProductInformation>();
    List<UserProductInformation> products;
    RVAdapter adapter;

    CardView cv;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    private GestureDetectorCompat GD;    //must instantiate the gesture detector

    private int SWIPE_MIN_DISTANCE=120;
    private int SWIPE_THRESHOLD_VELOCITY=150;

    private String productID = "";


//    products = new ArrayList<UserProductInformation>();

    // for menu
    private DrawerLayout mDraw;
    private ActionBarDrawerToggle mToggle;
    NavigationView mainNavigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        products = new ArrayList<UserProductInformation>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        initFCM();

        rv = (RecyclerView) findViewById(R.id.rv);
        //cv = (CardView) findViewById(R.id.cv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        GD = new GestureDetectorCompat(this, this);
        GD.setOnDoubleTapListener(this);

        // menu
        mDraw = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDraw, R.string.open, R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        mainNavigationMenu = (NavigationView) findViewById(R.id.navId);


        // add intent to go out
        mainNavigationMenu.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){

                            case R.id.addProduct:

                                Intent intent = new Intent(getBaseContext(),Input.class);
                                startActivity(intent);
                                return true;
                            case R.id.home:
                                Intent intent3 = new Intent(getBaseContext(),HomeActivity.class);
                                startActivity(intent3);
                                return true;
                            case R.id.profile:
                                Intent intent4 = new Intent(getBaseContext(),Profile.class);
                                startActivity(intent4);
                                return true;




                            case R.id.signOut:
                                FirebaseAuth.getInstance().signOut();
                                Intent intent2=new Intent(getBaseContext(),MainActivity.class);
                                startActivity(intent2);



                                break;
                                default:


                        }
                        return false;
                    }
                });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        myRef.child("Products").child(userID).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                products = new ArrayList<UserProductInformation>();
                showdata(dataSnapshot);
                adapter = new RVAdapter(products,getApplicationContext());
                rv.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rv,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return false;
                            }

                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
//                                for (int position : reverseSortedPositions) {

                                    Log.i("19960302",reverseSortedPositions+"");

                                    products.remove(reverseSortedPositions);
                                    adapter.notifyItemRemoved(reverseSortedPositions[0]);
                                    productID = products.get(reverseSortedPositions[0]).getId();
                                    myRef.child("Products").child(userID).child(productID).removeValue();

//                                    Log.i("19960302",products.get(position).toString());


//                                }
                                adapter.notifyDataSetChanged();




                                /* code here */
                                // product must be removed here from database here



                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                // do nothing
                                adapter.notifyDataSetChanged();
                            }
                        });


        rv.addOnItemTouchListener(swipeTouchListener);


    }




    // menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFCM(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "initFCM: token: " + token);
        sendRegistrationToServer(token);


    }
    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("messaging_token")
                .setValue(token);
    }
    public void showdata(DataSnapshot dataSnapshot){

        Log.i("Debug2","Goes In onBind");
        Log.i("log1","Goes In");

        for(DataSnapshot ds : dataSnapshot.getChildren()){



            // get image info from here
            UserProductInformation uInfo = new UserProductInformation();

            uInfo.setName(ds.getValue(UserProductInformation.class).getName()); //set the name
            uInfo.setPrice(ds.getValue(UserProductInformation.class).getPrice()); //set the email
            uInfo.setDate(ds.getValue(UserProductInformation.class).getDate()); //set the date
            uInfo.setBarcode(ds.getValue(UserProductInformation.class).getBarcode());
            uInfo.setImage(ds.getValue(UserProductInformation.class).getImage());
            uInfo.setBestprice(ds.getValue(UserProductInformation.class).getBestprice());
            uInfo.setUrl(ds.getValue(UserProductInformation.class).getUrl());
            uInfo.setId(ds.getValue(UserProductInformation.class).getId());





            // add image here
            // image here
            products.add(uInfo);




        }
//
//
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.GD.onTouchEvent(event);               //Our GD will not automatically receive Android Framework Touch notifications.
        // Insert this line to consume the touch event locally by our GD,
        // IF YOU DON'T insert this before the return, our GD will not receive the event, and therefore won't do anything.

        Log.i("onTOuchEvent","BAD");
        return super.onTouchEvent(event);          // Do this last, why?

    }
    //////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        return true;
    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
            Intent intent = new Intent(this, Input.class);
            startActivity(intent);
        }


        return true;
    }


    public void goocr(View v) {


        //Intent iii=new Intent(getBaseContext(),edu.sfsu.cs.orange.ocr.CaptureActivity.class);
        //getBaseContext().startActivity(iii);


    }





}

