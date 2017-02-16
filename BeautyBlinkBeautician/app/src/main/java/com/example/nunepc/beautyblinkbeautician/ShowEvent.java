package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by NunePC on 14/2/2560.
 */

public class ShowEvent extends AppCompatActivity implements View.OnClickListener {

    HashMap<String, Object> plannerValues;
    private TextView edit,title,location,note,date,time_start,time_end,answer;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showevent);

        //up button
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.title);
        location = (TextView) findViewById(R.id.location);
        note = (TextView) findViewById(R.id.note);
        date = (TextView) findViewById(R.id.date);
        time_start = (TextView) findViewById(R.id.time_start);
        time_end = (TextView) findViewById(R.id.time_end);
        answer = (TextView) findViewById(R.id.answer);

        plannerValues = (HashMap<String, Object>) getIntent().getExtras().getSerializable("planner");

        title.setText(plannerValues.get("title").toString());
        if(plannerValues.get("location").equals(null)){
            location.setVisibility(View.VISIBLE);
            location.setText(plannerValues.get("location").toString());
        }
        if (plannerValues.get("note").equals(null)) {
            note.setVisibility(View.VISIBLE);
            note.setText(plannerValues.get("note").toString());
        }
        time_start.setText(plannerValues.get("start").toString());
        time_end.setText(plannerValues.get("end").toString());
        date.setText(plannerValues.get("day").toString()+" / "+plannerValues.get("month").toString()+" / "+plannerValues.get("year").toString());
        answer.setText(plannerValues.get("status").toString());

        edit = (TextView) findViewById(R.id.edit);
        edit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                Intent cPro = new Intent(this,EditEvent.class);
                cPro.putExtra("planner",  plannerValues);
                startActivity(cPro);
                break;

        }
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
}
