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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    private TextView namename,noti;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private int count = -1;
    public ArrayList<DataPlanner> plan = new ArrayList<>();

    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseUser == null){
            // Not signed in, launch the sign in activity.
            startActivity(new Intent(this, EmailLogin.class));

        }else {

            uid = mFirebaseUser.getUid().toString();

            //fragment
            if(savedInstanceState==null){
                //first create
                //Place fragment
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentcontainer,new GalleryFragment())
                        .commit();
            }

            initInstances();

        }

    }

    private  void initInstances(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        noti = (TextView)findViewById(R.id.family_hub_tv_count);
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


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );


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
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gallery:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,GalleryFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_request:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,RequestFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_planner:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer, PlannerFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_noti:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,NotiFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                noti.setVisibility(View.GONE);
                break;
            case R.id.bt_setting:
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
