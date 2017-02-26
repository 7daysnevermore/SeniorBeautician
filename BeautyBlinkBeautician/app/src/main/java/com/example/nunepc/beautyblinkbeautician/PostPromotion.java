package com.example.nunepc.beautyblinkbeautician;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PostPromotion extends AppCompatActivity {

    private int REQUEST_CAMERA =0,SELECT_FILE=1;
    private Toolbar toolbar;
    private ImageView addPicture;
    private EditText input_promotion,input_price,input_sale,input_df_day,input_df_month,input_df_year,
            input_dt_day,input_dt_month,input_dt_year,input_details;
    private TextView input_name;
    String username,input_service,serviceID,profile;
    private ImageView btn_createpromotion;
    private Integer[] mThumbsId;
    private Uri imageUri = null;
    private ProgressDialog progressDialog;
    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;
    private RadioGroup radioGroup_service;
    private RadioButton button_service;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_promotion);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("beautician").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(PostPromotion.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    username = user.username;
                    profile = user.profile;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotion");

        progressDialog = new ProgressDialog(this);
        addPicture = (ImageView) findViewById(R.id.addPic);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });

        input_promotion= (EditText)findViewById(R.id.promotion);
        input_price = (EditText) findViewById(R.id.price);
        input_sale = (EditText) findViewById(R.id.sale);
        input_df_day = (EditText) findViewById(R.id.dateFrom_day);
        input_df_month = (EditText) findViewById(R.id.dateFrom_month);
        input_df_year = (EditText) findViewById(R.id.dateFrom_year);
        input_dt_day = (EditText) findViewById(R.id.dateTo_day);
        input_dt_month = (EditText) findViewById(R.id.dateTo_month);
        input_dt_year = (EditText) findViewById(R.id.dateTo_year);
        input_details = (EditText) findViewById(R.id.details);

        btn_createpromotion = (ImageView)findViewById(R.id.btn_createpromotion);

        btn_createpromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPost();
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
    protected  void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){
            imageUri = data.getData();
            addPicture.setImageURI(imageUri);

        }

    }
    private void startPost(){

        progressDialog.setMessage("Posting...");
        progressDialog.show();

        radioGroup_service = (RadioGroup) findViewById(R.id.service);
        int selectedId = radioGroup_service.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        button_service = (RadioButton) findViewById(selectedId);
        input_service = button_service.getText().toString();


        if(input_service.equals("MakeupandHair")){
            serviceID ="S01";
        }
        if(input_service.equals("Makeup")){
            serviceID ="S02";
        }
        if(input_service.equals("Hairstyle")){
            serviceID ="S03";
        }
        if(input_service.equals("Hairdressing")){
            serviceID ="S04";
        }

        final String promotion = input_promotion.getText().toString();
        final String price = input_price.getText().toString();
        final String sale = input_sale.getText().toString();
        final String df_day = input_df_day.getText().toString();
        final String df_month = input_df_month.getText().toString();
        final String df_year = input_df_year.getText().toString();
        final String dt_day = input_dt_day.getText().toString();
        final String dt_month = input_dt_month.getText().toString();
        final String dt_year = input_dt_year.getText().toString();
        final String details = input_details.getText().toString();

        if(!TextUtils.isEmpty(promotion) && !TextUtils.isEmpty(price) &&
                !TextUtils.isEmpty(sale) && !TextUtils.isEmpty(df_day) &&
                !TextUtils.isEmpty(df_month) && !TextUtils.isEmpty(df_year) &&
                !TextUtils.isEmpty(dt_day) && !TextUtils.isEmpty(dt_month) &&
                !TextUtils.isEmpty(dt_year) && !TextUtils.isEmpty(details) && imageUri != null){

            filepath = storageReference.child("Promotion").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri dowloadUrl = taskSnapshot.getDownloadUrl();

                    //create root of Promotion
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mPromotionRef = mRootRef.child("promotion");

                    String key = mPromotionRef.push().getKey();

                    final HashMap<String, Object> PromotionValues = new HashMap<>();
                    PromotionValues.put("promotion", promotion);
                    PromotionValues.put("image", dowloadUrl.toString());
                    PromotionValues.put("service",serviceID);
                    PromotionValues.put("price", price);
                    PromotionValues.put("sale", sale);
                    PromotionValues.put("datefrom", df_day + "/" + df_month + "/" + df_year);
                    PromotionValues.put("dateto", dt_day + "/" + dt_month + "/" + dt_year);
                    PromotionValues.put("details", details);
                    PromotionValues.put("uid", mFirebaseUser.getUid().toString());
                    PromotionValues.put("name", username);
                    PromotionValues.put("status", "active");
                    PromotionValues.put("profile", profile);


                    Map<String,Object> childUpdate = new HashMap<>();
                    childUpdate.put("/promotion/"+key, PromotionValues);
                    childUpdate.put("/beautician-promotion/"+mFirebaseUser.getUid().toString()+"/"+key, PromotionValues);

                    mRootRef.updateChildren(childUpdate);

                    //create root of Beautician-Promotion

                    progressDialog.dismiss();


                    startActivity(new Intent(PostPromotion.this,Promotion.class));

                }
            });
        }//end if


    }
    }

