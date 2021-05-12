package com.fabrika.gunes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.media.MediaDataSource;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityLogin extends AppCompatActivity {

    SharedPreferences preferences;
    EditText e_username;
    public static long avatar=-1;
    String username;
    ArrayList<Integer> avatars;
    MyCustomGridViewAdapter adapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prepareMe();

        addAvatars();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                avatar = position;
//                adapter = new MyCustomGridViewAdapter(getApplicationContext(), avatars, avatar);
//                gridView.setAdapter(adapter);
                MyCustomGridViewAdapter.secilenPos = avatar;
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void addAvatars() {
        avatars.add(R.drawable.avatar1);
        avatars.add(R.drawable.avatar2);
        avatars.add(R.drawable.avatar3);
        avatars.add(R.drawable.avatar4);
        avatars.add(R.drawable.avatar5);
        avatars.add(R.drawable.avatar6);
        avatars.add(R.drawable.avatar7);
        avatars.add(R.drawable.avatar8);
        avatars.add(R.drawable.avatar9);
        avatars.add(R.drawable.avatar10);
        avatars.add(R.drawable.avatar11);
        avatars.add(R.drawable.avatar12);
        avatars.add(R.drawable.avatar13);
        avatars.add(R.drawable.avatar14);
        avatars.add(R.drawable.avatar15);
        avatars.add(R.drawable.avatar16);
        avatars.add(R.drawable.avatar17);
        avatars.add(R.drawable.avatar18);
        avatars.add(R.drawable.avatar19);
        avatars.add(R.drawable.avatar20);
        avatars.add(R.drawable.avatar21);
        avatars.add(R.drawable.avatar22);
        avatars.add(R.drawable.avatar23);
        avatars.add(R.drawable.avatar24);
        avatars.add(R.drawable.avatar25);
        avatars.add(R.drawable.avatar26);
        avatars.add(R.drawable.avatar27);
        avatars.add(R.drawable.avatar28);
        avatars.add(R.drawable.avatar29);
        avatars.add(R.drawable.avatar30);
        avatars.add(R.drawable.avatar31);
        avatars.add(R.drawable.avatar32);
        avatars.add(R.drawable.avatar33);

        adapter.notifyDataSetChanged();
    }

    private void prepareMe() {

        InputMethodManager ipmm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        ipmm.hideSoftInputFromWindow(null, 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBackground));// set status background white


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e_username = findViewById(R.id.e_username);
        gridView = findViewById(R.id.gridview_avatar);
        avatars = new ArrayList<>();
        adapter = new MyCustomGridViewAdapter(this, avatars, avatar);
        gridView.setAdapter(adapter);
    }

    public void clickBack(View view) {
        finish();
    }

    public void clickSend(View view) {

        username = e_username.getText().toString();
        if(username.length()==0){
            e_username.setError("Özüňize gowja bir lakam saýlaň");
        }
        else if(username.length()<3){
            e_username.setError("Lakam iň az 3 harply bolmaly");
        }
        else{

            if(avatar==-1){
                Toast.makeText(this, "Bolanyna görä suratjyk hem saýlap göýberdä...", Toast.LENGTH_SHORT).show();
            }
            else{
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", username);
                editor.putLong("avatar", avatar);
                editor.commit();
                finish();
            }

        }

    }
}