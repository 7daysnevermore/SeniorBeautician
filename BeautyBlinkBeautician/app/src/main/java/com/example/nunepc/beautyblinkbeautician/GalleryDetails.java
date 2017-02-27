package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by NunePC on 26/1/2560.
 */

public class GalleryDetails extends AppCompatActivity {

    HashMap<String, Object> galleryValue;
    ImageView picpost;
    TextView caption,edit,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showgallery);

        caption = (TextView) findViewById(R.id.caption);
        edit = (TextView) findViewById(R.id.edit_gall);
        picpost = (ImageView) findViewById(R.id.picpost);
        name = (TextView) findViewById(R.id.name);

        galleryValue = (HashMap<String, Object>) getIntent().getExtras().getSerializable("gallery");

        Picasso.with(this).load(galleryValue.get("image").toString()).into(picpost);
        name.setText(galleryValue.get("username").toString());
        caption.setText(galleryValue.get("caption").toString());


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cPro = new Intent(GalleryDetails.this,EditGallery.class);
                cPro.putExtra("gallery",  galleryValue);
                startActivity(cPro);

            }
        });
    }
}
