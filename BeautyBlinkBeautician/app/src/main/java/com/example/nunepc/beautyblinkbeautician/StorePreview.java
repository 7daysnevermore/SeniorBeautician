package com.example.nunepc.beautyblinkbeautician;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewDetailFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewGalleryFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewPlannerFragment;
import com.example.nunepc.beautyblinkbeautician.fragment.PreviewReviewFragment;

/**
 * Created by CaptainPC on 15/1/2560.
 */

public class StorePreview extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storepreview);

            //fragment
            if(savedInstanceState==null){
                //first create
                //Place fragment
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.previewContainer,new PreviewGalleryFragment())
                        .commit();

                //tab button
                findViewById(R.id.bt_gallery).setOnClickListener(this);
                findViewById(R.id.bt_review).setOnClickListener(this);
                findViewById(R.id.bt_planner).setOnClickListener(this);
                findViewById(R.id.bt_detail).setOnClickListener(this);
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gallery:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewGalleryFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_review:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewReviewFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_planner:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewPlannerFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_detail:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.previewContainer, PreviewDetailFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
