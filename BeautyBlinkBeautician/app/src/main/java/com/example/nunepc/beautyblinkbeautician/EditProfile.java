package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 12/1/2560.
 */

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    EditText username,firstname, lastname, phone, address_number, address_building,
            address_sub_district, address_district, address_province, address_code;

    String latitude, longitude;

    Button editprofile;

    ImageView profilepicture;

    Toolbar toolbar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private int SELECT_FILE = 1;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        //up button
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        username = (EditText) findViewById(R.id.username);
        firstname = (EditText) findViewById(R.id.fname);
        lastname = (EditText) findViewById(R.id.lname);
        phone = (EditText) findViewById(R.id.phone);
        address_number = (EditText) findViewById(R.id.addressnum);
        address_building = (EditText) findViewById(R.id.building);
        address_sub_district = (EditText) findViewById(R.id.sub_district);
        address_district = (EditText) findViewById(R.id.district);
        address_province = (EditText) findViewById(R.id.province);
        address_code = (EditText) findViewById(R.id.code);

        editprofile = (Button) findViewById(R.id.btn_editprofile);

        profilepicture = (ImageView) findViewById(R.id.changeProfilePicture);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(EditProfile.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {

                    if (user.username != null){
                        username.setText(user.username);
                    }
                    if (user.firstname != null){
                        firstname.setText(user.firstname);
                    }
                    if (user.lastname != null){
                        lastname.setText(user.lastname);
                    }
                    if (user.phone != null){
                        phone.setText(user.phone);
                    }
                    if (user.address_number != null){
                        address_number.setText(user.address_number);
                    }
                    if (user.building != null){
                        address_building.setText(user.building);
                    }
                    if (user.address_sub_district != null){
                        address_sub_district.setText(user.address_sub_district);
                    }
                    if (user.address_district != null){
                        address_district.setText(user.address_district);
                    }
                    if (user.address_province != null){
                        address_province.setText(user.address_province);
                    }
                    if (user.address_code != null){
                        address_code.setText(user.address_code);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        editprofile.setOnClickListener(this);

    }

    // up button method
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

        switch (v.getId()) {
            case R.id.btn_editprofile:
                Editprofile();
                break;

            case R.id.changeProfilePicture:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,SELECT_FILE);
                break;
        }

    }

    public void Editprofile(){

        final String user = username.getText().toString().toLowerCase();
        final String addr_building = address_building.getText().toString();
        final String fname = firstname.getText().toString().toLowerCase();
        final String lname = lastname.getText().toString().toLowerCase();
        final String in_phone = phone.getText().toString();
        final String addr_num = address_number.getText().toString();
        final String addr_s_dist = address_sub_district.getText().toString();
        final String addr_dist = address_district.getText().toString();
        final String addr_province = address_province.getText().toString();
        final String addr_code = address_code.getText().toString();

        //Get current to pull UID and email
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //create root of Beautician
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mUsersRef = mRootRef.child("beautician");

        HashMap<String, Object> UserUpdate = new HashMap<>();
        UserUpdate.put("username", user);
        UserUpdate.put("firstname", fname);
        UserUpdate.put("lastname", lname);
        UserUpdate.put("phone", in_phone);
        UserUpdate.put("address_number", addr_num);
        UserUpdate.put("building", addr_building);
        UserUpdate.put("address_sub_district", addr_s_dist);
        UserUpdate.put("address_district", addr_dist);
        UserUpdate.put("address_province", addr_province);
        UserUpdate.put("address_code", addr_code);
        UserUpdate.put("latitude", latitude);
        UserUpdate.put("longitude", longitude);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(mFirebaseUser.getUid(), UserUpdate);

        mUsersRef.updateChildren(childUpdates);

        startActivity(new Intent(EditProfile.this, ViewProfile.class));

    }
}