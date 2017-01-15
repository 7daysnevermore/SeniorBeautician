package com.example.nunepc.beautyblinkbeautician;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PromotionDetails extends AppCompatActivity implements View.OnClickListener {

    HashMap<String, Object> promotionValues;
    private TextView proTopic,proPrice,proSale,proDF,proDT,proDetails ;
    ImageView proImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpromotion);

        promotionValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("promotion");

        proTopic = (TextView)findViewById(R.id.proTopic);
        proPrice = (TextView) findViewById(R.id.proPrice);
        proSale = (TextView) findViewById(R.id.proSale);
        proDF = (TextView) findViewById(R.id.proDF);
        proDT = (TextView) findViewById(R.id.proDT);
        proDetails = (TextView) findViewById(R.id.proDetails);
        proImg = (ImageView) findViewById(R.id.proImg);

        proTopic.setText(promotionValues.get("promotion").toString());
        proPrice.setText(promotionValues.get("price").toString()+" Bath");
        proPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        proSale.setText(promotionValues.get("sale").toString()+" Bath");
        proDF.setText("Date from : "+promotionValues.get("dateFrom").toString());
        proDT.setText("Date to     : "+promotionValues.get("dateTo").toString());
        proDetails.setText(promotionValues.get("details").toString());

        Picasso.with(this).load(promotionValues.get("image").toString()).into(proImg);

        findViewById(R.id.btn_editpromotion).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_editpromotion:

                Intent cPro = new Intent(PromotionDetails.this,EditPromotion.class);
                cPro.putExtra("promotion",  promotionValues);
                startActivity(cPro);

                break;

        }

    }
}