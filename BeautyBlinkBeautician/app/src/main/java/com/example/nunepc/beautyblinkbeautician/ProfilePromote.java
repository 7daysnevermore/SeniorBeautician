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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ProfilePromote extends AppCompatActivity {

    ImageView  addpromotepic1, addpromotepic2, addpromotepic3;
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
    TextView namepromote ,locationpromote ,pricepromote;

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
        locationpromote = (TextView) findViewById(R.id.namepromote);
        pricepromote = (TextView) findViewById(R.id.namepromote);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProfilePromote");

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(ProfilePromote.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    namepromote.setText(user.firstname+" "+user.lastname);
                    locationpromote.setText(user.address_number+" "+user.address_sub_district+", "+user.address_district+", "
                            +user.address_province+" "+user.address_code);
                    pricepromote.setText(user.birthday);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfilePromote.this, "Failed to load user information.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        addpromotepic1 = (ImageView) findViewById(R.id.addpromotepic1);
        addpromotepic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "1";
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });

        addpromotepic2 = (ImageView) findViewById(R.id.addpromotepic2);
        addpromotepic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "2";
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });

        addpromotepic3 = (ImageView) findViewById(R.id.addpromotepic3);
        addpromotepic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                pic = "3";
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });

        namepromote = (TextView) findViewById(R.id.namepromote);
        locationpromote = (TextView) findViewById(R.id.locationpromote);
        pricepromote = (TextView) findViewById(R.id.pricepromote);
        promotenow = (Button) findViewById(R.id.promotenow);

        promotenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPost();
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
    }

    private void startPost(){
        progressDialog.setMessage("Posting...");
        progressDialog.show();

        if(imageUri1!=null){
            filepath = storageReference.child("ProfilePromote").child(imageUri1.getLastPathSegment());
            putfile(imageUri1);
        }
        if(imageUri2!=null){
            filepath = storageReference.child("ProfilePromote").child(imageUri2.getLastPathSegment());
            putfile(imageUri2);
        }
        if(imageUri3!=null){
            filepath = storageReference.child("ProfilePromote").child(imageUri3.getLastPathSegment());
            putfile(imageUri3);
        }
    }

    private void putfile(Uri imageUri) {
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri dowloadUrl = taskSnapshot.getDownloadUrl();

                //create root of Promotion
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mPromotionRef = mRootRef.child("promotion");

                /*
                String key = mPromotionRef.push().getKey();

                final HashMap<String, Object> PromotionValues = new HashMap<>();
                PromotionValues.put("promotion", promotion);
                PromotionValues.put("image", dowloadUrl.toString());
                PromotionValues.put("price", price);
                PromotionValues.put("sale", sale);
                PromotionValues.put("datefrom", df_day + "/" + df_month + "/" + df_year);
                PromotionValues.put("dateto", dt_day + "/" + dt_month + "/" + dt_year);
                PromotionValues.put("details", details);
                PromotionValues.put("uid", mFirebaseUser.getUid().toString());
                PromotionValues.put("name", username);


                Map<String,Object> childUpdate = new HashMap<>();
                childUpdate.put("/promotion/"+key, PromotionValues);
                childUpdate.put("/beautician-promotion/"+mFirebaseUser.getUid().toString()+"/"+key, PromotionValues);

                mRootRef.updateChildren(childUpdate);

                //create root of Beautician-Promotion

                progressDialog.dismiss();

                startActivity(new Intent(ProfilePromote.this,Promotion.class));
                */
            }
        });
    }

}
