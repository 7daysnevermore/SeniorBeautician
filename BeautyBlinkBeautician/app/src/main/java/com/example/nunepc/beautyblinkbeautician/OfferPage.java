package com.example.nunepc.beautyblinkbeautician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
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
import com.example.nunepc.beautyblinkbeautician.model.DataProfilePromote;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class OfferPage extends AppCompatActivity {

    private static final String AUTH_KEY = "key=AAAAU3g0xJ0:APA91bFc_Bf77RiEJwf4kWxgnqlZFl0fmIZLP32zPSL1VW3aGfyZ-Zt92JbY5_SUIHepL5ZljcEZzukypPSypg6i8U7x8Y4cGn19McqcsNfqjTK4BEaLhPYIaZLFrZHtsiz487XJXfWwLHZd8Nt0-Jb7GOwW1Hxz2g";
    private TextView date, service, event, time, special, location, maxprice, numofPer, name;
    HashMap<String, Object> requestValues;
    private int SELECT_FILE =1;
    private Uri imageUri = null;
    private EditText offer_price,offer_time,offer_location;
    private ImageView special_img,offer_image,btnOffer,pic_pro;
    private String username,beaupic;
    private StorageReference storageReference,filepath;
    String uid,rating;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ProgressDialog progressDialog;
    private Button accept, decline, send_offer;
    String status;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);

        //up button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        status = getIntent().getStringExtra("status");

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
        special_img = (ImageView) findViewById(R.id.special_img);
        pic_pro = (ImageView) findViewById(R.id.pic_pro);

        if(!requestValues.get("requestpic").toString().equals("")){
            Picasso.with(getApplicationContext()).load(requestValues.get("requestpic").toString()).into(special_img);
        }
        if (!requestValues.get("profilecust").toString().equals("")) {
            Picasso.with(getApplicationContext()).load(requestValues.get("profilecust").toString()).into(pic_pro);
        }

        offer_price = (EditText) findViewById(R.id.offer_price);
        offer_time = (EditText) findViewById(R.id.offer_time);
        offer_location = (EditText) findViewById(R.id.offer_location);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        uid = mFirebaseUser.getUid().toString();


        mRootRef.child("beautician-profilepromote").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot startChild : dataSnapshot.getChildren()) {

                    DataProfilePromote user = startChild.getValue(DataProfilePromote.class);
                    String key = startChild.getKey();

                    if (user == null) {
                    } else {
                        username = user.username;
                        beaupic = user.BeauticianProfile;
                        rating = user.rating;
                    }

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


        offer_image = (ImageView) findViewById(R.id.offer_image);
        offer_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });

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

                if (!TextUtils.isEmpty(input_offer_price)&&imageUri!=null) {

                    filepath = storageReference.child("Request").child(imageUri.getLastPathSegment());
                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri dowloadUrl = taskSnapshot.getDownloadUrl();

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
                            RequestValues.put("date", requestValues.get("date").toString());
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
                            RequestValues.put("offerpic", dowloadUrl.toString());
                            RequestValues.put("rating", rating);
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
                    });
                    //Create root of Request

                }
            }
        });




    }
    public void sendTopic() {
        sendWithOtherThread("topic");
    }
    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }

    private void pushNotification(String type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "accept your request");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            jData.put("picture_url", "http://opsbug.com/static/google-io.jpg");

            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put("c5pBXXsuCN0:APA91bH8nLMt084KpzMrmSWRS2SnKZudyNjtFVxLRG7VFEFk_RgOm-Q5EQr_oOcLbVcCjFH6vIXIyWhST1jdhR8WMatujccY5uy1TE0hkppW_TSnSBiUsH_tRReutEgsmIMmq8fexTmL");
                    ja.put(FirebaseInstanceId.getInstance().getToken());
                    jPayload.put("registration_ids", ja);
                    break;
                case "topic":
                    jPayload.put("to", "/topics/customer");
                    break;
                case "token":
                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
                    Log.d("Game","Hi");
                    break;
                default:
                    jPayload.put("to", FirebaseInstanceId.getInstance().getToken());
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                   // mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){
            imageUri = data.getData();
            offer_image.setImageURI(imageUri);

        }

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
