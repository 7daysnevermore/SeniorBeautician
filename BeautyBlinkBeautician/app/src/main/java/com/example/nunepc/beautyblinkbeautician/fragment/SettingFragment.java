package com.example.nunepc.beautyblinkbeautician.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.EmailLogin;
import com.example.nunepc.beautyblinkbeautician.ProfilePromote;
import com.example.nunepc.beautyblinkbeautician.Promotion;
import com.example.nunepc.beautyblinkbeautician.R;
import com.example.nunepc.beautyblinkbeautician.Verified;
import com.example.nunepc.beautyblinkbeautician.ViewProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;

/**
 * Created by CaptainPC on 9/1/2560.
 */

public class SettingFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private TextView viewProfile;
    private TextView profilepromote;
    private TextView verified;
    private TextView promotion;
    private TextView accountsetting;
    private TextView support;
    private TextView logout;

    public SettingFragment(){ super(); }

    public static SettingFragment newInstance(){
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle(); //Argument
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_setting,container,false);
        initInstance(rootView);
        return rootView;

    }

    private void initInstance(View rootView){

        viewProfile = (TextView) rootView.findViewById(R.id.viewProfile);
        profilepromote = (TextView) rootView.findViewById(R.id.profilePromote);
        verified = (TextView) rootView.findViewById(R.id.verified);
        promotion = (TextView) rootView.findViewById(R.id.promotion);
        accountsetting = (TextView) rootView.findViewById(R.id.accountSetting);
        support = (TextView) rootView.findViewById(R.id.support);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewProfile.class));
            }
        });

        profilepromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfilePromote.class));
            }
        });

        verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Verified.class));
            }
        });

        promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), Promotion.class));
            }
        });


        logout = (TextView) rootView.findViewById(R.id.logout);

        //Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();

            }
        });


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
