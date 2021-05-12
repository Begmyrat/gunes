package com.fabrika.gunes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

public class ActivitySettings extends AppCompatActivity {

    Switch s_wifi, s_darkLight;
    SharedPreferences preferences;
    RadioButton r_dark, r_light;
    DayNightSwitch dayNightSwitch2;
    ImageView i_logo;
    TextView t_title;

    ImageView sun, dayland, nightland;
    View daySky, nightSky;
    DayNightSwitch dayNightSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prepareMe();

        s_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("wifi_only", "1");
                    editor.commit();
                }
                else{
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("wifi_only", "0");
                    editor.commit();
                }
            }
        });

        s_darkLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorNight));// set status background white
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("dark_mode", "1");
                    editor.commit();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorDay));// set status background white
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("dark_mode", "0");
                    editor.commit();
                }
            }
        });
    }

    private void prepareMe() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBackground));// set status background white

        s_wifi = findViewById(R.id.s_wifi);
        s_darkLight = findViewById(R.id.s_darkLight);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(preferences.getString("wifi_only","").equals("1")){
            s_wifi.setChecked(true);
        }
        else{
            s_wifi.setChecked(false);
        }

        if(preferences.getString("dark_mode","").equals("1")){
            s_darkLight.setChecked(true);
        }
        else{
            s_darkLight.setChecked(false);
        }

    }

    public void clickBack(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}