package com.example.nunepc.beautyblinkbeautician;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Noti_Receive extends AppCompatActivity {
    private String img;
    private ImageView imV;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti__receive);
        TextView txt = (TextView) findViewById(R.id.textView);
        imV = (ImageView)findViewById(R.id.imageView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                //txt.append(key + ": " + value + "\n\n");
                img=value.toString();
            }
        }
        bitmap = getBitmapFromURL(bundle.get("picture_url").toString());
        //imV.setImageBitmap(bitmap);

        txt.setText(bundle.get("status").toString());
    }
    public Bitmap getBitmapFromURL(String src){
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
