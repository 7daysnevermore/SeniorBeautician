package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 14/1/2560.
 */

public class AddService extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> servicekey;

    private EditText inputS01;
    private EditText inputS02;
    private EditText inputS03;
    private EditText inputS04;

    private int s01_price;
    private int s02_price;
    private int s03_price;
    private int s04_price;

    Toolbar toolbar;

    TextView addall;

    Button add;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    LinearLayout S01,S02,S03,S04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addservice);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        servicekey = (ArrayList<String>)getIntent().getSerializableExtra("serviceno");

        S01 = (LinearLayout) findViewById(R.id.S01);
        S02 = (LinearLayout) findViewById(R.id.S02);
        S03 = (LinearLayout) findViewById(R.id.S03);
        S04 = (LinearLayout) findViewById(R.id.S04);


        add = (Button) findViewById(R.id.btn_add);
        addall = (TextView) findViewById(R.id.addall);

        if(servicekey.size() == 0){

            addall.setVisibility(View.VISIBLE);
        }
        else{

            add.setVisibility(View.VISIBLE);

            if (servicekey.contains("S01")){

                S01.setVisibility(View.VISIBLE);

            }

            if (servicekey.contains("S02")){

                S02.setVisibility(View.VISIBLE);

            }

            if (servicekey.contains("S03")) {

                S03.setVisibility(View.VISIBLE);

            }

            if (servicekey.contains("S04")) {

                S04.setVisibility(View.VISIBLE);

            }

            add.setOnClickListener(this);

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //checkbox
        final CheckBox S01check = (CheckBox) findViewById(R.id.makeupandhair);
        final CheckBox S02check = (CheckBox) findViewById(R.id.makeup);
        final CheckBox S03check = (CheckBox) findViewById(R.id.hairstyle);
        final CheckBox S04check = (CheckBox) findViewById(R.id.hairdress);

        //starting price of each service
        inputS01 = (EditText) findViewById(R.id.makeupandhair_price);
        final String S01 = inputS01.getText().toString();
        inputS02 = (EditText) findViewById(R.id.makeup_price);
        final String S02 = inputS02.getText().toString();
        inputS03 = (EditText) findViewById(R.id.hairstyle_price);
        final String S03 = inputS03.getText().toString();
        inputS04 = (EditText) findViewById(R.id.hairdress_price);
        final String S04 = inputS04.getText().toString();

        //create root of BeauticianService
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mUsersRef = mRootRef.child("beautician-service");

        //Change price to integer
        if(!TextUtils.isEmpty(S01)){
            s01_price = Integer.parseInt(S01);
        }
        if (!TextUtils.isEmpty(S02)) {
            s02_price = Integer.parseInt(S02);
        }
        if (!TextUtils.isEmpty(S03)) {
            s03_price = Integer.parseInt(S03);
        }
        if (!TextUtils.isEmpty(S04)) {
            s04_price = Integer.parseInt(S04);
        }

        DatabaseReference mProfilePromoteRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mProRef = mProfilePromoteRef.child("beautician-profilepromote/"+mFirebaseUser.getUid());
        mProRef.orderByChild("uid").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String keypro = null;

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    keypro = childSnapshot.getKey();
                }

                if (!keypro.equals(null)) {

                    //Add to profile promote
                    final DatabaseReference mPromoteRef = mRootRef.child("profilepromote").child(keypro);
                    final DatabaseReference mPromoteRefB = mRootRef.child("beautician-profilepromote/").child(mFirebaseUser.getUid()).child(keypro);


                    if (S01check.isChecked()) {
                        DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
                        HashMap<String, Object> UserValues = new HashMap<>();
                        UserValues.put("price", s01_price);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("S01", UserValues);

                        mUsersRefService.updateChildren(childUpdates);

                        mPromoteRef.child("S01").setValue(s01_price);
                        mPromoteRefB.child("S01").setValue(s01_price);
                    }
                    if (S02check.isChecked()) {
                        DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
                        HashMap<String, Object> UserValues = new HashMap<>();
                        UserValues.put("price", s02_price);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("S02", UserValues);

                        mUsersRefService.updateChildren(childUpdates);

                        mPromoteRef.child("S02").setValue(s02_price);
                        mPromoteRefB.child("S02").setValue(s02_price);
                    }
                    if (S03check.isChecked()) {
                        DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
                        HashMap<String, Object> UserValues = new HashMap<>();
                        UserValues.put("price", s03_price);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("S03", UserValues);

                        mUsersRefService.updateChildren(childUpdates);

                        mPromoteRef.child("S03").setValue(s03_price);
                        mPromoteRefB.child("S03").setValue(s03_price);
                    }
                    if (S04check.isChecked()) {
                        DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
                        HashMap<String, Object> UserValues = new HashMap<>();
                        UserValues.put("price", s04_price);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("S04", UserValues);

                        mUsersRefService.updateChildren(childUpdates);

                        mPromoteRef.child("S04").setValue(s04_price);
                        mPromoteRefB.child("S04").setValue(s04_price);
                    }

                    startActivity(new Intent(AddService.this, ViewServices.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
