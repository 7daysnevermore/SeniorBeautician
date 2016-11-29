package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.fragment.GalleryFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.NotiFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PlannerFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.ProFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.RequestFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    private String name;
    private TextView showname;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

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
            //fragment
            if(savedInstanceState==null){
                //first create
                //Place fragment
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentcontainer,new GalleryFragment())
                        .commit();
            }

            name = mFirebaseUser.getDisplayName();
        }

        initInstances();
    }

    private  void initInstances(){

        showname = (TextView) findViewById(R.id.name);
        showname.setText(name);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //tab button
        findViewById(R.id.bt_gallery).setOnClickListener(this);
        findViewById(R.id.bt_request).setOnClickListener(this);
        findViewById(R.id.bt_planner).setOnClickListener(this);
        findViewById(R.id.bt_noti).setOnClickListener(this);
        findViewById(R.id.bt_promote).setOnClickListener(this);
        findViewById(R.id.v_pro).setOnClickListener(this);

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

    private void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gallery:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,GalleryFragment.newInstance())
                        .commit();
                break;
            case R.id.bt_request:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,RequestFragment.newInstance())
                        .commit();
                break;
            case R.id.bt_planner:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,PlannerFragment.newInstance())
                        .commit();
                break;
            case R.id.bt_noti:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,NotiFragment.newInstance())
                        .commit();
                break;
            case R.id.bt_promote:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentcontainer,ProFragment.newInstance())
                        .commit();
                break;
            case R.id.v_pro:
                signOut();
                break;
        }
    }
}