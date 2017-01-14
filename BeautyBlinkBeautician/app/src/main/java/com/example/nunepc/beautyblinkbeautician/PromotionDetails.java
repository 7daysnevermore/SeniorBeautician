package com.example.nunepc.beautyblinkbeautician;


import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.model.DataPromotion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Map;


public class PromotionDetails extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private TextView techTopic,techDetail,techPrice,techSale,techDT,techDF ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpromotion);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        String cshow = getIntent().getExtras().getString("to_show");
       // Toast.makeText(PromotionDetails.this, cshow,Toast.LENGTH_LONG).show();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mPromotionRef = mRootRef.child("beautician-promotion"+"/"+mFirebaseUser.getUid().toString());
        techTopic = (TextView)findViewById(R.id.proTopic);
        mPromotionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map)dataSnapshot.getValue();
                String mTop= map.get("promotion");
                techTopic.setText(mTop);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}