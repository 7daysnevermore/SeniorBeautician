package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.example.nunepc.beautyblinkbeautician.R.id.changeProfilePicture;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_FILE = 1;
    TextView fname,lname,birthday,gender,phone,addr,btn_changePic;
    Button edit;

    ImageView profilePicture;

    private Uri imageUri = null;
    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        fname = (TextView) findViewById(R.id.firstname);
        lname = (TextView) findViewById(R.id.lastname);
        birthday = (TextView) findViewById(R.id.birthday);
        gender = (TextView) findViewById(R.id.gender);
        phone = (TextView) findViewById(R.id.phone);
        addr = (TextView) findViewById(R.id.address);
        profilePicture = (ImageView) findViewById(changeProfilePicture);

        btn_changePic = (TextView) findViewById(R.id.btn_changeProfilePicture);

        edit = (Button) findViewById(R.id.btn_edit);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("BeauticianProfile");

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(ViewProfile.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {

                    if (!user.profile.equals("")) {
                        Picasso.with(ViewProfile.this).load(user.profile).fit().centerCrop().into(profilePicture);
                    }
                    fname.setText(user.firstname);
                    lname.setText(user.lastname);
                    birthday.setText(user.birthday);
                    gender.setText(user.gender);
                    phone.setText(user.phone);
                    addr.setText(user.address_number+" "+user.address_sub_district+", "+user.address_district+", "
                    +user.address_province+" "+user.address_code);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(this);

        btn_changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_edit:
                startActivity(new Intent(ViewProfile.this, EditProfile.class));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK){
            imageUri = data.getData();
            profilePicture.setImageURI(imageUri);
            filepath = storageReference.child("BeauticianProfile").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri dowloadUrl = taskSnapshot.getDownloadUrl();

                }
            });
        }

    }
}
