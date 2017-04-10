package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.model.DataBank;
import com.example.nunepc.beautyblinkbeautician.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyBusiness extends AppCompatActivity {
    private TextView name;
    private RecyclerView recyclerView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private EditText bank,acc_num;
    private Button add;
    String uid;
    Toolbar toolbar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business);

        //up button
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid().toString();
        name = (TextView) findViewById(R.id.name);
        bank = (EditText) findViewById(R.id.bank);
        acc_num = (EditText) findViewById(R.id.acc_num);
        add = (Button) findViewById(R.id.add);

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("beautician/"+uid);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null) {
                    } else {
                        User user = dataSnapshot.getValue(User.class);
                        name.setText(user.firstname+" "+user.lastname);
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String banking = bank.getText().toString();
                final String number = acc_num.getText().toString();

                if(!TextUtils.isEmpty(banking) && !TextUtils.isEmpty(number) ){

                    final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mBankRef = mRootRef.child("bank");
                    final String key = mBankRef.push().getKey();

                    final HashMap<String, Object> BankValues = new HashMap<String, Object>();
                    BankValues.put("bank", banking);
                    BankValues.put("number", number);


                    final Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put("/bank/" + key, BankValues);
                    childUpdate.put("/beautician-bank/" + uid+"/"+key, BankValues);
                    mRootRef.updateChildren(childUpdate);

                    Intent intent = new Intent(MyBusiness.this, MyBusiness.class);
                    startActivity(intent);

                }


            }

        });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("beautician-bank/"+uid);
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("request");

        //professor promotion feeds
        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Order from latest data
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        final FirebaseRecyclerAdapter<DataBank,MyBusiness.RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataBank, MyBusiness.RequestViewHolder>
                (DataBank.class,R.layout.bank_row,MyBusiness.RequestViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(final MyBusiness.RequestViewHolder viewHolder, final DataBank model, final int position) {

                viewHolder.setBank(model.bank);
                viewHolder.setNumber(model.number);

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String cshow = getRef(position).getKey();
                        final DatabaseReference mRootRef1 = FirebaseDatabase.getInstance().getReference().child("beautician-bank").child(uid).child(cshow);
                        final DatabaseReference mRootRef2 = FirebaseDatabase.getInstance().getReference().child("bank").child(cshow);

                        mRootRef1.removeValue();
                        mRootRef2.removeValue();

                        Intent intent = new Intent(MyBusiness.this, MyBusiness.class);
                        startActivity(intent);
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


    }

    // up button method
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                Intent intent = new Intent(MyBusiness.this,MainActivity.class);
                intent.putExtra("menu","setting");
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder  {

        public ImageView delete;
        View mview;
        public RequestViewHolder(View itemView){
            super(itemView);
            mview=itemView;
            delete= (ImageView)mview.findViewById(R.id.delete);

        }

        public void setBank(String bank){
            TextView tv  =(TextView)mview.findViewById(R.id.bank);
            tv.setText(bank);
        }

        public void setNumber(String number) {
            TextView tv = (TextView) mview.findViewById(R.id.number);
            tv.setText(number);
        }


    }

    }







