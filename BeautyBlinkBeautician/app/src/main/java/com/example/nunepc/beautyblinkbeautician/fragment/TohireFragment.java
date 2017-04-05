package com.example.nunepc.beautyblinkbeautician.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nunepc.beautyblinkbeautician.HiredDetails;
import com.example.nunepc.beautyblinkbeautician.MessagePage;
import com.example.nunepc.beautyblinkbeautician.OfferPage;
import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.model.RequestData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class TohireFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference, dref;
    private ImageView btnMsg;
    private Query dataQuery1;

    public TohireFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tohire,container,false);
        initInstance(rootView);
        return rootView;
    }

    private void initInstance(View rootView){

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("beautician-received").child(mFirebaseUser.getUid());
        final DatabaseReference databaseRef = databaseReference.getRef();
        dataQuery1 = databaseRef.orderByChild("status").equalTo("2");

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
                (RequestData.class,R.layout.offercard,RequestFragment.RequestViewHolder.class,dataQuery1) {

            @Override
            protected void populateViewHolder(final RequestFragment.RequestViewHolder viewHolder, final RequestData model, final int position) {
                if(model.userprofile!=null){
                    viewHolder.setProfile(getActivity().getApplicationContext(),model.userprofile);
                }
                viewHolder.setStatus(model.status);
                viewHolder.setDate(model.date);
                viewHolder.setEvent(model.event);
                viewHolder.setMaxprice(model.maxprice);
                viewHolder.setService(model.service);
                viewHolder.setCurrenttime(model.currenttime);
                viewHolder.setName(model.username);
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
                /*statusDes = (TextView)viewHolder.mview.findViewById(R.id.desStatus);
                String s = statusDes.getText().toString();
                RequestData rd = new RequestData();
                rd.setStatus(s);
                //Log.d("GG","="+rd.getTimes());
                String k ="offer";
                Log.d("GG","="+rd.getStatus());
                switch (rd.getStatus()){
                    case "offer":*/
                //Log.d("Please","="+rd.getStatus());
                // btnStatus.setOnClickListener(new View.OnClickListener() {
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    final String cshow = getRef(position).getKey();
                    @Override
                    public void onClick(View v) {

                        HashMap<String, Object> RequestValues = new HashMap<>();
                        RequestValues.put("key",cshow);
                        RequestValues.put("service", model.service);
                        RequestValues.put("event", model.event);
                        RequestValues.put("numberofperson", model.numberofperson);
                        RequestValues.put("maxprice", model.maxprice);
                        RequestValues.put("date", model.date);
                        RequestValues.put("time", model.time);
                        RequestValues.put("location", model.location);
                        RequestValues.put("specialrequest", model.specialrequest);
                        RequestValues.put("status",model.status);
                        RequestValues.put("beauid", mFirebaseUser.getUid().toString());
                        RequestValues.put("custid", model.customerid);
                        RequestValues.put("name", model.username);
                        RequestValues.put("profilecust", model.userprofile);
                        RequestValues.put("requestpic", model.reqpic);
                        RequestValues.put("currenttime", model.currenttime);

                        if(model.status.equals("1")){
                            Intent intent = new Intent(getActivity(),OfferPage.class);
                            intent.putExtra("request",  RequestValues);
                            startActivity(intent);
                        }
                        if (model.status.equals("3") ||model.status.equals("2") || model.status.equals("4") || model.status.equals("5")|| model.status.equals("6")|| model.status.equals("7")) {
                            Intent intent = new Intent(getActivity(), HiredDetails.class);
                            intent.putExtra("status",model.status);
                            intent.putExtra("request", RequestValues);
                            startActivity(intent);
                        }
                    }
                });
                        /*break;
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

                            }*/
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

    public static TohireFragment newInstance(){
        TohireFragment fragment = new TohireFragment();
        Bundle args = new Bundle(); //Argument
        fragment.setArguments(args);
        return fragment;
    }
}
