package com.fabrika.gunes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMakala extends Fragment implements MyRecMultiAdapter.ItemClickListener{

    View view;
    Toolbar toolbar;
    public static ArrayList<MakalaModel> makalaObjects;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MainActivity activity;
    MyRecMultiAdapter adapter;
    FloatingActionButton buttonInsertArticle;
    HashMap<String, Boolean> likedArticles;
    FirebaseFirestore db;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    SharedPreferences preferences;
    String[] likedArray;
    // progressDialog // progressDialog;
    long article_number = 0;
    public static int lastPosition = 0;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_makala, container, false);

        prepareMe();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMakala();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // progressDialog = new // progressDialog(activity.getApplicationContext());
        // progressDialog.setMessage("Az wagtdan...");
        // progressDialog.setTitle("Gazet");

        getMakala();

        buttonInsertArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for admin
//                insertEmptyArticle();
            }
        });



        return view;
    }

    private void insertEmptyArticle() {

        Calendar calendar = Calendar.getInstance();
        String day = ""+calendar.get(Calendar.DAY_OF_MONTH);
        if(day.length()==1)
            day = "0" + day;

        String month = "" + (calendar.get(Calendar.MONTH) + 1);
        if(month.length()==1)
            month = "0" + month;

        String year = "" + calendar.get(Calendar.YEAR);

        String hour = ""+calendar.get(Calendar.HOUR);
        String minute = ""+calendar.get(Calendar.MINUTE);
        String second = ""+calendar.get(Calendar.SECOND);
        String millisecond = ""+calendar.get(Calendar.MILLISECOND);

        if(month.length()==1)
            month = "0"+month;
        if(day.length()==1){
            day = "0"+day;
        }
        if(hour.length()==1)
            hour = "0"+hour;
        if(minute.length()==1)
            minute = "0"+minute;
        if(second.length()==1)
            second = "0"+second;
        if(millisecond.length()==1)
            millisecond = "0"+millisecond;

        String date = year + "." + month + "." + day + "-" + hour+minute+second+millisecond;

        // Create a new user with a first and last name
        Map<String, Object> article = new HashMap<>();
        article.put("article_author", "Serdaryň pikirleri");
        article.put("article_body", "");
        article.put("article_category", "durmuş");
        article.put("article_date", date);
        article.put("article_dislike_number", 0);
        article.put("article_img_url", "");
        article.put("article_like_number", 0);
        article.put("article_title", "");
        article.put("article_valid", true);
        article.put("article_view_number", 0);

// Add a new document with a generated ID
        db.collection("Articles")
                .document(""+article_number)
                .set(article);

        article_number++;

        Map<String, Object> article_num = new HashMap<>();
        article_num.put("article_number", article_number);


        db.collection("Articles")
                .document("0")
                .set(article_num);
    }

    private void prepareMe() {

//        ((MainActivity)getActivity()).updateStatusBarColor("#ffffff");

        setHasOptionsMenu(true);//Add this sentence to the menu
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        toolbar.setTitle("Iň täze makalalar");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show();
                getMakala();
            }
        });
        buttonInsertArticle = view.findViewById(R.id.buttonInsertNewArticle);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        activity = (MainActivity) getActivity();
        recyclerView = view.findViewById(R.id.recyclerView);
        makalaObjects = new ArrayList<>();
        isWifiEnabled();

        db = FirebaseFirestore.getInstance();

        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new MyRecMultiAdapter(view.getContext(), makalaObjects);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setClickListenerSub(this);

        likedArticles = new HashMap<>();
        String ids = preferences.getString("liked_articles","");
        likedArray = ids.split(" ");

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(2);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        adapter.setHasStableIds(true);

    }

    public void initScrollView(){
        if(makalaObjects.size()>3){

            ArrayList<MakalaModel> scrollList = new ArrayList<>();

            String ids = preferences.getString("liked_articles","");
            likedArray = ids.split(" ");
            for(int i=likedArray.length-1;i>=0;i--){
                for(int j=makalaObjects.size()-1;j>=0;j--){
                    if(likedArray[i].equals(makalaObjects.get(j).getMakala_id())){
                        scrollList.add(makalaObjects.get(j));
                    }
                }
            }
            MakalaModel m = new MakalaModel();
            m.setListMakalaModel(scrollList);
            m.setViewType(1);
            makalaObjects.set(3,m);
        }
    }

    @Override
    public void onItemClick(View view, int position, ArrayList<MakalaModel> list) {

        makalaObjects.get(position).setArticle_view_number(makalaObjects.get(position).getArticle_view_number()+1);

        Intent intent = new Intent(activity.getApplicationContext(), ActivityMakala.class);
        intent.putExtra("type", "Articles");
        intent.putExtra("position", position);
        intent.putExtra("id", makalaObjects.get(position).getMakala_id());
        intent.putExtra("category", makalaObjects.get(position).getArticle_category());
        intent.putExtra("date", makalaObjects.get(position).getArticle_date());
        intent.putExtra("title", makalaObjects.get(position).getArticle_title());
        intent.putExtra("author", makalaObjects.get(position).getArticle_author());
        intent.putExtra("body", makalaObjects.get(position).getArticle_body());
        intent.putExtra("img_url", makalaObjects.get(position).getArticle_img_url());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }

    private void getMakala() {

        makalaObjects.clear();

        view.findViewById(R.id.llProgressBar).setVisibility(View.VISIBLE);

        db.collection("Articles").orderBy("article_date", Query.Direction.ASCENDING).limit(1000).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                MakalaModel makalaModel = d.toObject(MakalaModel.class);
                                makalaModel.setMakala_id(d.getId());
                                makalaModel.setViewType(0);

                                if(!makalaModel.getMakala_id().equals("0"))
                                    makalaObjects.add(makalaModel);
                                else{
                                    article_number = makalaModel.getArticle_number();
                                }
                            }

                            ArrayList<MakalaModel> scrollList = new ArrayList<>();

                            for(int i=likedArray.length-1;i>=0;i--){
                                for(int j=makalaObjects.size()-1;j>=0;j--){
                                    if(likedArray[i].equals(makalaObjects.get(j).getMakala_id())){
                                        scrollList.add(makalaObjects.get(j));
                                    }
                                }
                            }

                            MakalaModel m = new MakalaModel();
                            m.setListMakalaModel(scrollList);
                            m.setViewType(1);
                            makalaObjects.add(3,m);

                            adapter.notifyDataSetChanged();

                            view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);
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
    public void onResume() {
        super.onResume();

        isWifiEnabled();
        initScrollView();


//
        if(activity!=null){
//            getMakala();
            adapter.notifyDataSetChanged();
        }
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