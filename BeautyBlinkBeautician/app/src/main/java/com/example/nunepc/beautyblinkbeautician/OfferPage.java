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

import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OfferPage extends AppCompatActivity {

    private TextView date,service,event,time,special,location,maxprice,numofPer,name;
    HashMap<String, Object> requestValues;
    private ImageView btnOffer;
    private EditText spe_b;
    private String username;
    private String k;
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
        //name.setText(requestValues.get("name").toString());
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
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("customer-request/W5mxjrCK4wXr1rm79gIe9gvrCtI3"+"/"+ke);
                    mRef.child("status").setValue("toprovide");
                    mRef.child("color").setValue("#ffeef3");
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
                    //RequestValues.put("uid", mFirebaseUser.getUid().toString());
                    //RequestValues.put("name", username);
                    RequestValues.put("color", 0xFFFFFF);
                    Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put("/offer/" + key, RequestValues);
                    childUpdate.put("/beautician-offer/" + mFirebaseUser.getUid().toString() + "/" + key, RequestValues);
                    mRootRef.updateChildren(childUpdate);
                   // progressDialog.dismiss();
                    Intent intent = new Intent(OfferPage.this,MainActivity.class);
                    startActivity(intent);

                }

                }

        });
    }
}
