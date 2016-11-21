package com.example.nunepc.beautyblinkbeautician.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nunepc.beautyblinkbeautician.R;

/**
 * Created by NunePC on 18/11/2559.
 */

public class PlannerFragment extends Fragment {

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
