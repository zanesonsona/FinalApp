package com.example.finalapp.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private CardView seniorcitezen,pwd;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        seniorcitezen = view.findViewById(R.id.cv_senior);
        pwd = view.findViewById(R.id.cv_pwd);

        seniorcitezen.setOnClickListener(this);
        pwd.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cv_senior:
                String Senior_URL = "https://rpts.cebucity.gov.ph/Senior.aspx";
                Intent intent_senior = new Intent(Intent.ACTION_VIEW);
                intent_senior.setData(Uri.parse(Senior_URL));
                startActivity(intent_senior);
                break;

            case R.id.cv_pwd:
                String Pwd_URL = "https://rpts.cebucity.gov.ph/PWD.aspx";
                Intent intent_pwd = new Intent(Intent.ACTION_VIEW);
                intent_pwd.setData(Uri.parse(Pwd_URL));
                startActivity(intent_pwd);
                break;
        }

    }
}
