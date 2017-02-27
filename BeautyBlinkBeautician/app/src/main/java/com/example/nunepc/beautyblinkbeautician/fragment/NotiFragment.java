package com.example.nunepc.beautyblinkbeautician.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.Confirm;
import com.example.nunepc.beautyblinkbeautician.MessagePage;
import com.example.nunepc.beautyblinkbeautician.OfferPage;
import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.Toprovide;
import com.example.nunepc.beautyblinkbeautician.model.DataNoti;
import com.example.nunepc.beautyblinkbeautician.model.RequestData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by NunePC on 18/11/2559.
 */

public class NotiFragment extends Fragment {

    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private FirebaseUser mFirebaseUser;
    public NotiFragment(){ super(); }

    public static NotiFragment newInstance(){
        NotiFragment fragment = new NotiFragment();
        Bundle args = new Bundle(); //Argument
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_noti,container,false);
        initInstance(rootView);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        return rootView;
    }

    private void initInstance(View rootView) {
        ref = FirebaseDatabase.getInstance().getReference().child("requestnotifromcus");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        final FirebaseRecyclerAdapter<DataNoti, NotiFragment.NotiViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataNoti, NotiFragment.NotiViewHolder>
                (DataNoti.class, R.layout.noticard, NotiFragment.NotiViewHolder.class, ref) {

            @Override
            protected void populateViewHolder(final NotiViewHolder viewHolder,final DataNoti model,final int position) {
                viewHolder.setName(model.getName());
                viewHolder.setDescription(model.getName(),model.getService(),model.getEvent());
                viewHolder.setCurrenttime(model.getCurrenttime());

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


    public static class NotiViewHolder extends RecyclerView.ViewHolder {
        View mview;
        public NotiViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setName(String name){
            TextView txname = (TextView)mview.findViewById(R.id.tname);
            txname.setText(name);
        }
        public void setDescription(String name,String service,String event){
            TextView txdes = (TextView)mview.findViewById(R.id.des);
            txdes.setText("need to call"+service+"for"+event+"event");
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
            tm.setText(""+a);


        }
    }
}
