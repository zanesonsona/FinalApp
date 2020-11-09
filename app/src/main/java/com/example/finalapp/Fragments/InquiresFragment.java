package com.example.finalapp.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.finalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InquiresFragment extends Fragment {


    public InquiresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inquires, container, false);



        return view;
    }

}
