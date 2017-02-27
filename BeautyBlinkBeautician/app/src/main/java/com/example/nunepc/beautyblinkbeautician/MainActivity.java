package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.fragment.GalleryFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.NotiFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PlannerFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.RequestFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.SettingFragment;
import com.example.nunepc.beautyblinkbeautician.model.DataPlanner;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    private TextView namename,noti;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    ImageView bt_request,bt_gallery,bt_planner,bt_noti,bt_setting,tab_req,tab_gal,tab_plan,tab_noti,tab_set;
    private int count = -1;
    private Button notiBtn;
    private String previous = null;

    public HashMap<String,Integer> eventday;
    public ArrayList<HashMap<String,Integer>> listevent;

    public String uid;
    String menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        bt_request = (ImageView) findViewById(R.id.bt_request);
        bt_gallery = (ImageView) findViewById(R.id.bt_gallery);
        bt_planner = (ImageView) findViewById(R.id.bt_planner);
        bt_noti = (ImageView) findViewById(R.id.bt_noti);
        bt_setting = (ImageView) findViewById(R.id.bt_setting);
        tab_req = (ImageView) findViewById(R.id.tap_req);
        tab_gal = (ImageView) findViewById(R.id.tap_gal);
        tab_plan = (ImageView) findViewById(R.id.tap_plan);
        tab_noti = (ImageView) findViewById(R.id.tap_noti);
        tab_set = (ImageView) findViewById(R.id.tap_set);

        if(mFirebaseUser == null){
            // Not signed in, launch the sign in activity.
            startActivity(new Intent(this, EmailLogin.class));

        }else {

            uid = mFirebaseUser.getUid().toString();

            menu = getIntent().getStringExtra("menu");
            if(menu != null) {
                if (menu.equals("gallery")) {
                    previous = "gallery";
                    bt_gallery.setImageResource(R.mipmap.camera_703_click);
                    tab_gal.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.contentcontainer, new GalleryFragment())
                            .commit();
                } else {
                    //fragment
                    if (savedInstanceState == null) {
                        //first create
                        //Place fragment
                        previous = "request";
                        bt_request.setImageResource(R.mipmap.request_702_click);
                        tab_gal.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.contentcontainer, new RequestFragment())
                                .commit();
                    }
                }
            }else{
                //fragment
                if (savedInstanceState == null) {
                    //first create
                    //Place fragment
                    previous = "request";
                    bt_request.setImageResource(R.mipmap.request_702_click);
                    tab_req.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.contentcontainer, new RequestFragment())
                            .commit();
                }
            }


            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("beautician-planner").child(uid);

            listevent = new ArrayList<>();

            mRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dateChild : dataSnapshot.getChildren()){


                        for (DataSnapshot startChild : dateChild.getChildren()){

                            DataPlanner planner = startChild.getValue(DataPlanner.class);

                            eventday = new HashMap<>();
                            eventday.put("month",Integer.parseInt(planner.getMonth()));
                            eventday.put("day",Integer.parseInt(planner.getDay()));
                            eventday.put("year", Integer.parseInt(planner.getYear()));

                            listevent.add(eventday);

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }

            });

            initInstances();

        }

    }

    private  void initInstances(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        noti = (TextView)findViewById(R.id.family_hub_tv_count);
        notiBtn=(Button)findViewById(R.id.noti);
        notiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TestNoti.class);
                startActivity(intent);
            }
        });
        /*mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(MainActivity.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    namename = (TextView) findViewById(R.id.showname);
                    namename.setText(user.firstname);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        /*drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );*/


        //tab button
        findViewById(R.id.bt_gallery).setOnClickListener(this);
        findViewById(R.id.bt_request).setOnClickListener(this);
        findViewById(R.id.bt_planner).setOnClickListener(this);
        findViewById(R.id.bt_noti).setOnClickListener(this);
        findViewById(R.id.bt_setting).setOnClickListener(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customer-request");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int countt = (int) dataSnapshot.getChildrenCount();

                noti.setVisibility(View.VISIBLE);
                noti.setText(""+countt);
                Log.d("countnum","="+countt);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        //actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gallery:
                if(previous.equals("request")){
                    tab_req.setVisibility(View.GONE);
                    bt_request.setImageResource(R.mipmap.request_702);
                }
                if(previous.equals("planner")){
                    tab_plan.setVisibility(View.GONE);
                    bt_planner.setImageResource(R.mipmap.calendar_702);
                }
                if (previous.equals("noti")) {
                    tab_noti.setVisibility(View.GONE);
                    bt_noti.setImageResource(R.mipmap.noti_702);
                }
                if (previous.equals("setting")) {
                    tab_set.setVisibility(View.GONE);
                    bt_setting.setImageResource(R.mipmap.setting_703);
                }
                previous = "gallery";
                tab_gal.setVisibility(View.VISIBLE);
                bt_gallery.setImageResource(R.mipmap.camera_703_click);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,GalleryFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_request:
                if(previous.equals("gallery")){
                    tab_gal.setVisibility(View.GONE);
                    bt_gallery.setImageResource(R.mipmap.camera_703);
                }
                if(previous.equals("planner")){
                    tab_plan.setVisibility(View.GONE);
                    bt_planner.setImageResource(R.mipmap.calendar_702);
                }
                if (previous.equals("noti")) {
                    tab_noti.setVisibility(View.GONE);
                    bt_noti.setImageResource(R.mipmap.noti_702);
                }
                if (previous.equals("setting")) {
                    tab_set.setVisibility(View.GONE);
                    bt_setting.setImageResource(R.mipmap.setting_703);
                }
                previous = "request";
                tab_req.setVisibility(View.VISIBLE);
                bt_request.setImageResource(R.mipmap.request_702_click);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,RequestFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_planner:
                if(previous.equals("gallery")){
                    tab_gal.setVisibility(View.GONE);
                    bt_gallery.setImageResource(R.mipmap.camera_703);
                }
                if(previous.equals("request")){
                    tab_req.setVisibility(View.GONE);
                    bt_request.setImageResource(R.mipmap.request_702);
                }
                if (previous.equals("noti")) {
                    tab_noti.setVisibility(View.GONE);
                    bt_noti.setImageResource(R.mipmap.noti_702);
                }
                if (previous.equals("setting")) {
                    tab_set.setVisibility(View.GONE);
                    bt_setting.setImageResource(R.mipmap.setting_703);
                }
                previous = "planner";
                tab_plan.setVisibility(View.VISIBLE);
                bt_planner.setImageResource(R.mipmap.calendar_702_click);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer, PlannerFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_noti:
                if(previous.equals("gallery")){
                    tab_gal.setVisibility(View.GONE);
                    bt_gallery.setImageResource(R.mipmap.camera_703);
                }
                if(previous.equals("request")){
                    tab_req.setVisibility(View.GONE);
                    bt_request.setImageResource(R.mipmap.request_702);
                }
                if (previous.equals("planner")) {
                    tab_plan.setVisibility(View.GONE);
                    bt_planner.setImageResource(R.mipmap.calendar_702);
                }
                if (previous.equals("setting")) {
                    tab_set.setVisibility(View.GONE);
                    bt_setting.setImageResource(R.mipmap.setting_703);
                }
                previous = "noti";
                tab_noti.setVisibility(View.VISIBLE);
                bt_noti.setImageResource(R.mipmap.noti_702_click);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,NotiFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_setting:
                if(previous.equals("gallery")){
                    tab_gal.setVisibility(View.GONE);
                    bt_gallery.setImageResource(R.mipmap.camera_703);
                }
                if(previous.equals("request")){
                    tab_req.setVisibility(View.GONE);
                    bt_request.setImageResource(R.mipmap.request_702);
                }
                if (previous.equals("planner")) {
                    tab_plan.setVisibility(View.GONE);
                    bt_planner.setImageResource(R.mipmap.calendar_702);
                }
                if (previous.equals("noti")) {
                    tab_noti.setVisibility(View.GONE);
                    bt_noti.setImageResource(R.mipmap.noti_702);
                }
                previous = "setting";
                tab_set.setVisibility(View.VISIBLE);
                bt_setting.setImageResource(R.mipmap.setting_703_click);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer, SettingFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

}
