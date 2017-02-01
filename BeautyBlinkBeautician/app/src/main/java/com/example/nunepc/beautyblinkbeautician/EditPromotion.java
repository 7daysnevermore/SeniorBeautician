package com.example.nunepc.beautyblinkbeautician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nunepc.beautyblinkbeautician.fragment.SettingFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 16/1/2560.
 */

public class EditPromotion extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ProgressDialog progressDialog;
    private StorageReference storageReference, filepath;
    private DatabaseReference databaseReference;

    HashMap<String, Object> promotionValues;
    private ImageView addPicture, editImage;
    private EditText input_promotion, input_price, input_sale, input_df_day, input_df_month, input_df_year,
            input_dt_day, input_dt_month, input_dt_year, input_details;

    private Uri imageUri = null;

    String[] dateF, dateT;

    boolean change = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpromotion);

        promotionValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("promotion");

        dateF = promotionValues.get("dateFrom").toString().split("/");
        dateT = promotionValues.get("dateTo").toString().split("/");

        input_promotion = (EditText) findViewById(R.id.promotion);
        input_price = (EditText) findViewById(R.id.price);
        input_sale = (EditText) findViewById(R.id.sale);
        input_df_day = (EditText) findViewById(R.id.dateFrom_day);
        input_df_month = (EditText) findViewById(R.id.dateFrom_month);
        input_df_year = (EditText) findViewById(R.id.dateFrom_year);
        input_dt_day = (EditText) findViewById(R.id.dateTo_day);
        input_dt_month = (EditText) findViewById(R.id.dateTo_month);
        input_dt_year = (EditText) findViewById(R.id.dateTo_year);
        input_details = (EditText) findViewById(R.id.details);

        input_promotion.setText(promotionValues.get("promotion").toString());
        input_price.setText(promotionValues.get("price").toString());
        input_sale.setText(promotionValues.get("sale").toString());
        input_df_day.setText(dateF[0]);
        input_df_month.setText(dateF[1]);
        input_df_year.setText(dateF[2]);
        input_dt_day.setText(dateT[0]);
        input_dt_month.setText(dateT[1]);
        input_dt_year.setText(dateT[2]);
        input_details.setText(promotionValues.get("details").toString());

        addPicture = (ImageView) findViewById(R.id.addPic);
        editImage = (ImageView) findViewById(R.id.editimage) ;

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotion");

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change = true;
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });

        Picasso.with(this).load(promotionValues.get("image").toString()).into(addPicture);

        findViewById(R.id.btn_editpromotion).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            addPicture.setImageURI(imageUri);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_editpromotion:
                editPromotion();
                break;
        }
    }

    public void editPromotion() {

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


        if (!TextUtils.isEmpty(promotion) && !TextUtils.isEmpty(price) &&
                !TextUtils.isEmpty(sale) && !TextUtils.isEmpty(df_day) &&
                !TextUtils.isEmpty(df_month) && !TextUtils.isEmpty(df_year) &&
                !TextUtils.isEmpty(dt_day) && !TextUtils.isEmpty(dt_month) &&
                !TextUtils.isEmpty(dt_year) && !TextUtils.isEmpty(details) && imageUri != null) {

            filepath = storageReference.child("Promotion").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri dowloadUrl = taskSnapshot.getDownloadUrl();

                    //create root of Promotion
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


                    final HashMap<String, Object> PromotionValues = new HashMap<>();
                    PromotionValues.put("service",promotionValues.get("service"));
                    PromotionValues.put("status", "active");
                    PromotionValues.put("promotion", promotion);
                    PromotionValues.put("image", dowloadUrl.toString());
                    PromotionValues.put("price", price);
                    PromotionValues.put("sale", sale);
                    PromotionValues.put("datefrom", df_day + "/" + df_month + "/" + df_year);
                    PromotionValues.put("dateto", dt_day + "/" + dt_month + "/" + dt_year);
                    PromotionValues.put("details", details);
                    PromotionValues.put("uid", promotionValues.get("uid"));
                    PromotionValues.put("name", promotionValues.get("name"));


                    Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put("/promotion/" + promotionValues.get("key"), PromotionValues);
                    childUpdate.put("/beautician-promotion/" + promotionValues.get("uid") + "/" + promotionValues.get("key"), PromotionValues);

                    mRootRef.updateChildren(childUpdate);

                    //create root of Beautician-Promotion



                    startActivity(new Intent(EditPromotion.this, Promotion.class));
                }
            });


        }
    }



}
