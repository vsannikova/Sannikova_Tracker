package com.example.sannikova_tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.sannikova_tracker.AddTrackActivity;
import com.example.sannikova_tracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TrackPageFragment extends Fragment {

    private FloatingActionButton addTrack;
    private ListView listView;
    SharedPreferences tracking;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> map;

    public static TrackPageFragment newInstance() {
        TrackPageFragment pageFragment = new TrackPageFragment();
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(map != null) {
            arrayList.clear();
            map.clear();
            for(String track : AddTrackActivity.string_types) {
                if (tracking.getString(track, null) != null) {
                    map = new HashMap<>();
                    map.put("Type", track);
                    map.put("Last", "Сорвался(ась): " + tracking.getString(track + "_last", ""));
                    arrayList.add(map);
                }
            }

            ((SimpleAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_page, null);
        addTrack = view.findViewById(R.id.addTrack);
        listView = view.findViewById(R.id.tracks);

        tracking = getActivity().getSharedPreferences(AddTrackActivity.APP_PREFERENCES, Context.MODE_PRIVATE);



        for(String track : AddTrackActivity.string_types) {
            if (tracking.getString(track, null) != null) {
                map = new HashMap<>();
                map.put("Type", track);
                map.put("Last", "Сорвался(ась): " + tracking.getString(track + "_last", ""));
                arrayList.add(map);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = tracking.edit();
                String key = arrayList.get(position).keySet().toArray()[0].toString();

                String myFormat = "MM/dd/yy HH:mm"; //In which you need put here
                Locale myLocale = new Locale("ru","RU");
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, myLocale);
                Calendar myCalendar = Calendar.getInstance();
                editor.putString(arrayList.get(position).get(key)+"_last", sdf.format(myCalendar.getTime()));
                arrayList.get(position).put("Last", "Сорвался(ась): " + sdf.format(myCalendar.getTime()));
                editor.apply();
                editor.commit();
                ((SimpleAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), arrayList, android.R.layout.simple_list_item_2, new String[]{"Type", "Last"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
        linkButtons();
        return view;
    }

    private void linkButtons(){
        addTrack.setOnClickListener(onAddTrack);
    }

    private View.OnClickListener onAddTrack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AddTrackActivity.class);
            startActivity(intent);
        }
    };
}