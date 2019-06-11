package com.example.sannikova_tracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTrackActivity extends AppCompatActivity {

    private EditText lastDate, lastTime;
    private Button addTrackButton;
    final Calendar myCalendar = Calendar.getInstance();
    SharedPreferences tracking;
    private Spinner types;
    /*private Toolbar toolbar;*/

    public static final String[] string_types = {"Алкоголь", "Курение", "Еда"};

    public static final String APP_PREFERENCES = "tracking";

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }

    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);/*
        toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }*/
        setContentView(R.layout.add_track);
        lastDate = findViewById(R.id.lastDate);
        lastTime = findViewById(R.id.lastTime);
        addTrackButton = findViewById(R.id.add_track_button);
        types = findViewById(R.id.trackType);
        tracking = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        List<String> types_array = new ArrayList<>();

        for (String string_type : string_types) {
            if (tracking.getString(string_type, null) == null) {
                types_array.add(string_type);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, types_array);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types.setAdapter(adapter);

        addTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastDate.getText().toString().matches("") && lastTime.getText().toString().matches("")){
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTrackActivity.this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Надо заполнить все поля")
                            .setTitle("Ошибка");

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                SharedPreferences.Editor editor = tracking.edit();
                editor.putString(types.getSelectedItem().toString(), types.getSelectedItem().toString());
                String myFormat = "MM/dd/yy HH:mm"; //In which you need put here
                Locale myLocale = new Locale("ru","RU");
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, myLocale);
                editor.putString(types.getSelectedItem().toString()+"_last", sdf.format(myCalendar.getTime()));
                editor.apply();
                editor.commit();
                finish();
            }
        });

        lastDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    openDatePicker();
                }
            }
        });

        lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        lastTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    openTimePicker();
                }
            }
        });

        lastTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });
    }

    private void openDatePicker(){
        DatePickerDialog dp = new DatePickerDialog(AddTrackActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dp.getDatePicker().setMaxDate(new Date().getTime());
        dp.show();
    }

    private void openTimePicker(){
        TimePickerDialog tp = new TimePickerDialog(AddTrackActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
        tp.show();
    }

    private void updateDateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        Locale myLocale = new Locale("ru","RU");
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, myLocale);

        lastDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTimeLabel() {
        String myFormat = "HH:mm"; //In which you need put here
        Locale myLocale = new Locale("ru","RU");
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, myLocale);

        lastTime.setText(sdf.format(myCalendar.getTime()));
    }


}
