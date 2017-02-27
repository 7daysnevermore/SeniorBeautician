package com.example.nunepc.beautyblinkbeautician.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.AddEvent;
import com.example.nunepc.beautyblinkbeautician.MainActivity;
import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.ShowEvent;
import com.example.nunepc.beautyblinkbeautician.decorators.EventDecorator;
import com.example.nunepc.beautyblinkbeautician.decorators.HighlightWeekendsDecorator;
import com.example.nunepc.beautyblinkbeautician.decorators.MySelectorDecorator;
import com.example.nunepc.beautyblinkbeautician.decorators.OneDayDecorator;
import com.example.nunepc.beautyblinkbeautician.model.DataGallery;
import com.example.nunepc.beautyblinkbeautician.model.DataPlanner;
import com.example.nunepc.beautyblinkbeautician.model.PlannerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by NunePC on 18/11/2559.
 */

public class PlannerFragment extends Fragment implements OnDateSelectedListener, View.OnClickListener {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    MaterialCalendarView materialCalendarView;
    Integer dd,mm,yyyy;
    private RecyclerView recyclerView;
    View view;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference databaseReference;


    public PlannerFragment(){ super(); }

    public static PlannerFragment newInstance(){
        PlannerFragment fragment = new PlannerFragment();
        Bundle args = new Bundle(); //Argument
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_planner,container,false);
        initInstance(rootView);
        return rootView;
    }

    private void initInstance(View rootView){

        view = rootView;
        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        materialCalendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);

        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        materialCalendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        materialCalendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        materialCalendarView.addDecorators(
                new MySelectorDecorator(getActivity()),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());


        rootView.findViewById(R.id.add_event).setOnClickListener(this);

    }

    @Override
    public void onStart(){ super.onStart();

    }

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

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        dd = date.getDay();
        mm = month(date.getMonth());
        yyyy = date.getYear();

        if(dd!=0){PlannerAdapter(dd,mm,yyyy);}

    }

    private int month(int m){
        int mon=0;

        if (m == Calendar.JANUARY){
            mon = 1;
        }
        if (m == Calendar.FEBRUARY){
            mon = 2;
        }
        if (m == Calendar.MARCH) {
            mon = 3;
        }
        if (m == Calendar.APRIL) {
            mon = 4;
        }
        if (m == Calendar.MAY) {
            mon = 5;
        }
        if (m == Calendar.JUNE) {
            mon = 6;
        }
        if (m == Calendar.JULY) {
            mon = 7;
        }
        if (m == Calendar.AUGUST) {
            mon = 8;
        }
        if (m == Calendar.SEPTEMBER) {
            mon = 9;
        }
        if (m == Calendar.OCTOBER) {
            mon = 10;
        }
        if (m == Calendar.NOVEMBER) {
            mon = 11;
        }
        if (m == Calendar.DECEMBER) {
            mon = 12;
        }

        return mon;
    }

    private void PlannerAdapter(int day,int month,int year) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("beautician-planner")
                .child(mFirebaseUser.getUid().toString()).child(day+"-"+month+"-"+year);

        //professor promotion feeds
        recyclerView =(RecyclerView)view.findViewById(R.id.recyclerview_plan);
        recyclerView.setHasFixedSize(true);

        //Order from latest data
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final FirebaseRecyclerAdapter<DataPlanner,PlannerViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataPlanner, PlannerViewHolder>
                (DataPlanner.class,R.layout.planner_row,PlannerViewHolder.class,databaseReference) {

            @Override
            protected void populateViewHolder(PlannerViewHolder viewHolder, final DataPlanner model, final int position) {


                    viewHolder.setTitle(model.title);
                    viewHolder.setStart(model.start);
                    viewHolder.setEnd(model.end);

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    //private static final String TAG = "Promotion";
                    final String cshow = getRef(position).getKey();
                    @Override
                    public void onClick(View view) {

                        HashMap<String, Object> PlannerValues = new HashMap<>();
                        PlannerValues.put("title",model.getTitle());
                        PlannerValues.put("location",model.getLocation());
                        PlannerValues.put("note",model.getNote());
                        PlannerValues.put("day",model.getDay());
                        PlannerValues.put("month",model.getMonth());
                        PlannerValues.put("year", model.getYear());
                        PlannerValues.put("start", model.getStart());
                        PlannerValues.put("end", model.getEnd());
                        PlannerValues.put("uid", model.getUid());
                        PlannerValues.put("status", model.getStatus());
                        Intent cPro = new Intent(getActivity(),ShowEvent.class);
                        cPro.putExtra("planner",  PlannerValues);
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


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_event:
                Intent intent = new Intent(getActivity(),AddEvent.class);
                intent.putExtra("day",dd);
                intent.putExtra("month", mm);
                intent.putExtra("year", yyyy);
                startActivity(intent);
                break;
        }
    }


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final CalendarDay[] day = new CalendarDay[1];
            final Calendar calendar = Calendar.getInstance();
            final ArrayList<CalendarDay> dates = new ArrayList<>();


            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("beautician-planner").child(mFirebaseUser.getUid());

            mRootRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot dateChild : dataSnapshot.getChildren()){



                        for (DataSnapshot startChild : dateChild.getChildren()){

                            DataPlanner planner = startChild.getValue(DataPlanner.class);

                            if (planner == null) {
                                Toast.makeText(getActivity(), "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                            } else {
                                int mon = Integer.parseInt(planner.getMonth())-1;
                                int da = Integer.parseInt(planner.getDay());
                                calendar.set(Integer.parseInt(planner.getYear()),mon,da);
                                day[0] = CalendarDay.from(calendar);
                                dates.add(day[0]);
                                Toast.makeText(getActivity(), " "+day[0], Toast.LENGTH_LONG).show();

                            }


                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }

            });

            mRootRef.onDisconnect();


            return dates;
        }


        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));

        }
    }
}

