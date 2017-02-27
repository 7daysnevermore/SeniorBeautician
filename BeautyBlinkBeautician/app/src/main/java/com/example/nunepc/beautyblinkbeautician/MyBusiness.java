package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.fragment.RequestFragment;
import com.example.nunepc.beautyblinkbeautician.model.DataMyBusiness;
import com.example.nunepc.beautyblinkbeautician.model.KeepStatus;
import com.example.nunepc.beautyblinkbeautician.model.RequestData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyBusiness extends AppCompatActivity {
    private TextView postP,statusDes;
    private RecyclerView recyclerView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    String uid;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();
        Log.d("uid",""+uid);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("beauticianbusiness/"+uid);
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("request");

        Log.d("test",""+uid);
        //professor promotion feeds
        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Order from latest data
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        Log.d("test1",""+uid);
        final FirebaseRecyclerAdapter<DataMyBusiness,MyBusiness.RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataMyBusiness, MyBusiness.RequestViewHolder>
                (DataMyBusiness.class,R.layout.offercard,MyBusiness.RequestViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(final MyBusiness.RequestViewHolder viewHolder, final DataMyBusiness model, final int position) {
                //viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());
                //viewHolder.setStatus(model.getStatus());
                //viewHolder.setDate(model.getDate());
                //viewHolder.setEvent(model.getEvent());
                //viewHolder.setMaxprice(model.getMaxprice());
                //viewHolder.setService(model.getService());
                //viewHolder.setColor(model.getColor());
                //viewHolder.setColorcircle(model.getColor());
                //viewHolder.setCurrenttime(model.getCurrenttime());
                //viewHolder.setName(model.getName());
                //viewHolder.setKeyrequest(model.getKeyrequest());
                viewHolder.setService(model.getService());
                viewHolder.setStatus(model.getAfterofferstatus());
                viewHolder.setColorcircle(model.getAfteroffercolor());
                viewHolder.setName(model.getNamecus());
                viewHolder.setEvent(model.getEvent());
                viewHolder.setDate(model.getDate());
                viewHolder.setCurrenttime(model.getCurrenttime());
                viewHolder.setMaxprice(model.getMaxprice());



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


    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder  {

        View mview;
        public RequestViewHolder(View itemView){
            super(itemView);
            mview=itemView;

        }
        public void setColorcircle(String color){
            ImageView cC = (ImageView)mview.findViewById(R.id.cirNoti);
            int colorc = Color.parseColor(color);
            //PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(colorc, PorterDuff.Mode.MULTIPLY);
            GradientDrawable drawable = (GradientDrawable) cC.getBackground();
            drawable.setColor(colorc);
            //((GradientDrawable)cC.getBackground().setColorFilter(greyFilter);
            //cC.setBackgroundColor(Color.parseColor(color));
            //GradientDrawable bgShape = (GradientDrawable)cC.getBackground();
            //bgShape.setColor(Color.parseColor(color));
        }
        public void setEvent(String event){
            TextView tv  =(TextView)mview.findViewById(R.id.event_des);
            tv.setText(event);
        }
        public void setService(String service){
            TextView post_service = (TextView)mview.findViewById(R.id.tService);
            post_service.setText(service);

        }
        public void setStatus(String afterofferstatus){
            TextView tv =(TextView)mview.findViewById(R.id.desStatus);
            tv.setText(afterofferstatus);
        }
        public void setName(String name){
            TextView tv =(TextView)mview.findViewById(R.id.tname);
            tv.setText(name);
        }
        public void setCurrenttime(String currenttime){
            TextView tv =(TextView)mview.findViewById(R.id.btnTime);
            DataMyBusiness r = new DataMyBusiness();
            r.setCurrenttime(currenttime);
            //String d = "JAN 31 2017 10:11 PM";
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm a");
            Date convertedDate = null;
            try{
                convertedDate = dateFormat.parse(r.getCurrenttime());
                //convertedDate = dateFormat.parse(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PrettyTime p = new PrettyTime();
            String datetime = p.format(convertedDate);
            Log.d("Bye","="+datetime);
            Log.d("test","="+convertedDate);
            String a = null;
            if(datetime.contains("minutes")){
                a =datetime.replace("minutes","mins");
            }else{
                a = datetime;
            }
            tv.setText(""+a+", ");

            }
        public void setDate(String date){
            TextView tv = (TextView)mview.findViewById(R.id.date_des);
            tv.setText(date);
        }
        public void setMaxprice(String maxprice){
            TextView tv = (TextView)mview.findViewById(R.id.price_des);
            tv.setText(maxprice);
        }

    }

    }







