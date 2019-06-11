package com.example.sannikova_tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sannikova_tracker.AddTrackActivity;
import com.example.sannikova_tracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainPageFragment extends Fragment {

    SharedPreferences tracking;
    TextView timeText;


    public static MainPageFragment newInstance() {
        MainPageFragment pageFragment = new MainPageFragment();
        return pageFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page, null);

        tracking = getActivity().getSharedPreferences(AddTrackActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        timeText = view.findViewById(R.id.timeText);
        List<Date> dates = new ArrayList<Date>();
        for(String track : AddTrackActivity.string_types) {
            if (tracking.getString(track, null) != null) {
                String timestr = tracking.getString(track + "_last", "");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm");
                Date date;
                try {
                    date = format.parse(timestr);
                    dates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        Date max = Collections.max(dates);
        timeText.setText(getDifference(max, new Date()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(tracking != null){
            List<Date> dates = new ArrayList<Date>();
            for(String track : AddTrackActivity.string_types) {
                if (tracking.getString(track, null) != null) {
                    String timestr = tracking.getString(track + "_last", "");
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm");
                    Date date;
                    try {
                        date = format.parse(timestr);
                        dates.add(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            Date max = Collections.max(dates);
            timeText.setText(getDifference(max, new Date()));
        }
    }


    public String getDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return String.format(new Locale("ru","RU"),"%d дн., %d ч., %d мин., %d сек.%n",elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    public void updateView() {

        if(tracking != null){
            List<Date> dates = new ArrayList<Date>();
            for(String track : AddTrackActivity.string_types) {
                if (tracking.getString(track, null) != null) {
                    String timestr = tracking.getString(track + "_last", "");
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm");
                    Date date;
                    try {
                        date = format.parse(timestr);
                        dates.add(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            Date max = Collections.max(dates);
            timeText.setText(getDifference(max, new Date()));
        }
    }
}