package com.example.finalapp.SuggestionPage;

import android.widget.EditText;
import android.widget.TextView;

import com.example.finalapp.R;
import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestUpload {

    private String s_ID;
    private String s_cImgView;
    private String s_Name;
    private String s_Date;
    private String s_suggest;
    private String s_likes;

    public SuggestUpload() {
        //No arg-Contructor
    }

//    ---------------partially remove imgView in here aswell (Constructor, Getter, Setter)--------

    public SuggestUpload(String s_cImgView, String s_Name, String s_Date, String s_suggest, String s_likes) {
        this.s_cImgView = s_cImgView;
        this.s_Name = s_Name;
        this.s_Date = s_Date;
        this.s_suggest = s_suggest;
        this.s_likes = s_likes;
    }

    @Exclude
    public String getS_ID() {
        return s_ID;
    }

    public void setS_ID(String s_ID) {
        this.s_ID = s_ID;
    }

    public String getS_cImgView() {
        return s_cImgView;
    }

    public void setS_cImgView(String s_cImgView) {
        this.s_cImgView = s_cImgView;
    }

    public String getS_Name() {
        return s_Name;
    }

    public void setS_Name(String s_Name) {
        this.s_Name = s_Name;
    }

    public String getS_Date() {
        return s_Date;
    }

    public void setS_Date(String s_Date) {
        this.s_Date = s_Date;
    }

    public String getS_suggest() {
        return s_suggest;
    }

    public void setS_suggest(String s_suggest) {
        this.s_suggest = s_suggest;
    }

    public String getS_likes() {
        return s_likes;
    }

    public void setS_likes(String s_likes) {
        this.s_likes = s_likes;
    }
}


