package com.example.nunepc.beautyblinkbeautician;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ViewServices extends AppCompatActivity implements View.OnClickListener {

    TextView price_s01,price_s02,price_s03,price_s04;
    LinearLayout show_s01,show_s02,show_s03,show_s04,addmoreservice;
    ImageView edit;
    private Toolbar toolbar;

    String price01,price02,price03,price04;

    ArrayList<String> serviceno;

    TextView editprice_01,editprice_02,editprice_03,editprice_04;
    ImageView edits01,edits02,edits03,edits04,editeds01,editeds02,editeds03,editeds04;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showservice);

        //up button
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        editprice_01 = (TextView) findViewById(R.id.editprice_s01);
        editprice_02 = (TextView) findViewById(R.id.editprice_s02);
        editprice_03 = (TextView) findViewById(R.id.editprice_s03);
        editprice_04 = (TextView) findViewById(R.id.editprice_s04);

        edits01 = (ImageView) findViewById(R.id.btn_edit01);
        edits02 = (ImageView) findViewById(R.id.btn_edit02);
        edits03 = (ImageView) findViewById(R.id.btn_edit03);
        edits04 = (ImageView) findViewById(R.id.btn_edit04);
        editeds01 = (ImageView) findViewById(R.id.btn_edited01);
        editeds02 = (ImageView) findViewById(R.id.btn_edited02);
        editeds03 = (ImageView) findViewById(R.id.btn_edited03);
        editeds04 = (ImageView) findViewById(R.id.btn_edited04);

        addmoreservice = (LinearLayout) findViewById(R.id.addmoreservice);

        serviceno = new ArrayList<>();

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
                    serviceno.add("S01");
                } else {
                    show_s01.setVisibility(View.VISIBLE);
                    price01 = Long.toString(services.price);
                    price_s01.setText(price01+" Bath");
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
                    serviceno.add("S02");
                } else {
                    show_s02.setVisibility(View.VISIBLE);
                    price02 = Long.toString(services.price);
                    price_s02.setText(price02+" Bath");
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
                    serviceno.add("S03");
                } else {
                    show_s03.setVisibility(View.VISIBLE);
                    price03 = Long.toString(services.price);
                    price_s03.setText(price03+" Bath");
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
                    serviceno.add("S04");
                } else {
                    show_s04.setVisibility(View.VISIBLE);
                    price04 = Long.toString(services.price);
                    price_s04.setText(price04+" Bath");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        edits01.setOnClickListener(this);
        edits02.setOnClickListener(this);
        edits03.setOnClickListener(this);
        edits04.setOnClickListener(this);
        addmoreservice.setOnClickListener(this);

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
            case R.id.btn_edit01:
                price_s01.setVisibility(View.GONE);
                edits01.setVisibility(View.GONE);
                editeds01.setVisibility(View.VISIBLE);
                editprice_01.setVisibility(View.VISIBLE);
                editprice_01.setText(price01);
                changePrice("S01");
                break;

            case R.id.btn_edit02:
                price_s02.setVisibility(View.GONE);
                edits02.setVisibility(View.GONE);
                editeds02.setVisibility(View.VISIBLE);
                editprice_02.setVisibility(View.VISIBLE);
                editprice_02.setText(price02);
                changePrice("S02");
                break;

            case R.id.btn_edit03:
                price_s03.setVisibility(View.GONE);
                edits03.setVisibility(View.GONE);
                editeds03.setVisibility(View.VISIBLE);
                editprice_03.setVisibility(View.VISIBLE);
                editprice_03.setText(price03);
                changePrice("S03");
                break;

            case R.id.btn_edit04:
                price_s04.setVisibility(View.GONE);
                edits04.setVisibility(View.GONE);
                editeds04.setVisibility(View.VISIBLE);
                editprice_04.setVisibility(View.VISIBLE);
                editprice_04.setText(price04);
                changePrice("S04");
                break;

            case R.id.addmoreservice:

                    Intent intent = new Intent(ViewServices.this, AddService.class);
                    intent.putExtra("serviceno",serviceno);
                    startActivity(intent);

                break;


        }
    }

    private void changePrice(final String key){


        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mServiceRef = mRootRef.child("beautician-service/"+mFirebaseUser.getUid());

        DatabaseReference mProfilePromoteRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mProRef = mProfilePromoteRef.child("beautician-profilepromote/"+mFirebaseUser.getUid());
        mProRef.orderByChild("uid").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String keypro = null;

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    keypro = childSnapshot.getKey();
                }

                if(!keypro.equals(null)){

                    //Add to profile promote
                    final DatabaseReference mPromoteRef = mRootRef.child("profilepromote").child(keypro);
                    final DatabaseReference mPromoteRefB = mRootRef.child("beautician-profilepromote/").child(mFirebaseUser.getUid()).child(keypro);

                    if(key.equals("S01")){

                        editeds01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String price_01 = editprice_01.getText().toString();
                                final Integer price = Integer.parseInt(price_01);

                                HashMap<String, Object> ServiceValues = new HashMap<>();
                                ServiceValues.put("price", price);
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("S01", ServiceValues);

                                mServiceRef.updateChildren(childUpdates);

                                mPromoteRef.child("S01").setValue(price);
                                mPromoteRefB.child("S01").setValue(price);

                                price_s01.setVisibility(View.VISIBLE);
                                edits01.setVisibility(View.VISIBLE);
                                editeds01.setVisibility(View.GONE);
                                editprice_01.setVisibility(View.GONE);

                                Intent intent = new Intent(ViewServices.this, ViewServices.class);
                                startActivity(intent);

                            }
                        });

                    }
                    if (key.equals("S02")) {

                        editeds02.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String price_02 = editprice_02.getText().toString();
                                final Integer price = Integer.parseInt(price_02);

                                HashMap<String, Object> ServiceValues = new HashMap<>();
                                ServiceValues.put("price", price);
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("S02", ServiceValues);

                                mServiceRef.updateChildren(childUpdates);

                                mPromoteRef.child("S02").setValue(price);
                                mPromoteRefB.child("S02").setValue(price);

                                price_s02.setVisibility(View.VISIBLE);
                                edits02.setVisibility(View.VISIBLE);
                                editeds02.setVisibility(View.GONE);
                                editprice_02.setVisibility(View.GONE);

                                Intent intent = new Intent(ViewServices.this, ViewServices.class);
                                startActivity(intent);

                            }
                        });

                    }
                    if (key.equals("S03")) {

                        editeds03.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String price_03 = editprice_03.getText().toString();
                                final Integer price = Integer.parseInt(price_03);

                                HashMap<String, Object> ServiceValues = new HashMap<>();
                                ServiceValues.put("price", price);
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("S03", ServiceValues);

                                mServiceRef.updateChildren(childUpdates);

                                mPromoteRef.child("S03").setValue(price);
                                mPromoteRefB.child("S03").setValue(price);

                                price_s03.setVisibility(View.VISIBLE);
                                edits03.setVisibility(View.VISIBLE);
                                editeds03.setVisibility(View.GONE);
                                editprice_03.setVisibility(View.GONE);


                                Intent intent = new Intent(ViewServices.this, ViewServices.class);
                                startActivity(intent);

                            }
                        });

                    }
                    if (key.equals("S04")) {

                        editeds04.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String price_04 = editprice_04.getText().toString();
                                final Integer price = Integer.parseInt(price_04);

                                HashMap<String, Object> ServiceValues = new HashMap<>();
                                ServiceValues.put("price", price);
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("S04", ServiceValues);

                                mServiceRef.updateChildren(childUpdates);

                                mPromoteRef.child("S04").setValue(price);
                                mPromoteRefB.child("S04").setValue(price);

                                price_s04.setVisibility(View.VISIBLE);
                                edits04.setVisibility(View.VISIBLE);
                                editeds04.setVisibility(View.GONE);
                                editprice_04.setVisibility(View.GONE);

                                Intent intent = new Intent(ViewServices.this, ViewServices.class);
                                startActivity(intent);

                            }
                        });

                    }
                }


        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
}
