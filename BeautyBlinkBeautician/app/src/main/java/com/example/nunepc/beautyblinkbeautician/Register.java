package com.example.nunepc.beautyblinkbeautician;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.fragment.GalleryFragment;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by NunePC on 23/11/2559.
 */

public class Register extends AppCompatActivity implements View.OnClickListener {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private TextView inputEmail, inputPassword, inputFirstname, inputLasstname,
            inputPhoneNo, inputAddr_num, inputAddr_s_dist,
            inputAddr_dist, inputAddr_province, inputAddr_code;

    private String email, pass, fname, lname, phone, addr_num,
            addr_s_dist, addr_dist, addr_province, addr_code;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist1);

        dateView = (TextView) findViewById(R.id.button1);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.btn_continue).setOnClickListener(this);

    }



    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
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
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                addNewUser();
            break;
            case R.id.btn_register:
                setContentView(R.layout.activity_pre_verify);
                findViewById(R.id.btn_skip).setOnClickListener(this);
            break;
            case  R.id.btn_skip:
                startActivity(new Intent(Register.this, MainActivity.class));
        }
    }

    public void addNewUser(){



        //create user
        mAuth.createUserWithEmailAndPassword(email, pass)
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
                            //Get current to pull UID and email
                            mFirebaseAuth = FirebaseAuth.getInstance();
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();

                            //create root of Beautician
                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                            DatabaseReference mUsersRef = mRootRef.child("beautician");
                            //String key = mUsersRef.push().getKey();

                            User user = new User(email, "Chanasit");

                                /*HashMap<String, Object> UserValues = new HashMap<>();
                                UserValues.put("Email", email);
                                UserValues.put("Name", "Tidaporn");

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put( mFirebaseUser.getUid(), UserValues);

                                mUsersRef.updateChildren(childUpdates);*/

                            mUsersRef.child(mFirebaseUser.getUid()).setValue(user);

                        }
                    }
                });

        //continue to add service
        setContentView(R.layout.activity_regist2);
        findViewById(R.id.btn_register).setOnClickListener(this);


    }

    public void addService(){



        //finish register
        setContentView(R.layout.activity_pre_verify);
        findViewById(R.id.btn_skip).setOnClickListener(this);

    }
}
