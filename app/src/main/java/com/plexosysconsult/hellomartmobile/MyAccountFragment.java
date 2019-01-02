package com.plexosysconsult.hellomartmobile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import im.delight.android.webview.AdvancedWebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment implements View.OnClickListener {


    View root;
    TextView tvName, tvEmail, tvLabelMyProfile, tvNotLoggedIn;
    Button bLogOut;
    SharedPreferences userSharedPrefs;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_my_account, container, false);
        tvName = (TextView) root.findViewById(R.id.tv_name);
        tvEmail = (TextView) root.findViewById(R.id.tv_email);
        bLogOut = (Button) root.findViewById(R.id.b_logout);
        tvLabelMyProfile = root.findViewById(R.id.tv_label_my_profile);
        tvNotLoggedIn = root.findViewById(R.id.tv_not_logged_in);
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        tvName.setTypeface(myApplicationClass.getBoldTypeface());
        tvEmail.setTypeface(myApplicationClass.getBoldTypeface());
        bLogOut.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelMyProfile.setTypeface(myApplicationClass.getBoldTypeface());
        tvNotLoggedIn.setTypeface(myApplicationClass.getRegularTypeface());

        userSharedPrefs = getActivity().getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);


        if (userSharedPrefs.getBoolean("available", false) == false) {

            tvName.setText("Guest");
            tvEmail.setText("iShop@hellomart.ug");
            tvNotLoggedIn.setVisibility(View.VISIBLE);
            bLogOut.setText("LOGIN");
            bLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            });


        }else {

            String fname = userSharedPrefs.getString("fname", "");
            String lname = userSharedPrefs.getString("lname", "");
            String email = userSharedPrefs.getString("email", "");


            tvName.setText(fname + " " + lname);
            tvEmail.setText(email);

            bLogOut.setOnClickListener(this);
        }



    }


    @Override
    public void onClick(View v) {
        if (v == bLogOut) {


            //Do some logout stuff here and then go to Login Screen
            /*
            1. make available false and go back to login screen
             */


            SharedPreferences.Editor editor;

            editor = userSharedPrefs.edit();
            editor.putBoolean("available", false);
            editor.apply();

            //logOut();

            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();


        }
    }
}
