package com.example.nunepc.beautyblinkbeautician.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.GalleryDetails;
import com.example.nunepc.beautyblinkbeautician.OpenPhoto;
import com.example.nunepc.beautyblinkbeautician.PostGallery;
import com.example.nunepc.beautyblinkbeautician.PostPromotion;
import com.example.nunepc.beautyblinkbeautician.Promotion;
import com.example.nunepc.beautyblinkbeautician.PromotionDetails;
import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.model.DataGallery;
import com.example.nunepc.beautyblinkbeautician.model.DataPromotion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private FloatingActionButton add_photo;

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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("beautician-gallery"+"/"+mFirebaseUser.getUid().toString());
        //professor promotion feeds
        recyclerView =(RecyclerView)rootView.findViewById(R.id.recyclerview_gall);
        recyclerView.setHasFixedSize(true);

        //Order from latest data
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        final FirebaseRecyclerAdapter<DataGallery,GalleryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataGallery, GalleryViewHolder>
                (DataGallery.class,R.layout.gallery_row,GalleryViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(GalleryViewHolder viewHolder, final DataGallery model, final int position) {

                viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    //private static final String TAG = "Promotion";
                    final String cshow = getRef(position).getKey();
                    @Override
                    public void onClick(View view) {
                        //Log.w(TAG, "You clicked on "+position);
                        //firebaseRecyclerAdapter.getRef(position).removeValue();
                        //Toast.makeText(Promotion.this, "This is my Toast message!",
                        // Toast.LENGTH_LONG).show();
                        HashMap<String, Object> galleryValues = new HashMap<>();
                        galleryValues.put("key",cshow);
                        galleryValues.put("caption",model.getCaption());
                        galleryValues.put("image",model.getImage());
                        galleryValues.put("uid",model.getUid());
                        galleryValues.put("name",model.getName());
                        Intent cPro = new Intent(getActivity(),GalleryDetails.class);
                        cPro.putExtra("promotion",  galleryValues);
                        startActivity(cPro);
                    }
                });

            }


        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setLayoutManager(mLayoutManager);


        add_photo = (FloatingActionButton) rootView.findViewById(R.id.add_photo);
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PostGallery.class);
                startActivity(intent);
            }
        });

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

    public static class GalleryViewHolder extends RecyclerView.ViewHolder  {

        View mview;

        public GalleryViewHolder(View itemView){
            super(itemView);
            mview=itemView;

        }

        public void setImage(Context context, String image){
            ImageView img = (ImageView)mview.findViewById(R.id.post_gall);

            Picasso.with(context).load(image).fit().centerCrop().into(img);
        }
    }
}
