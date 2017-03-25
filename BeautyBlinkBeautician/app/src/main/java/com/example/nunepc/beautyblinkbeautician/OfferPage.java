package com.example.nunepc.beautyblinkbeautician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.fragment.RequestFragment;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OfferPage extends AppCompatActivity {

    private TextView date, service, event, time, special, location, maxprice, numofPer, name;
    HashMap<String, Object> requestValues;
    private EditText offer_price,offer_time,offer_location;
    private ImageView btnOffer;
    private String username,beaupic;
    String uid;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ProgressDialog progressDialog;
    private Button accept, decline, send_offer;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);

        //up button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("request");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        date = (TextView) findViewById(R.id.cusD);
        service = (TextView) findViewById(R.id.cusSer);
        event = (TextView) findViewById(R.id.cusEv);
        time = (TextView) findViewById(R.id.cusTime);
        special = (TextView) findViewById(R.id.cusSpe);
        location = (TextView) findViewById(R.id.cusLo);
        maxprice = (TextView) findViewById(R.id.cusMax);
        name = (TextView) findViewById(R.id.tname);
        numofPer = (TextView) findViewById(R.id.cusNum);
        //spe_b = (EditText)findViewById(R.id.speB);

        offer_price = (EditText) findViewById(R.id.offer_price);
        offer_time = (EditText) findViewById(R.id.offer_time);
        offer_location = (EditText) findViewById(R.id.offer_location);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        uid = mFirebaseUser.getUid().toString();
        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                } else {
                    username = user.username;
                    beaupic = user.profile;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        date = (TextView) findViewById(R.id.cusD);
        service = (TextView) findViewById(R.id.cusSer);
        event = (TextView) findViewById(R.id.cusEv);
        time = (TextView) findViewById(R.id.cusTime);
        special = (TextView) findViewById(R.id.cusSpe);
        location = (TextView) findViewById(R.id.cusLo);
        maxprice = (TextView) findViewById(R.id.cusMax);
        name = (TextView) findViewById(R.id.tname);
        numofPer = (TextView) findViewById(R.id.cusNum);
        offer_price = (EditText)findViewById(R.id.offer_price);
        offer_time = (EditText) findViewById(R.id.offer_time);
        offer_location = (EditText) findViewById(R.id.offer_location);


        date.setText(requestValues.get("date").toString());
        service.setText(requestValues.get("service").toString());
        event.setText(requestValues.get("event").toString());
        numofPer.setText(requestValues.get("numberofperson").toString());
        maxprice.setText(requestValues.get("maxprice").toString());
        time.setText(requestValues.get("time").toString());
        location.setText(requestValues.get("location").toString());
        special.setText(requestValues.get("specialrequest").toString());
        name.setText(requestValues.get("name").toString());

        btnOffer = (ImageView) findViewById(R.id.btn_offer);

        accept = (Button) findViewById(R.id.accept);
        decline = (Button) findViewById(R.id.decline);
        final LinearLayout linear = (LinearLayout) findViewById(R.id.offer_detail);
        send_offer = (Button) findViewById(R.id.send_offer);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.accept:
                        linear.setVisibility(View.VISIBLE);
                        offer_price.requestFocus();
                        break;
                }
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.decline:
                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("beautician-received").child(uid);
                        mRootRef.child(requestValues.get("key").toString()).removeValue();
                        Intent intent = new Intent(OfferPage.this,MainActivity.class);
                        intent.putExtra("menu","request");
                        startActivity(intent);
                        break;
                }
            }
        });

        send_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input_offer_price = offer_price.getText().toString();
                final String input_offer_time = offer_time.getText().toString();
                final String input_offer_location = offer_location.getText().toString();

                if (!TextUtils.isEmpty(input_offer_price)) {
                    //Create root of Request
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mRequestRef = mRootRef.child("offer1");

                    String key = mRequestRef.push().getKey();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat currenttime = new SimpleDateFormat("MMM dd yyyy hh:mm a");
                    String dateform = currenttime.format(c.getTime());

                    final HashMap<String, Object> RequestValues = new HashMap<String, Object>();
                    RequestValues.put("offerid", key);
                    RequestValues.put("requestid", requestValues.get("key").toString());
                    RequestValues.put("service", requestValues.get("service").toString());
                    RequestValues.put("event", requestValues.get("event").toString());
                    RequestValues.put("username", requestValues.get("name").toString());
                    if(requestValues.get("profilecust")!=null){
                        RequestValues.put("userprofile", requestValues.get("profilecust").toString());
                    }
                    RequestValues.put("beauname", username);
                    RequestValues.put("beauprofile", beaupic);
                    RequestValues.put("numberofperson", requestValues.get("numberofperson").toString());
                    RequestValues.put("price", input_offer_price);
                    RequestValues.put("amount", Integer.parseInt(input_offer_price) * Integer.parseInt(requestValues.get("numberofperson").toString()));
                    RequestValues.put("date", requestValues.get("numberofperson").toString());
                    if (input_offer_time.equals(null)) {
                        RequestValues.put("time", input_offer_time);
                    } else {
                        RequestValues.put("time", requestValues.get("time").toString());
                    }
                    if (input_offer_location.equals(null)) {
                        RequestValues.put("location", input_offer_location);
                    } else {
                        RequestValues.put("location", requestValues.get("location").toString());
                    }
                    RequestValues.put("specialrequest", requestValues.get("specialrequest").toString());
                    RequestValues.put("status", "2");
                    RequestValues.put("customerid", requestValues.get("custid").toString());
                    RequestValues.put("currenttime", dateform);
                    RequestValues.put("reqpic", requestValues.get("requestpic").toString());
                    RequestValues.put("offerpic", "");
                    RequestValues.put("beauid", mFirebaseUser.getUid().toString());

                    Map<String, Object> childUpdate = new HashMap<>();
                    //childUpdate.put("/request/"+ke,changestatus);
                    childUpdate.put("/offer1/" + key, RequestValues);
                    childUpdate.put("/beautician-offer1/" + mFirebaseUser.getUid().toString() + "/" + requestValues.get("key").toString() + "/" + key, RequestValues);
                    childUpdate.put("/customer-received/" + requestValues.get("custid").toString() + "/" + requestValues.get("key").toString() + "/" + key, RequestValues);
                    mRootRef.updateChildren(childUpdate);

                    final DatabaseReference mCustReqRef = mRootRef.child("customer-request1").child(requestValues.get("custid").toString()).child(requestValues.get("key").toString());
                    final DatabaseReference mBeauReqRef = mRootRef.child("beautician-received").child(mFirebaseUser.getUid().toString()).child(requestValues.get("key").toString());

                    mCustReqRef.child("status").setValue("2");
                    mBeauReqRef.child("status").setValue("2");
                    // progressDialog.dismiss();
                    Intent intent2 = new Intent(OfferPage.this, MainActivity.class);
                    intent2.putExtra("menu", "request");
                    startActivity(intent2);
                }
            }
        });




    }

    // up button method
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                startActivity(new Intent(OfferPage.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
