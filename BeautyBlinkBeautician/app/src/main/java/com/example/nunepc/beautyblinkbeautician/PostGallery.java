package com.example.nunepc.beautyblinkbeautician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 26/1/2560.
 */

public class PostGallery extends AppCompatActivity {

    ImageView picpost;
    EditText caption;
    TextView share;
    private Button takeP,chooseP,can;
    private int REQUEST_CAMERA =0,SELECT_FILE=1;
    private AlertDialog dialog;
    Uri imageUri = null;
    private ProgressDialog progressDialog;
    String uid,name,propic;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gallery);

        caption = (EditText) findViewById(R.id.caption);
        share = (TextView) findViewById(R.id.share);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");

        mRootRef.child("beautician").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(PostGallery.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                } else {
                    name = user.firstname;
                    propic = user.profile;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        picpost = (ImageView) findViewById(R.id.picpost);
        picpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initialize dialog gallery
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PostGallery.this);
                final View mView = PostGallery.this.getLayoutInflater().inflate(R.layout.dialog_menu,null);

                takeP = (Button)mView.findViewById(R.id.takephoto);
                chooseP =(Button)mView.findViewById(R.id.choosephoto);
                can = (Button)mView.findViewById(R.id.canc);

                takeP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cameraIntent();
                    }

                    private void cameraIntent() {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,REQUEST_CAMERA);
                    }
                });

                chooseP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        galleryIntent();
                    }

                    private void galleryIntent() {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select File"),SELECT_FILE);
                    }
                });

                can.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                dialog=mBuilder.create();
                dialog.show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPost();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){
            imageUri = data.getData();
            picpost.setImageURI(imageUri);

        }
        else if(requestCode == REQUEST_CAMERA && requestCode == RESULT_OK){
            imageUri = data.getData();
            picpost.setImageURI(imageUri);
        }

    }

    private void startPost(){


        final String post_caption = caption.getText().toString();

        if(!TextUtils.isEmpty(post_caption) && imageUri != null){

            filepath = storageReference.child("Gallery").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri dowloadUrl = taskSnapshot.getDownloadUrl();

                    //create root of Promotion
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mGalleryRef = mRootRef.child("gallery");

                    String key = mGalleryRef.push().getKey();

                    final HashMap<String, Object> GalleryValues = new HashMap<>();
                    GalleryValues.put("image", dowloadUrl.toString());
                    GalleryValues.put("caption", post_caption);
                    GalleryValues.put("uid", uid);
                    GalleryValues.put("name", name);
                    GalleryValues.put("propic", propic);


                    Map<String,Object> childUpdate = new HashMap<>();
                    childUpdate.put("/gallery/"+key, GalleryValues);
                    childUpdate.put("/beautician-gallery/"+mFirebaseUser.getUid().toString()+"/"+key, GalleryValues);

                    mRootRef.updateChildren(childUpdate);


                    startActivity(new Intent(PostGallery.this,MainActivity.class));

                }
            });


        }

    }

}
