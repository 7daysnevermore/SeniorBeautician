package com.example.nunepc.beautyblinkbeautician;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class PostPromotion extends AppCompatActivity {

    private int REQUEST_CAMERA =0,SELECT_FILE=1;
    private Toolbar toolbar;
    private ImageView addPicture;
    private EditText input_promotion,input_price,input_sale,input_df_day,input_df_month,input_df_year,
            input_dt_day,input_dt_month,input_dt_year,input_details;
    private TextView input_name;
    String username;
    private ImageView btn_createpromotion;
    private Integer[] mThumbsId;
    private Uri imageUri = null;
    private ProgressDialog progressDialog;
    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

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


                    startActivity(new Intent(PostPromotion.this,Promotion.class));
                }
            });
        }//end if


    }
    }

