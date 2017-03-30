package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.model.DataOffer;
import com.example.nunepc.beautyblinkbeautician.model.DataPayment;
import com.example.nunepc.beautyblinkbeautician.model.DataReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 1/3/2560.
 */

public class HiredDetails extends AppCompatActivity {

    HashMap<String, Object> requestValues;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    String beauid;
    TextView payment_date, payment_time, payment_bank, payment_amount,topic,desc,waittopay,confirm,cancel;
    LinearLayout bt_payment, bt_finish, bt_confirm, payment,review;
    private TextView date, service, event, time, special, location, maxprice, numofPer, amount, beauname, yes, no;
    ImageView picpro, slip,attachphoto;
    String status;
    private AlertDialog dialog;
    private RatingBar rating_Bar;
    View view1,view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_details);

        requestValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("request");
        status = getIntent().getStringExtra("status");


        bt_finish = (LinearLayout) findViewById(R.id.bt_finish);
        bt_confirm = (LinearLayout) findViewById(R.id.bt_confirm);
        payment = (LinearLayout) findViewById(R.id.payment);
        review = (LinearLayout) findViewById(R.id.review);
        payment_date = (TextView) findViewById(R.id.payment_date);
        payment_time = (TextView) findViewById(R.id.payment_time);
        payment_bank = (TextView) findViewById(R.id.payment_bank);
        payment_amount = (TextView) findViewById(R.id.payment_amount);
        waittopay = (TextView) findViewById(R.id.waittopay);
        confirm = (TextView) findViewById(R.id.confirm);
        cancel = (TextView) findViewById(R.id.cancel);
        view1 = (View) findViewById(R.id.view1);
        view2 = (View) findViewById(R.id.view2);
        topic = (TextView) findViewById(R.id.topic);
        desc = (TextView) findViewById(R.id.des);
        slip = (ImageView) findViewById(R.id.slip);
        rating_Bar = (RatingBar) findViewById(R.id.rating);
        attachphoto = (ImageView) findViewById(R.id.attachphoto);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if(status.equals("3")){
            bt_confirm.setVisibility(View.VISIBLE);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference mCustRef = FirebaseDatabase.getInstance().getReference().child("/customer-request1/" + requestValues.get("custid").toString() + "/" + requestValues.get("key").toString());
                    mCustRef.child("status").setValue("4");

                    DatabaseReference mBeauRef = FirebaseDatabase.getInstance().getReference().child("/beautician-received/" + beauid + "/" + requestValues.get("key").toString());
                    mBeauRef.child("status").setValue("4");

                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                    String[] Dateparts = requestValues.get("date").toString().split("-");
                    String day = Dateparts[0];
                    String month = Dateparts[1];
                    String year = Dateparts[2];

                    String[] Timeparts = requestValues.get("time").toString().split(":");
                    String hh = Timeparts[0]+2;
                    String mm = Timeparts[1];

                    final HashMap<String, Object> PlannerValues = new HashMap<>();
                    PlannerValues.put("title", requestValues.get("event").toString());
                    PlannerValues.put("location", requestValues.get("location").toString());
                    PlannerValues.put("note","");
                    PlannerValues.put("day",day);
                    PlannerValues.put("month", month);
                    PlannerValues.put("year", year);
                    PlannerValues.put("start",requestValues.get("time").toString());
                    PlannerValues.put("end", hh+":"+mm);
                    PlannerValues.put("status", requestValues.get("status").toString());
                    PlannerValues.put("uid", mFirebaseUser.getUid().toString());

                    String datekey = requestValues.get("date").toString();
                    String startkey = requestValues.get("time").toString();


                    Map<String,Object> childUpdate = new HashMap<>();
                    childUpdate.put("/beautician-planner/"+mFirebaseUser.getUid().toString()+"/"+datekey+"/"+startkey, PlannerValues);

                    mRootRef.updateChildren(childUpdate);

                    Intent intent = new Intent(HiredDetails.this, MainActivity.class);
                    intent.putExtra("menu", "request");
                    startActivity(intent);
                }

            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference mCustRef = FirebaseDatabase.getInstance().getReference().child("/customer-request1/" + mFirebaseUser.getUid().toString() + "/" + requestValues.get("key").toString());
                    mCustRef.child("status").setValue("2");

                    DatabaseReference mBeauRef = FirebaseDatabase.getInstance().getReference().child("/beautician-received/" + beauid + "/" + requestValues.get("key").toString());
                    mBeauRef.child("status").setValue("8");

                    Intent intent = new Intent(HiredDetails.this, MainActivity.class);
                    intent.putExtra("menu", "request");
                    startActivity(intent);
                }

            });

        }
        if(status.equals("4")){
            waittopay.setVisibility(View.VISIBLE);
        }
        if (status.equals("5")) {
            payment.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);

            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("customer-payment").child(requestValues.get("uid").toString())
                    .child(requestValues.get("key").toString());

            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot datashot : dataSnapshot.getChildren()) {


                        if (datashot.getValue() == null) {
                        } else {

                            DataPayment hired = datashot.getValue(DataPayment.class);
                            Picasso.with(getApplicationContext()).load(hired.slip).fit().centerCrop().into(slip);
                            payment_date.setText(hired.date);
                            payment_time.setText(hired.time);
                            payment_bank.setText(hired.bank);
                            payment_amount.setText(hired.amount);

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (status.equals("6")) {
            payment.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);

            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("customer-payment").child(requestValues.get("uid").toString())
                    .child(requestValues.get("key").toString());

            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot datashot : dataSnapshot.getChildren()) {


                        if (datashot.getValue() == null) {
                        } else {

                            DataPayment hired = datashot.getValue(DataPayment.class);
                            Picasso.with(getApplicationContext()).load(hired.slip).fit().centerCrop().into(slip);
                            payment_date.setText(hired.date);
                            payment_time.setText(hired.time);
                            payment_bank.setText(hired.bank);
                            payment_amount.setText(hired.amount);

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        if(status.equals("7")){
            review.setVisibility(View.VISIBLE);
            payment.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);

            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("customer-payment").child(requestValues.get("uid").toString())
                    .child(requestValues.get("key").toString());

            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot datashot : dataSnapshot.getChildren()) {


                        if (datashot.getValue() == null) {
                        } else {

                            DataPayment hired = datashot.getValue(DataPayment.class);
                            Picasso.with(getApplicationContext()).load(hired.slip).fit().centerCrop().into(slip);
                            payment_date.setText(hired.date);
                            payment_time.setText(hired.time);
                            payment_bank.setText(hired.bank);
                            payment_amount.setText(hired.amount);

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference dataReview = FirebaseDatabase.getInstance().getReference().child("customer-review").child(requestValues.get("uid").toString())
                    .child(requestValues.get("key").toString());

            dataReview.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot datashot : dataSnapshot.getChildren()) {


                        if (datashot.getValue() == null) {
                        } else {

                            DataReview rev = datashot.getValue(DataReview.class);
                            rating_Bar.setRating((float) (rev.rating*1.0));
                            topic.setText(rev.topic);
                            desc.setText(rev.desc);

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        date = (TextView) findViewById(R.id.cusD);
        service = (TextView) findViewById(R.id.cusSer);
        event = (TextView) findViewById(R.id.cusEv);
        time = (TextView) findViewById(R.id.cusTime);
        special = (TextView) findViewById(R.id.cusSpe);
        location = (TextView) findViewById(R.id.cusLo);
        maxprice = (TextView) findViewById(R.id.cusMax);
        numofPer = (TextView) findViewById(R.id.cusNum);
        amount = (TextView) findViewById(R.id.amount);
        beauname = (TextView) findViewById(R.id.tname);
        picpro = (ImageView) findViewById(R.id.pic_pro);

        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("customer-received").child(requestValues.get("custid").toString())
                .child(requestValues.get("key").toString());

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot datashot : dataSnapshot.getChildren()) {


                    if (datashot.getValue() == null) {
                    } else {

                        DataOffer hired = datashot.getValue(DataOffer.class);
                        //if(hired.status.equals("3")){
                            if (hired.beaupic!="") {
                                Picasso.with(getApplicationContext()).load(hired.beaupic).fit().centerCrop().into(picpro);
                            }
                            if (hired.offerpic != "") {
                                Picasso.with(getApplicationContext()).load(hired.offerpic).fit().centerCrop().into(attachphoto);
                            }

                            beauid = hired.beauid;
                            beauname.setText(hired.beauname);
                            date.setText(hired.date);
                            service.setText(hired.service);
                            event.setText(hired.event);
                            time.setText(hired.time);
                            amount.setText(String.valueOf(hired.amount));
                            special.setText(hired.specialrequest);
                            location.setText(hired.location);
                            maxprice.setText(hired.price);
                            numofPer.setText(hired.numberofperson);
                       // }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}

