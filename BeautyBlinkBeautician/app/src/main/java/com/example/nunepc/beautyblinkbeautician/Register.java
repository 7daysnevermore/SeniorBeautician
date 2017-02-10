package com.example.nunepc.beautyblinkbeautician;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;

import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.fragment.GalleryFragment;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 23/11/2559.
 */

public class Register extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CAMERA =0,SELECT_FILE=1;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private TextView birthday;
    private int year, month, day;
    private int yyyy, mm, dd;

    private RadioGroup radioGroup_gender;
    private RadioButton button_gender;

    private TextView inputEmail;
    private TextView inputPassword;
    private TextView inputFirstname;
    private TextView inputLastname;
    private TextView inputPhoneNo;
    private TextView inputAddr_num;
    private TextView inputAddr_s_dist;
    private TextView inputAddr_dist;
    private TextView inputAddr_province;
    private TextView inputAddr_code;
    private String input_gender;
    private ImageView imageprofile;
    Uri imageUri;

    String key;

    private int s01_price;
    private int s02_price;
    private int s03_price;
    private int s04_price;

    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

    private EditText inputS01;
    private EditText inputS02;
    private EditText inputS03;
    private EditText inputS04;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private ImageView marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist1);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //
            }


        };

        //find view by id
        dateView = (TextView) findViewById(R.id.button1);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        birthday = (TextView) findViewById(R.id.birthdate);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pass);
        inputFirstname = (EditText) findViewById(R.id.fname);
        inputLastname = (EditText) findViewById(R.id.lname);
        inputPhoneNo = (EditText) findViewById(R.id.phone);
        marker = (ImageView) findViewById(R.id.marker);
        inputAddr_num = (EditText) findViewById(R.id.addressnum);
        inputAddr_s_dist = (EditText) findViewById(R.id.sub_district);
        inputAddr_dist = (EditText) findViewById(R.id.district);
        inputAddr_province = (EditText) findViewById(R.id.province);
        inputAddr_code = (EditText) findViewById(R.id.code);

        imageprofile = (ImageView) findViewById(R.id.imageprofile);
        imageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });

        findViewById(R.id.marker).setOnClickListener(this);
        findViewById(R.id.btn_continue).setOnClickListener(this);

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageprofile.setImageURI(imageUri);
        }

    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        birthday.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        yyyy = year;
        mm = month;
        dd = day;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.marker:
                startActivity(new Intent(Register.this, MapCurrentLocation.class));
                break;
            case R.id.btn_continue:
                addNewUser();
                break;
            case R.id.btn_register:
                addService();
                break;
            case R.id.btn_skip:
                startActivity(new Intent(Register.this, MainActivity.class));
        }
    }

    public void addNewUser() {

        radioGroup_gender = (RadioGroup) findViewById(R.id.gender);
        int selectedId = radioGroup_gender.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        button_gender = (RadioButton) findViewById(selectedId);
        input_gender = button_gender.getText().toString();


        //Get value
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month + 1, day);

        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        final String fname = inputFirstname.getText().toString();
        final String lname = inputLastname.getText().toString();
        final String phone = inputPhoneNo.getText().toString();
        final String addr_num = inputAddr_num.getText().toString();
        final String addr_s_dist = inputAddr_s_dist.getText().toString();
        final String addr_dist = inputAddr_dist.getText().toString();
        final String addr_province = inputAddr_province.getText().toString();
        final String addr_code = inputAddr_code.getText().toString();

        /*if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(fname)) {
            Toast.makeText(getApplicationContext(), "Enter firstname!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(lname)) {
            Toast.makeText(getApplicationContext(), "Enter lastname!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Enter phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addr_num)) {
            Toast.makeText(getApplicationContext(), "Enter house number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addr_s_dist)) {
            Toast.makeText(getApplicationContext(), "Enter sub district!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addr_dist)) {
            Toast.makeText(getApplicationContext(), "Enter district!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addr_province)) {
            Toast.makeText(getApplicationContext(), "Enter province!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addr_code)) {
            Toast.makeText(getApplicationContext(), "Enter code!", Toast.LENGTH_SHORT).show();
            return;
        }*/



                //create user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    if (imageUri != null) {
                                        storageReference = FirebaseStorage.getInstance().getReference();
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("BeauticianProfile");

                                        filepath = storageReference.child("BeauticianProfile").child(imageUri.getLastPathSegment());

                                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                final Uri dowloadUrl = taskSnapshot.getDownloadUrl();
                                                //Get current to pull UID and email
                                                mFirebaseAuth = FirebaseAuth.getInstance();
                                                mFirebaseUser = mFirebaseAuth.getCurrentUser();

                                                //create root of Beautician
                                                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                                                DatabaseReference mUsersRef = mRootRef.child("beautician");

                                                HashMap<String, Object> UserValues = new HashMap<>();
                                                UserValues.put("profile", dowloadUrl.toString());
                                                UserValues.put("email", email);
                                                UserValues.put("firstname", fname);
                                                UserValues.put("lastname", lname);
                                                UserValues.put("phone", phone);
                                                UserValues.put("birthday", dd + "/" + mm + "/" + yyyy);
                                                UserValues.put("gender", input_gender);
                                                UserValues.put("address_number", addr_num);
                                                UserValues.put("address_sub_district", addr_s_dist);
                                                UserValues.put("address_district", addr_dist);
                                                UserValues.put("address_province", addr_province);
                                                UserValues.put("address_code", addr_code);
                                                Map<String, Object> childUpdates = new HashMap<>();
                                                childUpdates.put(mFirebaseUser.getUid(), UserValues);

                                                mUsersRef.updateChildren(childUpdates);

                                                //Add to profile promote
                                                DatabaseReference mPromoteRef = mRootRef.child("profilepromote");

                                                key = mPromoteRef.push().getKey();

                                                final HashMap<String, Object> ProfilePromoteValues = new HashMap<>();
                                                ProfilePromoteValues.put("BeauticianProfile", dowloadUrl.toString());
                                                ProfilePromoteValues.put("uid", mFirebaseUser.getUid());
                                                ProfilePromoteValues.put("name", fname);
                                                ProfilePromoteValues.put("sub_district", addr_s_dist);
                                                ProfilePromoteValues.put("district", addr_dist);
                                                ProfilePromoteValues.put("province", addr_province);
                                                ProfilePromoteValues.put("S01", 0);
                                                ProfilePromoteValues.put("S02", 0);
                                                ProfilePromoteValues.put("S03", 0);
                                                ProfilePromoteValues.put("S04", 0);
                                                ProfilePromoteValues.put("picture1", "");
                                                ProfilePromoteValues.put("picture2", "");
                                                ProfilePromoteValues.put("picture3", "");
                                                ProfilePromoteValues.put("rating", "");


                                                Map<String, Object> childUpdate = new HashMap<>();
                                                childUpdate.put("/profilepromote/" + key, ProfilePromoteValues);
                                                childUpdate.put("/beautician-profilepromote/" + mFirebaseUser.getUid().toString() + "/" + key, ProfilePromoteValues);

                                                mRootRef.updateChildren(childUpdate);

                                                //continue to add service
                                                setContentView(R.layout.activity_regist2);
                                                findViewById(R.id.btn_register).setOnClickListener(Register.this);

                                            }
                                        });

                                    }

                                    if (imageUri == null){

                                        mFirebaseAuth = FirebaseAuth.getInstance();
                                        mFirebaseUser = mFirebaseAuth.getCurrentUser();

                                        //create root of Beautician
                                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                                        DatabaseReference mUsersRef = mRootRef.child("beautician");

                                        HashMap<String, Object> UserValues = new HashMap<>();
                                        UserValues.put("profile", "");
                                        UserValues.put("email", email);
                                        UserValues.put("firstname", fname);
                                        UserValues.put("lastname", lname);
                                        UserValues.put("phone", phone);
                                        UserValues.put("birthday", dd + "/" + mm + "/" + yyyy);
                                        UserValues.put("gender", input_gender);
                                        UserValues.put("address_number", addr_num);
                                        UserValues.put("address_sub_district", addr_s_dist);
                                        UserValues.put("address_district", addr_dist);
                                        UserValues.put("address_province", addr_province);
                                        UserValues.put("address_code", addr_code);
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put(mFirebaseUser.getUid(), UserValues);

                                        mUsersRef.updateChildren(childUpdates);

                                        //Add to profile promote
                                        DatabaseReference mPromoteRef = mRootRef.child("profilepromote");

                                        key = mPromoteRef.push().getKey();

                                        final HashMap<String, Object> ProfilePromoteValues = new HashMap<>();
                                        ProfilePromoteValues.put("BeauticianProfile", "");
                                        ProfilePromoteValues.put("uid", mFirebaseUser.getUid());
                                        ProfilePromoteValues.put("name", fname);
                                        ProfilePromoteValues.put("sub_district", addr_s_dist);
                                        ProfilePromoteValues.put("district", addr_dist);
                                        ProfilePromoteValues.put("province", addr_province);
                                        ProfilePromoteValues.put("S01", 0);
                                        ProfilePromoteValues.put("S02", 0);
                                        ProfilePromoteValues.put("S03", 0);
                                        ProfilePromoteValues.put("S04", 0);
                                        ProfilePromoteValues.put("picture1", "");
                                        ProfilePromoteValues.put("picture2", "");
                                        ProfilePromoteValues.put("picture3", "");
                                        ProfilePromoteValues.put("rating", "");


                                        Map<String, Object> childUpdate = new HashMap<>();
                                        childUpdate.put("/profilepromote/" + key, ProfilePromoteValues);
                                        childUpdate.put("/beautician-profilepromote/" + mFirebaseUser.getUid().toString() + "/" + key, ProfilePromoteValues);

                                        mRootRef.updateChildren(childUpdate);

                                        //continue to add service
                                        setContentView(R.layout.activity_regist2);
                                        findViewById(R.id.btn_register).setOnClickListener(Register.this);

                                    }
                                }

                            }
                        });


            }



    public void addService() {

        //checkbox
        CheckBox S01check = (CheckBox) findViewById(R.id.makeupandhair);
        boolean S01checked = S01check.isChecked();
        CheckBox S02check = (CheckBox) findViewById(R.id.makeup);
        boolean S02checked = S02check.isChecked();
        CheckBox S03check = (CheckBox) findViewById(R.id.hairstyle);
        boolean S03checked = S03check.isChecked();
        CheckBox S04check = (CheckBox) findViewById(R.id.hairdress);
        boolean S04checked = S04check.isChecked();

        //starting price of each service
        inputS01 = (EditText) findViewById(R.id.makeupandhair_price);
        final String S01 = inputS01.getText().toString();
        inputS02 = (EditText) findViewById(R.id.makeup_price);
        final String S02 = inputS02.getText().toString();
        inputS03 = (EditText) findViewById(R.id.hairstyle_price);
        final String S03 = inputS03.getText().toString();
        inputS04 = (EditText) findViewById(R.id.hairdress_price);
        final String S04 = inputS04.getText().toString();

        //create root of BeauticianService
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("beautician-service");

        //Change price to integer
        if(!TextUtils.isEmpty(S01)){
            s01_price = Integer.parseInt(S01);
        }
        if (!TextUtils.isEmpty(S02)) {
            s02_price = Integer.parseInt(S02);
        }
        if (!TextUtils.isEmpty(S03)) {
            s03_price = Integer.parseInt(S03);
        }
        if (!TextUtils.isEmpty(S04)) {
            s04_price = Integer.parseInt(S04);
        }

        //Add to profile promote
        DatabaseReference mPromoteRef = mRootRef.child("profilepromote").child(key);
        DatabaseReference mPromoteRefB = mRootRef.child("beautician-profilepromote/").child(mFirebaseUser.getUid()).child(key);


        if (S01checked) {
            DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
            HashMap<String, Object> UserValues = new HashMap<>();
            UserValues.put("price", s01_price);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("S01", UserValues);

            mUsersRefService.updateChildren(childUpdates);

            mPromoteRef.child("S01").setValue(s01_price);
            mPromoteRefB.child("S01").setValue(s01_price);

        }
        if (S02checked) {
            DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
            HashMap<String, Object> UserValues = new HashMap<>();
            UserValues.put("price", s02_price);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("S02", UserValues);

            mUsersRefService.updateChildren(childUpdates);

            mPromoteRef.child("S02").setValue(s02_price);
            mPromoteRefB.child("S02").setValue(s02_price);
        }
        if (S03checked) {
            DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
            HashMap<String, Object> UserValues = new HashMap<>();
            UserValues.put("price", s03_price);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("S03", UserValues);

            mUsersRefService.updateChildren(childUpdates);

            mPromoteRef.child("S03").setValue(s03_price);
            mPromoteRefB.child("S03").setValue(s03_price);
        }
        if (S04checked) {
            DatabaseReference mUsersRefService = mUsersRef.child(mFirebaseUser.getUid());
            HashMap<String, Object> UserValues = new HashMap<>();
            UserValues.put("price", s04_price);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("S04", UserValues);

            mUsersRefService.updateChildren(childUpdates);

            mPromoteRef.child("S04").setValue(s04_price);
            mPromoteRefB.child("S04").setValue(s04_price);
        }

        //finish register
        setContentView(R.layout.activity_pre_verify);
        findViewById(R.id.btn_skip).setOnClickListener(this);

    }

}
