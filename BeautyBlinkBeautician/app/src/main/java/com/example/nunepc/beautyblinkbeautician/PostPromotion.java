package com.example.nunepc.beautyblinkbeautician;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PostPromotion extends AppCompatActivity {

    private int REQUEST_CAMERA =0,SELECT_FILE=1;
    private Toolbar toolbar;
    private ImageView addPicture,addPic1,addPic2,addPic3;
    private EditText topic,desc;
    private Button btnP;
    private CheckBox reqPro;
    private Integer[] mThumbsId;
    private Uri imageUri = null;
    private ProgressDialog progressDialog;
    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_promotion);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(PostPromotion.this,EmailLogin.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotion");
        mAuth.addAuthStateListener(mAuthListener);
        progressDialog = new ProgressDialog(this);
        reqPro = (CheckBox)findViewById(R.id.requestP);
        addPicture = (ImageView) findViewById(R.id.addPic);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,SELECT_FILE);
            }
        });

        topic= (EditText)findViewById(R.id.topic);
        desc = (EditText)findViewById(R.id.desc);
        btnP=(Button)findViewById(R.id.post);
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPost();
            }
        });

    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){
            imageUri = data.getData();
            addPicture.setImageURI(imageUri);

        }

    }
    private void startPost(){

        progressDialog.setMessage("Posting...");
        progressDialog.show();
        final String str_topic = topic.getText().toString().trim();
        final String str_desc = desc.getText().toString().trim();

        if(!TextUtils.isEmpty(str_topic) && !TextUtils.isEmpty(str_desc) && imageUri != null){
            filepath = storageReference.child("Post_Images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri dowloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = databaseReference.push();
                    newPost.child("Topic").setValue(str_topic);
                    newPost.child("Desc").setValue(str_desc);
                    newPost.child("Image").setValue(dowloadUrl.toString());
                    if(reqPro.isChecked() == true){
                        newPost.child("RequestPro").setValue("Request");
                    }
                    else
                    {
                        newPost.child("RequestPro").setValue("Nope");
                    }

                    progressDialog.dismiss();


                    startActivity(new Intent(PostPromotion.this,Promotion.class));
                }
            });


        }
    }
    }

