package com.example.nunepc.beautyblinkbeautician;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.model.DataPromotion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by NunePC on 12/1/2560.
 */

public class Promotion extends AppCompatActivity {

    private TextView postP;
    private List<DataPromotion> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostPromotion pp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private LinearLayout create_pro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("beautician-promotion"+"/"+mAuth.getCurrentUser().getUid().toString());
        //professor promotion feeds
        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Order from latest data
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        FirebaseRecyclerAdapter<DataPromotion,PromotionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataPromotion, PromotionViewHolder>
                (DataPromotion.class,R.layout.promotion_row,PromotionViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(PromotionViewHolder viewHolder, DataPromotion model, int position) {
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setPromotion(model.getPromotion());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setSale(model.getSale());

            }
        };

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

    @Override
    public void onStart(){
        super.onStart();

    }

    public static class PromotionViewHolder extends RecyclerView.ViewHolder{

        View mview;

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
            //Picasso.with(context).load(image).into(img);
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
