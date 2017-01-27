package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {

    TextView fname,lname,birthday,gender,phone,addr;
    Button edit;

    ImageView profile;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        fname = (TextView) findViewById(R.id.firstname);
        lname = (TextView) findViewById(R.id.lastname);
        birthday = (TextView) findViewById(R.id.birthday);
        gender = (TextView) findViewById(R.id.gender);
        phone = (TextView) findViewById(R.id.phone);
        addr = (TextView) findViewById(R.id.address);
        profile = (ImageView) findViewById(R.id.profile);

        edit = (Button) findViewById(R.id.btn_edit);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(ViewProfile.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {

                    Picasso.with(ViewProfile.this).load(user.profile).fit().centerCrop().into(profile);
                    fname.setText(user.firstname);
                    lname.setText(user.lastname);
                    birthday.setText(user.birthday);
                    gender.setText(user.gender);
                    phone.setText(user.phone);
                    addr.setText(user.address_number+" "+user.address_sub_district+", "+user.address_district+", "
                    +user.address_province+" "+user.address_code);
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
                startActivity(new Intent(ViewProfile.this, EditProfile.class));
                break;

        }
    }
}
