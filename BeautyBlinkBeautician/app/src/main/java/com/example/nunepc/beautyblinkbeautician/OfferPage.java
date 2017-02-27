package com.example.nunepc.beautyblinkbeautician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

    private TextView date,service,event,time,special,location,maxprice,numofPer,name;
    HashMap<String, Object> requestValues;
    private ImageView btnOffer;
    private EditText spe_b;
    private String username;
    private String k,keyC;
    String uid;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);
        requestValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("request");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        uid = mFirebaseUser.getUid().toString();
        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(OfferPage.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    username = user.firstname;
                    Log.d("name1",""+username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        date= (TextView)findViewById(R.id.cusD);
        service =(TextView)findViewById(R.id.cusSer);
        event =(TextView)findViewById(R.id.cusEv);
        time = (TextView)findViewById(R.id.cusTime);
        special = (TextView)findViewById(R.id.cusSpe);
        location = (TextView)findViewById(R.id.cusLo);
        maxprice = (TextView)findViewById(R.id.cusMax);
        name = (TextView)findViewById(R.id.tname);
        numofPer = (TextView)findViewById(R.id.cusNum);
        spe_b = (EditText)findViewById(R.id.speB);

        k = requestValues.get("key").toString();
        date.setText(requestValues.get("date").toString());
        service.setText(requestValues.get("service").toString());
        event.setText(requestValues.get("event").toString());
        numofPer.setText(requestValues.get("numberofperson").toString());
        maxprice.setText(requestValues.get("maxprice").toString());
        time.setText(requestValues.get("time").toString());
        location.setText(requestValues.get("location").toString());
        special.setText(requestValues.get("specialrequest").toString());
        name.setText(requestValues.get("name").toString());


        //DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("request");

        //location.setText(requestValues.get("location").toString());
       // findViewById(R.id.btn_editpromotion).setOnClickListener((View.OnClickListener) this);
            btnOffer = (ImageView)findViewById(R.id.btn_offer);

        btnOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String d = date.getText().toString();
                final String ser = service.getText().toString();
                final String eve = event.getText().toString();
                final String maxP = maxprice.getText().toString();
                final String numP = numofPer.getText().toString();
                final String ti = time.getText().toString();
                final String specus = special.getText().toString();
                final String locate = location.getText().toString();
                final String spebeau = spe_b.getText().toString();
                final String ke = k;
                final String status = "offer-pro";

                if(!TextUtils.isEmpty(d) && !TextUtils.isEmpty(ser) &&
                        !TextUtils.isEmpty(eve) && !TextUtils.isEmpty(ti) &&
                        !TextUtils.isEmpty(specus) && !TextUtils.isEmpty(locate) &&
                        !TextUtils.isEmpty(spebeau) && !TextUtils.isEmpty(status)&&
                        !TextUtils.isEmpty(maxP) && !TextUtils.isEmpty(numP)  )
                {
                    //Create root of Request
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mRequestRef = mRootRef.child("offer");

                    String key = mRequestRef.push().getKey();

                    Calendar c= Calendar.getInstance();
                    SimpleDateFormat currenttime = new SimpleDateFormat("MMM dd yyyy hh:mm a");
                    String dateform = currenttime.format(c.getTime());
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("request"+"/"+ke);
                    //mRef.child("status").setValue("toprovide");
                    //mRef.child("color").setValue("#85FC56");
                    final HashMap<String, Object> RequestValues = new HashMap<String, Object>();
                    RequestValues.put("date", d);
                    RequestValues.put("service", ser);
                    RequestValues.put("event", eve);
                    RequestValues.put("maxprice", maxP);
                    RequestValues.put("numberofcustomer",numP);
                    RequestValues.put("time", ti);
                    RequestValues.put("specialcus", specus);
                    RequestValues.put("beauticianoffer", spebeau);
                    RequestValues.put("location",locate);
                    RequestValues.put("status",status);
                    //RequestValues.put("beauid",)
                    RequestValues.put("dateform",dateform);
                    RequestValues.put("uid", uid);
                    RequestValues.put("key", key);
                    RequestValues.put("namecus", requestValues.get("name").toString());
                    RequestValues.put("namebeau",username);
                    RequestValues.put("color", 0xFFFFFF);

                    final HashMap<String, Object> changestatus = new HashMap<String, Object>();
                    changestatus.put("afterofferstatus","toprovide");
                    changestatus.put("afteroffercolor","#85FC56");
                    changestatus.put("namecus", requestValues.get("name").toString());
                    changestatus.put("date", d);
                    changestatus.put("maxprice", maxP);
                    changestatus.put("numberofcustomer",numP);
                    changestatus.put("time", ti);
                    changestatus.put("specialcus", specus);
                    changestatus.put("beauticianoffer", spebeau);
                    changestatus.put("location",locate);
                    changestatus.put("currenttime",dateform);
                    changestatus.put("event",eve);
                    changestatus.put("service", ser);



                    final HashMap<String, Object> keepkey = new HashMap<String, Object>();
                    keepkey.put("key",key);
                    keepkey.put("uid",uid);


                    Map<String, Object> childUpdate = new HashMap<>();
                    //childUpdate.put("/request/"+ke,changestatus);
                    childUpdate.put("/offer/"+ke+"/" + key, RequestValues);
                    childUpdate.put("/beautician-offer/" + mFirebaseUser.getUid().toString() + "/" + key, RequestValues);
                    childUpdate.put("/beautician-key/" + ke, keepkey);
                    mRootRef.updateChildren(childUpdate);
                    //Map<String, Object> childUpdate1 = new HashMap<>();
                    mRootRef.child("beauticianbusiness/"+mFirebaseUser.getUid().toString()+"/"+key).updateChildren(changestatus);
                   // mRootRef.child("beauticianbusiness/"+mFirebaseUser.getUid().toString()+"/"+key).updateChildren(RequestValues);

                   // progressDialog.dismiss();
                    Intent intent = new Intent(OfferPage.this,MainActivity.class);
                    startActivity(intent);

                }

                }

        });
    }
}
