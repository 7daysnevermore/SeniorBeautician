package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 23/2/2560.
 */

public class EditGallery extends AppCompatActivity {

    HashMap<String, Object> galleryValue;
    ImageView picpost,edit_image;
    EditText caption;
    TextView edit;
    private Button takeP,chooseP,can,delete;
    private int REQUEST_CAMERA =0,SELECT_FILE=1;
    private AlertDialog dialog;
    Uri imageUri = null;
    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgallery);

        caption = (EditText) findViewById(R.id.caption);
        edit = (TextView) findViewById(R.id.edit_gallery);
        picpost = (ImageView) findViewById(R.id.picpost);
        delete = (Button) findViewById(R.id.delete);

        galleryValue = (HashMap<String, Object>) getIntent().getExtras().getSerializable("gallery");

        Picasso.with(this).load(galleryValue.get("image").toString()).into(picpost);
        caption.setText(galleryValue.get("caption").toString());

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");

        edit_image = (ImageView) findViewById(R.id.edit_image);
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initialize dialog gallery
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditGallery.this);
                final View mView = EditGallery.this.getLayoutInflater().inflate(R.layout.dialog_menu,null);

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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPost();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){
            dialog.dismiss();
            imageUri = data.getData();
            picpost.setImageURI(imageUri);


        }
        else if(requestCode == REQUEST_CAMERA && requestCode == RESULT_OK){
            dialog.dismiss();
            imageUri = data.getData();
            picpost.setImageURI(imageUri);
        }

    }

    public void editPost(){

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

                    String key = galleryValue.get("key").toString();


                    final HashMap<String, Object> GalleryValues = new HashMap<>();
                    GalleryValues.put("image", dowloadUrl.toString());
                    GalleryValues.put("caption", post_caption);
                    GalleryValues.put("uid", galleryValue.get("uid").toString());
                    GalleryValues.put("username", galleryValue.get("username").toString());
                    GalleryValues.put("timestamp",Long.parseLong(galleryValue.get("timestamp").toString()));


                    Map<String,Object> childUpdate = new HashMap<>();
                    childUpdate.put("/gallery/"+key, GalleryValues);
                    childUpdate.put("/beautician-gallery/"+galleryValue.get("uid").toString()+"/"+key, GalleryValues);

                    mRootRef.updateChildren(childUpdate);

                    Intent intent = new Intent(EditGallery.this,MainActivity.class);
                    intent.putExtra("menu","gallery");
                    startActivity(intent);


                }
            });


        }



    }

    public void deletePost() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("beautician-gallery/"+galleryValue.get("uid").toString()+"/"+galleryValue.get("key").toString());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    snapshot.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        ref = FirebaseDatabase.getInstance().getReference().child("gallery/"+galleryValue.get("key").toString());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    snapshot.getRef().removeValue();
                    Intent intent = new Intent(EditGallery.this,MainActivity.class);
                    intent.putExtra("menu","gallery");
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });



    }

}
