package com.example.sannikova_tracker;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sannikova_tracker.fragments.MainPageFragment;
import com.example.sannikova_tracker.fragments.TrackPageFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    static final int PAGE_COUNT = 2;
    MyFragmentPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ViewPager pager = findViewById(R.id.view_pager);

        TabLayout tabs = findViewById(R.id.tabLayout);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0){
                    ((MainPageFragment)pagerAdapter.getItem(i)).updateView();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabs.setupWithViewPager(pager);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter  {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        ArrayList<Fragment> fragments = new ArrayList<>();

        @Override
        public Fragment getItem(int position) {
            Fragment fr = null;
            if(fragments.size() >= position+1){
                return fragments.get(position);
            }
            switch (position){
                case 0:
                    fr = new MainPageFragment();
                    break;
                case 1:
                    fr = new TrackPageFragment();
                    break;
            }
            fragments.add(position, fr);

            return fr;
        }

        // tab titles
        private int[] tabTitlesIds = new int[]{R.string.main_page, R.string.track_page};

        // overriding getPageTitle()
        @Override
        public CharSequence getPageTitle(int position) {
            return getString(tabTitlesIds[position]);
        }


        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
