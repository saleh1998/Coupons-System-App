package com.example.databases;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SupportClass {

    private static SupportClass instance = null;
    private Context context;

    private SupportClass(Context context) {
        this.context = context;
    }
    public static SupportClass getInstance(Context context) {
        if (instance == null) instance = new SupportClass(context);
        return instance;
    }

    public Date getDate(String dateToConvert){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = null;

        try {
            date = dateFormat.parse(dateToConvert);
        } catch (ParseException e) {
            Toast.makeText(context,"Wrong date format, should be: 'yyyy-MM-dd' ", Toast.LENGTH_SHORT).show();
        }
        return date;
    }
}
