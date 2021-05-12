package com.fabrika.gunes;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class FragmentHabar extends Fragment implements MyHabarRecycleListAdapter.ItemClickListener{

    public static View view;
    Toolbar toolbar;
    EditText e_search;
    public static RecyclerView recyclerView;
    public static ArrayList<HabarObject> habarList, habarListSearch;
    public static MyHabarRecycleListAdapter habarAdapter;
    StaggeredGridLayoutManager layoutManager;
    public static MainActivity activity;
    HashMap<String, Boolean> clickMap;
    SharedPreferences preferences;

    public static FirebaseFirestore db;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FloatingActionButton floatingActionButton;
    public static long news_number=0;
    SwipeRefreshLayout myNestedScroll;
    NestedScrollView nestedScrollView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_habar, container, false);

        prepareMe();

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested);;

        myNestedScroll = view.findViewById(R.id.pullToRefresh);


        myNestedScroll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHabar(); // your code
                myNestedScroll.setRefreshing(false);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for admin
//                insertNews();

                startActivity(new Intent(activity.getApplicationContext(), ActivitySettings.class));
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
//                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
//                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
//                    Log.i(TAG, "TOP SCROLL");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    getHabar();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void insertNews() {

        Map<String, Object> info = new HashMap<>();
        info.put("news_author", "Atavatan Türkmenistan");
        info.put("news_body", "");
        info.put("news_category", "Dünýä");
        info.put("news_date", "06.05.2021");
        info.put("news_img_url", "");
        info.put("news_title", "");
        info.put("news_view_number", 0);
        info.put("news_id", news_number);

        db.collection("News")
                .document(""+news_number)
                .set(info);

        news_number++;

        Map<String, Object> article_num = new HashMap<>();
        article_num.put("news_number", news_number);

        db.collection("News")
                .document("0")
                .set(article_num);

    }

    private void prepareMe() {

//        ((MainActivity)getActivity()).updateStatusBarColor("#ffffff");

        setHasOptionsMenu(true);//Add this sentence to the menu
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isWifiEnabled();

        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        db = FirebaseFirestore.getInstance();

        e_search = view.findViewById(R.id.e_search);
        toolbar.setTitle("Habarlar");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHabar();
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
//        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        habarList = new ArrayList<>();
        habarListSearch = new ArrayList<>();
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getHabar();
        habarAdapter = new MyHabarRecycleListAdapter(activity, habarList);
        recyclerView.setAdapter(habarAdapter);
        habarAdapter.setClickListener(this);

//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("read_news", "");
//        editor.commit();

        clickMap = new HashMap<>();
    }

    public static void getHabar() {

        view.findViewById(R.id.llProgressBar).setVisibility(View.VISIBLE);

//        habarList.clear();

        int size = habarList.size();
        if(size>0)
            size = size -1;

        if(habarList.size()==0){
            db.collection("News").orderBy("news_id", Query.Direction.DESCENDING).limit(10).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty()){

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                for(DocumentSnapshot d : list){
                                    HabarObject habarObject = d.toObject(HabarObject.class);
                                    habarObject.setId(d.getId());

                                    if(habarObject.getId().equals("0")){
                                        news_number = habarObject.getNews_number();
//                                    Toast.makeText(activity, ""+news_number, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        habarList.add(habarObject);
                                    }
                                }

                                habarAdapter.notifyDataSetChanged();

                                view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);
                }
            });
        }
        else{
            Toast.makeText(activity, "size: " + size, Toast.LENGTH_SHORT).show();
            db.collection("News").orderBy("news_id", Query.Direction.DESCENDING).startAt(size).limit(10).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty()){

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                for(DocumentSnapshot d : list){
                                    HabarObject habarObject = d.toObject(HabarObject.class);
                                    habarObject.setId(d.getId());

                                    if(habarObject.getId().equals("0")){
                                        news_number = habarObject.getNews_number();
//                                    Toast.makeText(activity, ""+news_number, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        habarList.add(habarObject);
                                    }
                                }

                                habarAdapter.notifyDataSetChanged();

                                view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);
                }
            });
        }
    }

    public static void getHabarRange() {

//        habarList.clear();

        db.collection("News").orderBy("news_id", Query.Direction.DESCENDING).startAfter(habarList.get(habarList.size()-1)).limit(10).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                HabarObject habarObject = d.toObject(HabarObject.class);
                                habarObject.setId(d.getId());

                                if(habarObject.getId().equals("0")){
                                    news_number = habarObject.getNews_number();
//                                    Toast.makeText(activity, ""+news_number, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    habarList.add(habarObject);
                                }
                            }

//                            Collections.reverse(habarList);

                            habarList.sort((schedule1, schedule2)->{
                                int returnValue = 0;
                                if(schedule1.getNews_id() > schedule2.getNews_id())	return -1;
                                else if(schedule1.getNews_id() < schedule2.getNews_id())	return 1;
                                return returnValue;
                            });

                            habarAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);
            }});
    }

    @Override
    public void onItemClick(View view, int position) {

        SharedPreferences.Editor editor = preferences.edit();
        String s = preferences.getString("read_news","");
        s += habarList.get(position).getId() + " ";
        editor.putString("read_news", s);
        editor.commit();

        habarList.get(position).setNews_view_number(habarList.get(position).getNews_view_number()+1);

        Intent intent = new Intent(activity.getApplicationContext(), ActivityMakala.class);
        intent.putExtra("type", "News");
        intent.putExtra("position", position);
        intent.putExtra("id", habarList.get(position).getId());
        intent.putExtra("category", habarList.get(position).getNews_category());
        intent.putExtra("date", habarList.get(position).getNews_date());
        intent.putExtra("title", habarList.get(position).getNews_title());
        intent.putExtra("author", habarList.get(position).getNews_author());
        intent.putExtra("body", habarList.get(position).getNews_body());
        intent.putExtra("img_url", habarList.get(position).getNews_img_url());

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

//        isWifiEnabled();
//
//        habarAdapter.notifyDataSetChanged();
    }

    public boolean isWifiEnabled(){
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("wifi_enabled", "1");
            editor.commit();
            return true;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("wifi_enabled", "0");
        editor.commit();
        return false;
    }
}