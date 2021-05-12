package com.fabrika.gunes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar smoothBottomBar;
    ViewPager viewPager;
    ArrayList<Fragment> fragmentList;
    PagerAdapter pagerAdapter;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareMe();

//        llprogress.setVisibility(View.VISIBLE);

        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                viewPager.setCurrentItem(i);

                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                smoothBottomBar.setItemActiveIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void prepareMe() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBackground));// set status background white

        smoothBottomBar = findViewById(R.id.smoothBottomBar);
        viewPager = findViewById(R.id.viewpager);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String darkMode = preferences.getString("dark_mode","0");

        if(darkMode.equals("1")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentHabar());
        fragmentList.add(new FragmentMakala());
        fragmentList.add(new FragmentBlog());
        fragmentList.add(new FragmentHasap());


        pagerAdapter = new PagerAdapter(this, getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);


    }
}