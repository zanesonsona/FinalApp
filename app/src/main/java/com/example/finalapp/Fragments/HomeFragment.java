package com.example.finalapp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.finalapp.EventListAdapter;
import com.example.finalapp.Events;
import com.example.finalapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listView = view.findViewById(R.id.lvHome);

        Events event1 = new Events(R.drawable.sample_event, "MAR", "Summer League", "29", "Open Court");
        Events event3 = new Events(R.drawable.sample_event3, "DEC", "Christmas Party", "19", "Brgy. Hall Kamagayan");
        Events event2 = new Events(R.drawable.sample_event2, "OCT", "Christmas League", "22", "Gymnassium");
        Events event4 = new Events(R.drawable.sample_event, "JUN", "Summer League", "12", "Open Court");
        Events event5 = new Events(R.drawable.sample_event, "JUL", "Summer League", "09", "Open Court");

        ArrayList<Events> eventlist = new ArrayList<>();
        eventlist.add(event1);
        eventlist.add(event3);
        eventlist.add(event2);
        eventlist.add(event4);
        eventlist.add(event5);

        EventListAdapter adapter = new EventListAdapter( getActivity(), R.layout.events_model, eventlist);
        listView.setAdapter(adapter);

        return view;
    }

}
