package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.model.DataVerified;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by CaptainPC on 29/3/2560.
 */

public class VerifiedShow extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    private ImageView identity_img,certificate_img,certificate_img2,certificate_img3;
    private TextView verifieduser,verifieduser1,verifieduser2,verifieduser3,identitytext,cert1,cert2,cert3;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String uid;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_show);

        //up button
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        identity_img = (ImageView) findViewById(R.id.identity_img);
        certificate_img = (ImageView) findViewById(R.id.certificate_img);
        certificate_img2 = (ImageView) findViewById(R.id.certificate_img2);
        certificate_img3 = (ImageView) findViewById(R.id.certificate_img3);
        verifieduser = (TextView) findViewById(R.id.verifieduser);
        verifieduser1 = (TextView) findViewById(R.id.verifieduser1);
        verifieduser2 = (TextView) findViewById(R.id.verifieduser2);
        verifieduser3 = (TextView) findViewById(R.id.verifieduser3);
        identitytext = (TextView) findViewById(R.id.identitytext);
        cert1 = (TextView) findViewById(R.id.cert1);
        cert2 = (TextView) findViewById(R.id.cert2);
        cert3 = (TextView) findViewById(R.id.cert3);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("beautician-verified").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataVerified verified = dataSnapshot.getValue(DataVerified.class);
                if (verified == null) {
                    Toast.makeText(VerifiedShow.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    if (verified.citizenid != null) {
                        identity_img.setImageResource(R.mipmap.useradd_done);
                        identitytext.setTextColor(Color.parseColor("#91dc5a"));
                        verifieduser.setText("Verified");
                    }
                    if (verified.makeup != null) {
                        certificate_img.setImageResource(R.mipmap.diploma_done);
                        cert1.setTextColor(Color.parseColor("#91dc5a"));
                        verifieduser1.setText("Verified");
                    }
                    if (verified.hairstyle != null) {
                        certificate_img2.setImageResource(R.mipmap.diploma_done);
                        cert2.setTextColor(Color.parseColor("#91dc5a"));
                        verifieduser2.setText("Verified");
                    }
                    if (verified.hairdressing != null) {
                        certificate_img3.setImageResource(R.mipmap.diploma_done);
                        cert3.setTextColor(Color.parseColor("#91dc5a"));
                        verifieduser3.setText("Verified");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // up button method
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                Intent intent = new Intent(VerifiedShow.this,StorePreview.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}
