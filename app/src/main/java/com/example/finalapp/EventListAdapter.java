//package com.example.finalapp;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//
//public class EventListAdapter extends ArrayAdapter<Events> {
//
//    private static final String TAG = "EventListAdapter";
//
//    private Context mcontext;
//    int mresource;
//
//    public EventListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Events> objects) {
//        super(context, resource, objects);
//        mcontext = context;
//        mresource = resource;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        int img = getItem(position).getImage();
//        String mnth = getItem(position).getMonth();
//        String title = getItem(position).getTitle();
//        String day = getItem(position).getDay();
//        String location = getItem(position).getLocation();
//
//        Events events = new Events(img,mnth,title,day,location);
//
//        if(convertView==null){
//            LayoutInflater inflater = LayoutInflater.from(mcontext);
//            convertView = inflater.inflate(mresource, parent, false);
//
//            ImageView imageView = convertView.findViewById(R.id.EventsImg);
//            TextView textmm = convertView.findViewById(R.id.EventsMonth);
//            TextView textdd = convertView.findViewById(R.id.EventsDay);
//            TextView texttitle = convertView.findViewById(R.id.EventsTitle);
//            TextView textlocation = convertView.findViewById(R.id.EventsLocation);
//
//            imageView.setImageResource(img);
//            textmm.setText(mnth);
//            textdd.setText(day);
//            texttitle.setText(title);
//            textlocation.setText(location);
//        }
//        else {
//            convertView.getTag();
//        }
//
//        return convertView;
//    }
//}

package com.example.finalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends ArrayAdapter<Events> {

    private static final String TAG = "EventListAdapter";

    private Context mcontext;
    int mresource;

    static class ViewHolder {
        ImageView vImgView;
        TextView vFname;
        TextView vDate;
        TextView vSuggest;
    }

    public EventListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Events> objects) {
        super(context, resource, objects);
        mcontext = context;
        mresource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        int img = getItem(position).getImage();
        String mnth = getItem(position).getMonth();
        String title = getItem(position).getTitle();
        String day = getItem(position).getDay();
        String location = getItem(position).getLocation();

        Events events = new Events(img,mnth,title,day,location);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mresource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.EventsImg);
        TextView textmm = convertView.findViewById(R.id.EventsMonth);
        TextView textdd = convertView.findViewById(R.id.EventsDay);
        TextView texttitle = convertView.findViewById(R.id.EventsTitle);
        TextView textlocation = convertView.findViewById(R.id.EventsLocation);

        imageView.setImageResource(img);
        textmm.setText(mnth);
        textdd.setText(day);
        texttitle.setText(title);
        textlocation.setText(location);

        return convertView;
    }
}

