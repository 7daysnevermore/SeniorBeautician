package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.model.DataVerified;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 12/1/2560.
 */

public class Verified extends AppCompatActivity {

    private static final int SELECT_FILE = 1;
    Toolbar toolbar;
    Button verifynow, uploadimg1, uploadimg2, uploadimg3, uploadimg4, identity, certi_btn1, certi_btn2, certi_btn3;
    TextView identitytext, identitytext2, identitytext3, identitytext4, verifieduser, verifieduser1, verifieduser2, verifieduser3;
    ImageView citizenimg, certificate1, certificate2, certificate3, certimg1, certimg2, certimg3;
    String pic;

    private Uri imageUri1 = null;
    private Uri imageUri2 = null;
    private Uri imageUri3 = null;
    private Uri imageUri4 = null;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private StorageReference storageReference, filepath;
    private DatabaseReference databaseReference;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        //up button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        citizenimg = (ImageView) findViewById(R.id.identity_img);
        certimg1 = (ImageView) findViewById(R.id.certificate_img);
        certimg2 = (ImageView) findViewById(R.id.certificate_img2);
        certimg3 = (ImageView) findViewById(R.id.certificate_img3);

        certificate1 = (ImageView) findViewById(R.id.certificate_img);
        certificate2 = (ImageView) findViewById(R.id.certificate_img2);
        certificate3 = (ImageView) findViewById(R.id.certificate_img3);

        identitytext = (TextView) findViewById(R.id.identitytext);
        identitytext2 = (TextView) findViewById(R.id.cert1);
        identitytext3 = (TextView) findViewById(R.id.cert2);
        identitytext4 = (TextView) findViewById(R.id.cert3);

        identity = (Button) findViewById(R.id.identity);
        certi_btn1 = (Button) findViewById(R.id.certi_btn);

        verifieduser = (TextView) findViewById(R.id.verifieduser);
        verifieduser1 = (TextView) findViewById(R.id.verifieduser1);
        verifieduser2 = (TextView) findViewById(R.id.verifieduser2);
        verifieduser3 = (TextView) findViewById(R.id.verifieduser3);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        uploadimg1 = (Button) findViewById(R.id.identity);
        uploadimg2 = (Button) findViewById(R.id.certi_btn);
        uploadimg3 = (Button) findViewById(R.id.certi_btn2);
        uploadimg4 = (Button) findViewById(R.id.certi_btn3);

        uploadimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "1";
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });

        uploadimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "2";
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });

        uploadimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "3";
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });

        uploadimg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "4";
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });

        // DB call ref verified
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Verified");

        // listen if have pic in db = show pic
        mRootRef.child("beautician-verified").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataVerified verified = dataSnapshot.getValue(DataVerified.class);
                if (verified == null) {
                    Toast.makeText(Verified.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    if (verified.citizenid != null) {
                        identitytext.setTextColor(Color.parseColor("#91dc5a"));
                        identity.setVisibility(View.GONE);
                        verifieduser.setVisibility(View.VISIBLE);
                        citizenimg.setImageResource(R.mipmap.useradd_done);
                    }
                    if (verified.makeup != null) {
                        identitytext2.setTextColor(Color.parseColor("#91dc5a"));
                        certi_btn1.setVisibility(View.GONE);
                        verifieduser1.setVisibility(View.VISIBLE);
                        certimg1.setImageResource(R.mipmap.diploma_done);
                    }
                    if (verified.hairstyle != null) {
                        identitytext3.setTextColor(Color.parseColor("#91dc5a"));
                        certi_btn2.setVisibility(View.GONE);
                        verifieduser2.setVisibility(View.VISIBLE);
                        certimg2.setImageResource(R.mipmap.diploma_done);
                    }
                    if (verified.hairdressing != null) {
                        identitytext4.setTextColor(Color.parseColor("#91dc5a"));
                        certi_btn3.setVisibility(View.GONE);
                        verifieduser3.setVisibility(View.VISIBLE);
                        certimg3.setImageResource(R.mipmap.diploma_done);
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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && pic == "1") {
            imageUri1 = data.getData();
            citizenimg.setImageURI(imageUri1);
        }
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && pic == "2") {
            imageUri2 = data.getData();
            certificate1.setImageURI(imageUri2);
        }
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && pic == "3") {
            imageUri3 = data.getData();
            certificate2.setImageURI(imageUri3);
        }
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && pic == "4") {
            imageUri4 = data.getData();
            certificate3.setImageURI(imageUri4);
        }

        startPost();
    }

    private void startPost() {
        if (imageUri1 != null) {
            filepath = storageReference.child("Verified").child(imageUri1.getLastPathSegment());
            putfile(imageUri1, "1");
        }
        if (imageUri2 != null) {
            filepath = storageReference.child("Verified").child(imageUri2.getLastPathSegment());
            putfile(imageUri2, "2");
        }
        if (imageUri3 != null) {
            filepath = storageReference.child("Verified").child(imageUri3.getLastPathSegment());
            putfile(imageUri3, "3");
        }
        if (imageUri4 != null) {
            filepath = storageReference.child("Verified").child(imageUri3.getLastPathSegment());
            putfile(imageUri4, "4");
        }
    }

    private void putfile(Uri imageUri, final String pic) {
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                //create root of Promotion
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("beautician-verified").child(uid);

                final HashMap<String, Object> VerifiedValues = new HashMap<>();
                VerifiedValues.put("image", downloadUrl.toString());

                Map<String, Object> childUpdate = new HashMap<>();
                if (pic.equals("1")) {
                    mRootRef.child("citizenid").setValue(downloadUrl.toString());
                }
                if (pic.equals("2")) {
                    mRootRef.child("makeup").setValue(downloadUrl.toString());
                }
                if (pic.equals("3")) {
                    mRootRef.child("hairstyle").setValue(downloadUrl.toString());
                }
                if (pic.equals("4")) {
                    mRootRef.child("hairdressing").setValue(downloadUrl.toString());
                }
            }
        });
    }
}
