package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by NunePC on 12/1/2560.
 */

public class ProfilePromote extends AppCompatActivity implements View.OnClickListener {

    ImageView picpromote, addpromotepic1, addpromotepic2, addpromotepic3;
    TextView namepromote, locationpromote, pricepromote;
    Button promotenow;
    private Uri imageUri = null;

    private int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepromote);

        findViewById(R.id.addpromotepic1).setOnClickListener(this);
        findViewById(R.id.addpromotepic2).setOnClickListener(this);
        findViewById(R.id.addpromotepic3).setOnClickListener(this);
        findViewById(R.id.promotenow).setOnClickListener(this);

        namepromote = (TextView) findViewById(R.id.namepromote);
        locationpromote = (TextView) findViewById(R.id.locationpromote);
        pricepromote = (TextView) findViewById(R.id.pricepromote);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addpromotepic1:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.setFlags(1);
                startActivityForResult(galleryIntent, SELECT_FILE);
                break;
            case R.id.addpromotepic2:
                Intent galleryIntent2 = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent2.setType("image/*");
                startActivityForResult(galleryIntent2, SELECT_FILE);
                break;
            case R.id.addpromotepic3:
                Intent galleryIntent3 = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent3.setType("image/*");
                startActivityForResult(galleryIntent3, SELECT_FILE);
                break;
            case R.id.promotenow:

                break;
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK && data.getFlags() == 1 ){
            imageUri = data.getData();
            addpromotepic1.setImageURI(imageUri);

        }

    }
}
