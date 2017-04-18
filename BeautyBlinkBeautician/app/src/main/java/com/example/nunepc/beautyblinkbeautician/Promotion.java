package com.example.nunepc.beautyblinkbeautician;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import com.example.nunepc.beautyblinkbeautician.model.DataPromotion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by NunePC on 12/1/2560.
 */

public class Promotion extends AppCompatActivity {

    private TextView postP;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private LinearLayout create_pro;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        //up button
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("beautician-promotion"+"/"+mFirebaseUser.getUid().toString());
        //professor promotion feeds
        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Order from latest data
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final FirebaseRecyclerAdapter<DataPromotion,PromotionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataPromotion, PromotionViewHolder>
                (DataPromotion.class,R.layout.promotion_row,PromotionViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(PromotionViewHolder viewHolder, final DataPromotion model, final int position) {

                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setPromotion(model.getPromotion());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setSale(model.getSale());

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    //private static final String TAG = "Promotion";
                    final String cshow = getRef(position).getKey();
                    @Override
                    public void onClick(View view) {
                        //Log.w(TAG, "You clicked on "+position);
                        //firebaseRecyclerAdapter.getRef(position).removeValue();
                        //Toast.makeText(Promotion.this, "This is my Toast message!",
                               // Toast.LENGTH_LONG).show();
                        HashMap<String, Object> promotionValues = new HashMap<>();
                        promotionValues.put("key",cshow);
                        promotionValues.put("promotion",model.getPromotion());
                        promotionValues.put("image",model.getImage());
                        promotionValues.put("details",model.getDetails());
                        promotionValues.put("price",model.getPrice());
                        promotionValues.put("sale",model.getSale());
                        promotionValues.put("dateFrom",model.getDateFrom());
                        promotionValues.put("dateTo",model.getDateTo());
                        promotionValues.put("uid",model.getUid());
                        promotionValues.put("name",model.getName());
                        promotionValues.put("service",model.getService());
                        promotionValues.put("status",model.getStatus());
                        Intent cPro = new Intent(Promotion.this,PromotionDetails.class);
                        cPro.putExtra("promotion",  promotionValues);
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



        create_pro = (LinearLayout)findViewById(R.id.create_pro);
        create_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Promotion.this,PostPromotion.class);
                startActivity(intent);
            }
        });
    }

    // up button method
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                Intent intent = new Intent(Promotion.this,MainActivity.class);
                intent.putExtra("menu","setting");
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();


    }

    public static class PromotionViewHolder extends RecyclerView.ViewHolder  {

        public View mview;

        public PromotionViewHolder(View itemView){
            super(itemView);
            mview=itemView;

        }


        public void setPromotion(String promotion){
            TextView post_promotion = (TextView)mview.findViewById(R.id.promotion);
            post_promotion.setText(promotion);
        }
        public void setImage(Context context, String image){
            ImageView img = (ImageView)mview.findViewById(R.id.post_image);

            Picasso.with(context).load(image).fit().centerCrop().into(img);
        }
        public void setPrice(String price){
            TextView post_price = (TextView)mview.findViewById(R.id.price);
            post_price.setText(price+" Bath");
            post_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        public void setSale(String sale){
            TextView post_sale= (TextView)mview.findViewById(R.id.sale);
            post_sale.setText(sale+" Bath");
        }
    }



}
