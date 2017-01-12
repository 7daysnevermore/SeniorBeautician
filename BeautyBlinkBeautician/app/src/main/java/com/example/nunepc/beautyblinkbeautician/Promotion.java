package com.example.nunepc.beautyblinkbeautician;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private LinearLayout lr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotion");
        //professor promotion feeds
        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lr = (LinearLayout)findViewById(R.id.post_content);
        lr.setOnClickListener(new View.OnClickListener() {
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

        FirebaseRecyclerAdapter<DataPromotion,PromotionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataPromotion, PromotionViewHolder>
                (DataPromotion.class,R.layout.promotion_row,PromotionViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(PromotionViewHolder viewHolder, DataPromotion model, int position) {
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setTopic(model.getTopic());
                viewHolder.setDesc(model.getDesc());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PromotionViewHolder extends RecyclerView.ViewHolder{

        View mview;

        public PromotionViewHolder(View itemView){
            super(itemView);
            mview=itemView;

        }
        public void setTopic(String topic){
            TextView post_topic=(TextView)mview.findViewById(R.id.post_topic);
            post_topic.setText(topic);
        }
        public void setDesc(String desc){
            TextView post_pro=(TextView)mview.findViewById(R.id.post_desc);
            post_pro.setText(desc);
        }
        public void setImage(Context context, String image){
            ImageView img =(ImageView)mview.findViewById(R.id.post_image);
            //Picasso.with(context).load(image).into(img);
            Picasso.with(context).load(image).fit().centerCrop().into(img);
        }
    }


}
