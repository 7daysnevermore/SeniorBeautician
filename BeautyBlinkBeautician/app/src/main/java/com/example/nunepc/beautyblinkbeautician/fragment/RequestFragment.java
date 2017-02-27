package com.example.nunepc.beautyblinkbeautician.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.Confirm;
import com.example.nunepc.beautyblinkbeautician.EditProfile;
import com.example.nunepc.beautyblinkbeautician.MainActivity;
import com.example.nunepc.beautyblinkbeautician.MessagePage;
import com.example.nunepc.beautyblinkbeautician.OfferPage;
import com.example.nunepc.beautyblinkbeautician.Promotion;
import com.example.nunepc.beautyblinkbeautician.PromotionDetails;
import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.Toprovide;
import com.example.nunepc.beautyblinkbeautician.model.DataPromotion;
import com.example.nunepc.beautyblinkbeautician.model.KeepStatus;
import com.example.nunepc.beautyblinkbeautician.model.RequestData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by NunePC on 18/11/2559.
 */

public class RequestFragment extends Fragment {

    private TextView postP,statusDes;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference, dref;
    private LinearLayout createReq;
    private String test;
    private Button btnStatus;
    private ImageView btnMsg;

    public RequestFragment(){ super(); }

    public static RequestFragment newInstance(){
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle(); //Argument
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_request,container,false);
        initInstance(rootView);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        //test= ""+mFirebaseUser.getUid().toString();
        //dref = FirebaseDatabase.getInstance().getReference().child("customer-request/0XIvXEgxomQqZ5XHYiNmuJ7MZfy1"+"/"+mFirebaseUser.getUid().toString() +"/"+"status");

        return rootView;
    }

    private void initInstance(View rootView){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("request");

        Log.d("GG","="+test);
        //professor promotion feeds
        recyclerView =(RecyclerView)rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
       // btnStatus = (Button)rootView.findViewById(R.id.btnStat);
        //Order from latest data
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        final FirebaseRecyclerAdapter<RequestData,RequestFragment.RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RequestData, RequestFragment.RequestViewHolder>
                (RequestData.class,R.layout.offercard,RequestFragment.RequestViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(final RequestViewHolder viewHolder, final RequestData model, final int position) {
                //viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setDate(model.getDate());
                viewHolder.setEvent(model.getEvent());
                viewHolder.setMaxprice(model.getMaxprice());
                viewHolder.setService(model.getService());
                //viewHolder.setColor(model.getColor());
                viewHolder.setColorcircle(model.getColor());
                viewHolder.setCurrenttime(model.getCurrenttime());
                viewHolder.setName(model.getName());
                viewHolder.setKeyrequest(model.getKeyrequest());
                btnMsg = (ImageView) viewHolder.mview.findViewById(R.id.btnMessage);
                btnMsg.setOnClickListener(new View.OnClickListener() {
                    final String key = getRef(position).getKey();
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), MessagePage.class);
                        startActivity(intent);
                    }
                });
                //btnStatus = (Button)viewHolder.mview.findViewById(R.id.btnStat);
                statusDes = (TextView)viewHolder.mview.findViewById(R.id.desStatus);
                String s = statusDes.getText().toString();
                RequestData rd = new RequestData();
                rd.setStatus(s);
                //Log.d("GG","="+rd.getTimes());
                String k ="offer";
                Log.d("GG","="+rd.getStatus());
                switch (rd.getStatus()){
                    case "offer":
                        Log.d("Please","="+rd.getStatus());
                        // btnStatus.setOnClickListener(new View.OnClickListener() {
                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            final String cshow = getRef(position).getKey();
                            @Override
                            public void onClick(View v) {

                                HashMap<String, Object> RequestValues = new HashMap<>();
                                RequestValues.put("key",cshow);
                                RequestValues.put("service", model.getService());
                                RequestValues.put("event", model.getEvent());
                                RequestValues.put("numberofperson", model.getNumberofperson());
                                RequestValues.put("maxprice", model.getMaxprice());
                                RequestValues.put("date", model.getDate());
                                RequestValues.put("time", model.getTime());
                                RequestValues.put("location", model.getLocation());
                                RequestValues.put("specialrequest", model.getSpecialrequest());
                                RequestValues.put("offer",model.getStatus());
                                RequestValues.put("uid", mFirebaseUser.getUid().toString());
                                RequestValues.put("name", model.getName());
                                Intent intent = new Intent(getActivity(),OfferPage.class);
                                intent.putExtra("request",  RequestValues);
                                startActivity(intent);
                            }
                        });
                        break;
                    case "confirm":
                        Log.d("Bye","="+rd.getStatus());
                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            final String cshow = getRef(position).getKey();
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getActivity(), Confirm.class);
                                startActivity(intent);
                            }
                        });

                        break;
                    case "toprovide":
                        Log.d("provide","="+rd.getStatus());
                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            final String cshow = getRef(position).getKey();
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), Toprovide.class);
                                startActivity(intent);

                            }
                        });
                }
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
    public static class RequestViewHolder extends RecyclerView.ViewHolder  {

        View mview;
        public RequestViewHolder(View itemView){
            super(itemView);
            mview=itemView;

        }
        public void setKeyrequest(String keyrequest){
            TextView tv = (TextView)mview.findViewById(R.id.keyC);
            tv.setText(keyrequest);
        }
        public void setName(String name){
            TextView n = (TextView)mview.findViewById(R.id.tname);
            n.setText(name);
        }
        public void setDate(String date){
            TextView post_date= (TextView)mview.findViewById(R.id.date_des);
            post_date.setText(date);
        }
        public void setEvent(String promotion){
            TextView post_event = (TextView)mview.findViewById(R.id.event_des);
            post_event.setText(promotion);

        }
        public void setMaxprice(String maxprice){
            TextView post_maxprice = (TextView)mview.findViewById(R.id.price_des);
            post_maxprice.setText(maxprice);
        }
        public void setService(String service){
            TextView post_service = (TextView)mview.findViewById(R.id.tService);
            post_service.setText(service);

        }
        public void setS(String status){
            String s = status;
        }
        public void setStatus(String status){
            KeepStatus kt = new KeepStatus(status);
            //Button post_service = (Button)mview.findViewById(R.id.btnStat);
            //post_service.setText(kt.getStatus());
            TextView tx = (TextView)mview.findViewById(R.id.desStatus);
            tx.setText(status);
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
        public void setColor(String color){
            LinearLayout cC = (LinearLayout)mview.findViewById(R.id.ch_color);
            cC.setBackgroundColor(Color.parseColor(color));

        }
        public void setCurrenttime(String currenttime){
            TextView tm= (TextView)mview.findViewById(R.id.btnTime);
            RequestData r = new RequestData();
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
            tm.setText(""+a+", ");
        }

    }


}
