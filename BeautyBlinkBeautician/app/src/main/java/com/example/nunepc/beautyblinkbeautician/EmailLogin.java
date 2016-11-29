package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 23/11/2559.
 */

public class EmailLogin extends AppCompatActivity implements View.OnClickListener {

        private EditText inputEmail, inputPassword;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private FirebaseAuth mAuth;
        private TextView btnRegister;
        private Button btnLogin;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseUser mFirebaseUser;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    //
                }


            };

            inputEmail = (EditText) findViewById(R.id.email);
            inputPassword = (EditText) findViewById(R.id.pass);
            btnLogin = (Button) findViewById(R.id.btn_login);
            btnRegister = (TextView) findViewById(R.id.register);



            btnLogin.setOnClickListener(this);
            btnRegister.setOnClickListener(this);
        }

        // [START on_start_add_listener]
        @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }
        // [END on_start_add_listener]

        // [START on_stop_remove_listener]
        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
        // [END on_stop_remove_listener]


        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    EmailPasswordLogin();
                    break;
                case R.id.register:
                    Register();
                    break;
            }
        }

        private void EmailPasswordLogin() {
            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            //authenticate user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(EmailLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {

                                Toast.makeText(EmailLogin.this, "Fail", Toast.LENGTH_LONG).show();

                            } else {
                                updateUI();
                            }
                        }
                    });
        }

        private void Register(){

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

            //create user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(EmailLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(EmailLogin.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(EmailLogin.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //Get current to pull UID and email
                                mFirebaseAuth = FirebaseAuth.getInstance();
                                mFirebaseUser = mFirebaseAuth.getCurrentUser();

                                //create root of Beautician
                                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                                DatabaseReference mUsersRef = mRootRef.child("beautician");
                                String key = mUsersRef.push().getKey();

                                HashMap<String, Object> UserValues = new HashMap<>();
                                UserValues.put("Email", email);
                                UserValues.put("Pass", password);
                                UserValues.put("UID", mFirebaseUser.getUid());
                                UserValues.put("Name", "Tidaporn");

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put( key, UserValues);

                                mUsersRef.updateChildren(childUpdates);

                                startActivity(new Intent(EmailLogin.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }

        private void updateUI() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

}
