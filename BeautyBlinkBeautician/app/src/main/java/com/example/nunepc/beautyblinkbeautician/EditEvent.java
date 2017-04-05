package com.example.nunepc.beautyblinkbeautician;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nunepc.beautyblinkbeautician.fragment.GalleryFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.NotiFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PlannerFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.RequestFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.SettingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NunePC on 14/2/2560.
 */

public class EditEvent extends AppCompatActivity implements View.OnClickListener {

    private DatePicker datePicker;
    private int year, month, day,yyyy,mm,dd,hour1,minute1,hour2,minute2;
    private TextView dateView,time_start,time_end,date;
    Button set_time_start,set_time_end,delete;
    private int mHour, mMinute;
    RadioButton no;
    String datekey;
    String startkey;

    String input_ans;

    private RadioGroup radioGroup_ques;
    private RadioButton ques;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    Toolbar toolbar;

    EditText title,location,note;
    HashMap<String, Object> plannerValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        //up button
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        plannerValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("planner");
        //find view by id
        dateView = (TextView) findViewById(R.id.button1);
        time_start = (TextView) findViewById(R.id.time_start);
        time_end = (TextView) findViewById(R.id.time_end);
        date = (TextView) findViewById(R.id.date);

        set_time_start = (Button) findViewById(R.id.set_time_start);
        set_time_end = (Button) findViewById(R.id.set_time_end);

        title = (EditText) findViewById(R.id.title);
        location = (EditText) findViewById(R.id.location);
        note = (EditText) findViewById(R.id.note);

        title.setText(plannerValues.get("title").toString());
        if(plannerValues.get("location") != null){
            location.setText(plannerValues.get("location").toString());
        }
        if (plannerValues.get("note") != null) {
            note.setText(plannerValues.get("note").toString());
        }
        time_start.setText(plannerValues.get("start").toString());
        time_end.setText(plannerValues.get("end").toString());
        date.setText(plannerValues.get("day").toString()+" / "+plannerValues.get("month").toString()+" / "+plannerValues.get("year").toString());

        if(plannerValues.get("status").toString().equals("no")){
            no.setChecked(true);
        }

        findViewById(R.id.editevent).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);

    }

    // up button method
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTime1(View view){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time_start.setText(hourOfDay + " : " + minute);
                        hour1 = hourOfDay;
                        minute1 = minute;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void setTime2(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time_end.setText(hourOfDay + " : " + minute);
                        hour2 = hourOfDay;
                        minute2 = minute;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.editevent:

                radioGroup_ques = (RadioGroup) findViewById(R.id.available);
                int selectedId = radioGroup_ques.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                ques = (RadioButton) findViewById(selectedId);
                input_ans = ques.getText().toString();

                final String input_title = title.getText().toString();
                final String input_location = location.getText().toString();
                final String input_note = note.getText().toString();

                if (TextUtils.isEmpty(input_title)) {
                    Toast.makeText(getApplicationContext(), "Enter title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(String.valueOf(hour1))) {
                    Toast.makeText(getApplicationContext(), "Select time to start event!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(String.valueOf(hour2))) {
                    Toast.makeText(getApplicationContext(), "Select time to end event!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!input_title.equals(null)){

                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                    final HashMap<String, Object> PlannerValues = new HashMap<>();
                    PlannerValues.put("title", input_title);
                    PlannerValues.put("location", input_location);
                    PlannerValues.put("note",input_note);
                    PlannerValues.put("day", plannerValues.get("day").toString());
                    PlannerValues.put("month", plannerValues.get("month").toString());
                    PlannerValues.put("year", plannerValues.get("year").toString());
                    if(hour1==0){
                        PlannerValues.put("start", plannerValues.get("start").toString());
                        startkey = plannerValues.get("start").toString();
                    }else{
                        PlannerValues.put("start", hour1+":"+minute1);
                        startkey = hour1+":"+minute1;
                    }
                    if (hour2 == 0) {
                        PlannerValues.put("end", plannerValues.get("end").toString());
                    } else {
                        PlannerValues.put("end", hour2 + ":" + minute2);
                    }
                    PlannerValues.put("status", input_ans);
                    PlannerValues.put("uid", plannerValues.get("uid").toString());

                    datekey = plannerValues.get("day").toString()+"-"+plannerValues.get("month").toString()
                            +"-"+plannerValues.get("year").toString();


                    Map<String,Object> childUpdate = new HashMap<>();
                    childUpdate.put("/beautician-planner/"+plannerValues.get("uid").toString()+"/"+datekey+"/"+startkey, PlannerValues);

                    mRootRef.updateChildren(childUpdate);

                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);

                }


                break;
            case R.id.delete:

                startkey = plannerValues.get("start").toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query query = ref.child("beautician-planner/"+plannerValues.get("uid").toString()+"/"+datekey+"/"+startkey).orderByChild("end").equalTo(plannerValues.get("end").toString());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            snapshot.getRef().removeValue();

                            Intent intent = new Intent(EditEvent.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

                break;
        }
    }
}
