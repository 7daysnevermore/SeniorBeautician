package com.example.nunepc.beautyblinkbeautician.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.nunepc.beautyblinkbeautician.OpenPhoto;
import com.example.nunepc.beautyblinkbeautician.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by NunePC on 18/11/2559.
 */

public class GalleryFragment extends Fragment {
    private GridView gallery;
    private Button addP,takeP,chooseP,can;
    private AlertDialog dialog;
    private int REQUEST_CAMERA =0,SELECT_FILE=1;
    private Bitmap bm =null;
    private ImageView testP;
    private ArrayList<Bitmap> mylist = new ArrayList<>();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    private StorageReference storageReference,filepath;
    private DatabaseReference databaseReference;

    public GalleryFragment(){ super(); }

    public static GalleryFragment newInstance(){
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle(); //Argument
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gallery,container,false);
        initInstance(rootView);
        return rootView;
    }

    private void initInstance(View rootView){

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");

        addP =(Button)rootView.findViewById(R.id.add_photo);
        testP = (ImageView)rootView.findViewById(R.id.imageView);

        gallery=(GridView)rootView.findViewById(R.id.gal_photo);
        gallery.setAdapter(new ImageAdapter(getActivity()));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Keep photo that we chose in this
                Bitmap bmp = (Bitmap) parent.getItemAtPosition(position);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(getActivity(), OpenPhoto.class);
                intent.putExtra("image",byteArray);

                //Start details activity
                startActivity(intent);
            }
        });

        addP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initialize dialog gallery
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu,null);

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

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){

            onSelectFromGalleryResult(data);

        }
        else if(requestCode == REQUEST_CAMERA && requestCode == RESULT_OK){
            onCaptureImageResult(data);
        }

    }
    private void onCaptureImageResult(Intent data) {

        Uri imageUri = data.getData();

        /*Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);*/

        filepath = storageReference.child("Gallery/"+mFirebaseUser.getUid()).child(data.getData().getLastPathSegment());

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


                Map<String,Object> childUpdate = new HashMap<>();
                childUpdate.put("/gallery/"+key, GalleryValues);
                childUpdate.put("/beautician-promotion/"+mFirebaseUser.getUid().toString()+"/"+key, GalleryValues);

                mRootRef.updateChildren(childUpdate);
            }
        });

        /*File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    private void onSelectFromGalleryResult(Intent data) {

        /*if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mylist.add(bm);
        testP.setImageBitmap(bm);*/

    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public ImageAdapter(Context c) {mContext = c;
        }

        @Override
        public int getCount() {
            return mylist.size();
        }

        @Override
        public Object getItem(int i) {
            return mylist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView;

            if (view == null) {

                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                imageView = (ImageView) view;
            }

            imageView.setImageBitmap(mylist.get(i));

            return imageView;
        }
    }
    @Override
    public void onStart(){ super.onStart(); }

    @Override
    public void onStop(){ super.onStop(); }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            //Restore Instance State here
        }
    }
}
