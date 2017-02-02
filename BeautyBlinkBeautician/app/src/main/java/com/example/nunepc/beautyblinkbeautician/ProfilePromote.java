package com.example.nunepc.beautyblinkbeautician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.model.DataProfilePromote;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ProfilePromote extends AppCompatActivity {

    ImageView  addpromotepic1, addpromotepic2, addpromotepic3,profile;
    private ProgressDialog progressDialog;
    String pic;
    Button promotenow;
    private Uri imageUri1 = null;
    private Uri imageUri2 = null;
    private Uri imageUri3 = null;

    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String uid;
    TextView namepromote ,locationpromote ,priceS01,priceS02,priceS03,priceS04;

    private int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepromote);

        progressDialog = new ProgressDialog(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        namepromote = (TextView) findViewById(R.id.namepromote);
        locationpromote = (TextView) findViewById(R.id.locationpromote);
        priceS01 = (TextView) findViewById(R.id.priceS01);
        priceS02 = (TextView) findViewById(R.id.priceS02);
        priceS03 = (TextView) findViewById(R.id.priceS03);
        priceS04 = (TextView) findViewById(R.id.priceS04);

        addpromotepic1 = (ImageView) findViewById(R.id.addpromotepic1);
        addpromotepic2 = (ImageView) findViewById(R.id.addpromotepic2);
        addpromotepic3 = (ImageView) findViewById(R.id.addpromotepic3);
        profile = (ImageView) findViewById(R.id.picpromote);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProfilePromote");

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("profilepromote").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    DataProfilePromote promote = data.getValue(DataProfilePromote.class);

                    if (promote == null) {
                        Toast.makeText(ProfilePromote.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                    } else {

                        namepromote.setText(promote.name);
                        locationpromote.setText(promote.district+promote.province+" ...");

                        if(!promote.BeauticianProfile.equals("")){
                            Picasso.with(ProfilePromote.this).load(promote.BeauticianProfile).into(profile);
                        }

                        if(!promote.picture1.equals("")){
                            Picasso.with(ProfilePromote.this).load(promote.picture1).into(addpromotepic1);
                        }
                        if(!promote.picture2.equals("")){
                            Picasso.with(ProfilePromote.this).load(promote.picture2).into(addpromotepic2);
                        }
                        if(!promote.picture3.equals("")){
                            Picasso.with(ProfilePromote.this).load(promote.picture3).into(addpromotepic3);
                        }
                    if(promote.S01 != 0){
                        priceS01.setText("Hair and Makeup : "+Long.toString(promote.S01)+"B");
                    }
                    if(promote.S02 != 0){
                        priceS02.setText("Makeup          : "+Long.toString(promote.S02)+"B");
                    }
                    if(promote.S03 != 0){
                        priceS03.setText("Hairstyle               : "+Long.toString(promote.S03)+"B");
                    }
                    if(promote.S04 != 0){
                        priceS04.setText("Hairdressing : "+Long.toString(promote.S04)+"B");
                    }
                    }

            }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfilePromote.this, "Failed to load user information.",
                        Toast.LENGTH_SHORT).show();
            }
        });


        addpromotepic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "1";
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });


        addpromotepic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "2";
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });


        addpromotepic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "3";
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK && pic=="1" ){
            imageUri1 = data.getData();
            addpromotepic1.setImageURI(imageUri1);
        }
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK && pic=="2" ){
            imageUri2 = data.getData();
            addpromotepic2.setImageURI(imageUri2);
        }
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK && pic=="3"  ){
            imageUri3 = data.getData();
            addpromotepic3.setImageURI(imageUri3);
        }

        startPost();
    }

    private void startPost(){
        if(imageUri1!=null){
            filepath = storageReference.child("ProfilePromote").child(imageUri1.getLastPathSegment());
            putfile(imageUri1, "1");
        }
        if(imageUri2!=null){
            filepath = storageReference.child("ProfilePromote").child(imageUri2.getLastPathSegment());
            putfile(imageUri2, "2");
        }
        if(imageUri3!=null){
            filepath = storageReference.child("ProfilePromote").child(imageUri3.getLastPathSegment());
            putfile(imageUri3, "3");
        }
    }

    private void putfile(Uri imageUri, final String pic) {
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                //create root of BeauticianService
                final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                //create root of ProfilePromote
                DatabaseReference mProfilePromoteRef = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference mProRef = mProfilePromoteRef.child("beautician-profilepromote/" + mFirebaseUser.getUid());
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

                            if (pic.equals("1")){
                                mPromoteRef.child("picture1").setValue(downloadUrl.toString());
                                mPromoteRefB.child("picture1").setValue(downloadUrl.toString());
                            }
                            if (pic.equals("2")){
                                mPromoteRef.child("picture2").setValue(downloadUrl.toString());
                                mPromoteRefB.child("picture2").setValue(downloadUrl.toString());
                            }
                            if (pic.equals("3")){
                                mPromoteRef.child("picture3").setValue(downloadUrl.toString());
                                mPromoteRefB.child("picture3").setValue(downloadUrl.toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        });
    }

}
