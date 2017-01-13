package com.example.nunepc.beautyblinkbeautician;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.model.BeauticianService;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ViewServices extends AppCompatActivity implements View.OnClickListener {

    TextView price_s01,price_s02,price_s03,price_s04;
    LinearLayout show_s01,show_s02,show_s03,show_s04;
    ImageView edit;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showservice);

        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        price_s01 = (TextView) findViewById(R.id.price_s01);
        price_s02 = (TextView) findViewById(R.id.price_s02);
        price_s03 = (TextView) findViewById(R.id.price_s03);
        price_s04 = (TextView) findViewById(R.id.price_s04);
        show_s01 = (LinearLayout) findViewById(R.id.shows01);
        show_s02 = (LinearLayout) findViewById(R.id.shows02);
        show_s03 = (LinearLayout) findViewById(R.id.shows03);
        show_s04 = (LinearLayout) findViewById(R.id.shows04);

        edit = (ImageView) findViewById(R.id.btn_editservice);

        DatabaseReference mRootRef01 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRootRef02 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRootRef03 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRootRef04 = FirebaseDatabase.getInstance().getReference();

        mRootRef01.child("beautician-service").child(uid+"/S01").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BeauticianService services = dataSnapshot.getValue(BeauticianService.class);
                if (services == null) {
                    show_s01.setVisibility(View.GONE);
                } else {
                    show_s01.setVisibility(View.VISIBLE);
                    price_s01.setText(Long.toString(services.price)+" Bath");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootRef02.child("beautician-service").child(uid + "/S02").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BeauticianService services = dataSnapshot.getValue(BeauticianService.class);
                if (services == null) {
                    show_s02.setVisibility(View.GONE);
                } else {
                    show_s02.setVisibility(View.VISIBLE);
                    price_s02.setText(Long.toString(services.price)+" Bath");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootRef03.child("beautician-service").child(uid + "/S03").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BeauticianService services = dataSnapshot.getValue(BeauticianService.class);
                if (services == null) {
                    show_s03.setVisibility(View.GONE);
                } else {
                    show_s03.setVisibility(View.VISIBLE);
                    price_s03.setText(Long.toString(services.price)+" Bath");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootRef04.child("beautician-service").child(uid + "/S04").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BeauticianService services = dataSnapshot.getValue(BeauticianService.class);
                if (services == null) {
                    show_s04.setVisibility(View.GONE);
                } else {
                    show_s04.setVisibility(View.VISIBLE);
                    price_s04.setText(Long.toString(services.price)+" Bath");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        edit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_edit:
                startActivity(new Intent(ViewServices.this, EditProfile.class));
                break;

        }
    }
}
