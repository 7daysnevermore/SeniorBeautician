package com.example.nunepc.beautyblinkbeautician.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nunepc.beautyblinkbeautician.R;

/**
 * Created by CaptainPC on 15/1/2560.
 */

public class PreviewDetailFragment extends Fragment{
    public PreviewDetailFragment(){ super(); }

    public static PreviewDetailFragment newInstance(){
        PreviewDetailFragment fragment = new PreviewDetailFragment();
        Bundle args = new Bundle(); //Argument
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_preview_detail,container,false);
        initInstance(rootView);
        return rootView;
    }

    private void initInstance(View rootView){
        // Start Code Here
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
}
