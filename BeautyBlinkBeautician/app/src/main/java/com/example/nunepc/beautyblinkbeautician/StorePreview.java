package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewDetailFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewGalleryFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewPlannerFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewReviewFragment;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by CaptainPC on 15/1/2560.
 */

public class StorePreview extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference databaseReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String uid;
    TextView previewname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storepreview);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        previewname = (TextView) findViewById(R.id.previewname);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(StorePreview.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    previewname.setText(user.username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StorePreview.this, "Failed to load user information.",
                        Toast.LENGTH_SHORT).show();
            }
        });

            //fragment
            if(savedInstanceState==null){
                //first create
                //Place fragment
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.previewContainer,new PreviewGalleryFragment())
                        .commit();

            }

        //tab button
        findViewById(R.id.bt_gallery).setOnClickListener(this);
        findViewById(R.id.bt_review).setOnClickListener(this);
        findViewById(R.id.bt_planner).setOnClickListener(this);
        findViewById(R.id.bt_detail).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gallery:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewGalleryFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_review:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewReviewFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_planner:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewPlannerFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_detail:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewDetailFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
