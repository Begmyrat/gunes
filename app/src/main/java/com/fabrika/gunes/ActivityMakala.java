package com.fabrika.gunes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ActivityMakala extends AppCompatActivity {

    Toolbar toolbar;
    Bundle extras;
    String title="", body="", category="", author="", date="", img_url="", body1="", body2="";
    String id="0", type="";
    TextView t_author, t_title, t_date, t_body1, t_body2, t_dateBelow;
    ImageView i_image;
    FirebaseFirestore db;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private DocumentReference news;
    long view=0;
    int position;
    FloatingActionButton buttonLike;
    SharedPreferences preferences;
    HashSet<String> map;
    String isWifiEnabled = "0", isOnlyWifi = "0";
    String operation="";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makala);

        prepareMe();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        getData();

//        increaseValue(type, operation);
        increaseValue(operation);

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(map.contains(id)){
                    Toast.makeText(ActivityMakala.this, "Bu makalany ozal halapsyňyz... ", Toast.LENGTH_SHORT).show();
                }
                else {
//                    likeData();
//                    increaseValue(type, "article_like_number");
                    increaseValue("article_like_number");
                }
            }
        });

    }

    private void prepareMe() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBackground));// set status background white

//        setHasOptionsMenu(true);//Add this sentence to the menu
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        toolbar.setTitle("");

        db = FirebaseFirestore.getInstance();

        t_author = findViewById(R.id.t_authorBelow);
        t_title = findViewById(R.id.t_title);
        t_date = findViewById(R.id.t_date);
        t_body1 = findViewById(R.id.t_body1);
        t_body2 = findViewById(R.id.t_body2);
        t_dateBelow = findViewById(R.id.t_dateBelow);
        i_image = findViewById(R.id.i_image);
        buttonLike = findViewById(R.id.buttonLike);

        extras = getIntent().getExtras();
        map = new HashSet<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isWifiEnabled = preferences.getString("wifi_enabled", "0");
        isOnlyWifi = preferences.getString("wifi_only", "0");

        if(extras!=null){
            type = extras.getString("type");

            if(type.equals("News")){
                buttonLike.setVisibility(View.GONE);
                operation = "news_view_number";
            }
            else{
                operation = "article_view_number";
            }

            id = extras.getString("id");
            position = extras.getInt("position");
            title = extras.getString("title");
            body = extras.getString("body");
            body += "\n\n\n\n";
            category = extras.getString("category");
            author = extras.getString("author");
            date = extras.getString("date");
            img_url = extras.getString("img_url");

            int count = 0;
            int index = 0;

            while(count<2 && index<body.length()){
                if((body.charAt(index)=='.' && body.charAt(index+1)!='.') || body.charAt(index)=='?' || body.charAt(index)=='!'){
                    count++;
                }
                index++;
            }

            body1 = body.substring(0, index);
            body2 = body.substring(index);
        }

        t_author.setText(author);
        t_title.setText(title);
        t_date.setText(date);
        t_dateBelow.setText(date);
        t_body1.setText(body1);
        t_body2.setText(body2);

        String url = img_url;
        if(isOnlyWifi.equals("1") && isWifiEnabled.equals("0")){
            url = "";
        }

        try {
            Glide
                    .with(getApplicationContext())
                    .load(new URL(url))
                    .centerCrop()
                    .placeholder(R.drawable.mini_map)
                    .into(i_image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String s = preferences.getString("liked_articles", "");
        String[] sa = s.split(" ");
        for(int i=0;i<sa.length;i++){
            map.add(sa[i]);
        }
    }

    public void increaseValue(String operation) {

        final DocumentReference sfDocRef = db.collection(type).document(id);

        db.runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {

            @Nullable
            @Override
            public Void apply(@NonNull com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                double newPopulation = snapshot.getDouble(operation) + 1;
                transaction.update(sfDocRef, operation, newPopulation);

                return null;
            }
        });

        if(operation.equals("article_like_number") && !map.contains(id)){
            String s = preferences.getString("liked_articles","");
            s += id+" ";
            map.add(id);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("liked_articles", s);
            editor.commit();

            Toast.makeText(ActivityMakala.this, "Halanan makalalara goşuldy.", Toast.LENGTH_SHORT).show();
        }
        else if(operation.equals("article_like_number")){
            Toast.makeText(ActivityMakala.this, "Siz bu makalany öň hem halapsyňyz.", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBack(View view) {
        finish();

    }
}