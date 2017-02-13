package com.example.nunepc.beautyblinkbeautician;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

/**
 * Created by NunePC on 14/2/2560.
 */

public class EditEvent extends AppCompatActivity {

    private DatePicker datePicker;
    private int year, month, day,yyyy,mm,dd,hour1,minute1,hour2,minute2;
    private TextView dateView,time_start,time_end;
    Button date,set_time_start,set_time_end;
    private int mHour, mMinute;

    String input_ans;

    private RadioGroup radioGroup_ques;
    private RadioButton ques;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    EditText title,location,note;
    HashMap<String, Object> plannerValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);

        plannerValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("planner");

        dd = getIntent().getExtras().getInt("day");
        mm = getIntent().getExtras().getInt("month");
        yyyy = getIntent().getExtras().getInt("year");

        //find view by id
        dateView = (TextView) findViewById(R.id.button1);
        time_start = (TextView) findViewById(R.id.time_start);
        time_end = (TextView) findViewById(R.id.time_end);
        date = (Button) findViewById(R.id.date);

        date.setText(dd+"/"+mm+"/"+yyyy);

        set_time_start = (Button) findViewById(R.id.set_time_start);
        set_time_end = (Button) findViewById(R.id.set_time_end);

        title = (EditText) findViewById(R.id.title);
        location = (EditText) findViewById(R.id.location);
        note = (EditText) findViewById(R.id.note);

    }
}
