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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ProfilePromote extends AppCompatActivity {

    ImageView  addpromotepic1, addpromotepic2, addpromotepic3;
    TextView namepromote, locationpromote, pricepromote;
    private ProgressDialog progressDialog;
    String pic,pic2,pic3;
    Button promotenow;
    private Uri imageUri1 = null;
    private Uri imageUri2 = null;
    private Uri imageUri3 = null;

    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepromote);

        progressDialog = new ProgressDialog(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProfilePromote");

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

        if(imageUri1!=null && imageUri2!=null && imageUri3!=null){
            filepath = storageReference.child("ProfilePromote").child(imageUri1.getLastPathSegment());
            filepath = storageReference.child("ProfilePromote").child(imageUri2.getLastPathSegment());
            filepath = storageReference.child("ProfilePromote").child(imageUri3.getLastPathSegment());


        }
    }

}
