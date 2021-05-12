package com.fabrika.gunes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentBlog extends Fragment implements MyBlogListAdapter.ItemClickListener{

    View view;
    MainActivity activity;
    SharedPreferences preferences;
    FirebaseFirestore db, dbc;
    public static ArrayList<BlogModel> blog_list;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyBlogListAdapter adapter;
    Toolbar toolbar;
    FloatingActionButton buttonInsertBlog;
    public static int lastPosition=0;
    public static boolean isLiked = false;
    // progressDialog // progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_blog, container, false);;

        prepareMe();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBlogs();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("blog_id","");
//        editor.commit();

        // progressDialog = new // progressDialog(activity.getApplicationContext());
        // progressDialog.setMessage("Az wagtdan...");
        // progressDialog.setTitle("Gazet");

        getBlogs();

        buttonInsertBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert new Blog
//                insertNewBlog();
                String username = preferences.getString("username", "");
                if(username.length()==0){
                    startActivity(new Intent(activity.getApplicationContext(), ActivityLogin.class));
                }
                else{
                    startActivity(new Intent(activity.getApplicationContext(), ActivityInsertBlog.class));
                }
            }
        });

        return view;
    }

    private void prepareMe() {

        setHasOptionsMenu(true);//Add this sentence to the menu
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        toolbar.setTitle("Blog");
        activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("username", "");
//        editor.putLong("avatar", 0);
//        editor.commit();

        db = FirebaseFirestore.getInstance();
        blog_list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new MyBlogListAdapter(view.getContext(), blog_list, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setClickListenerSub(this);

        buttonInsertBlog = view.findViewById(R.id.buttonInsertBlog);

    }

    private void insertNewBlog() {

        Calendar calendar = Calendar.getInstance();
        String year = ""+calendar.get(Calendar.YEAR);
        String month = ""+(calendar.get(Calendar.MONTH)+1);
        String day = ""+calendar.get(Calendar.DAY_OF_MONTH);
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

        Map<String, Object> blogMap = new HashMap<>();
        blogMap.put("blog_author", "Admin");
        blogMap.put("blog_author_avatar", 2);
        blogMap.put("blog_body", "blog");
        blogMap.put("blog_comment_number", 0);
        blogMap.put("blog_date", date);
        blogMap.put("blog_like_number", 0);
        blogMap.put("blog_view_number", 0);

        Map<String, Object> m = new HashMap<>();
        m.put("comment_number", 0);

        db.collection("Blog").add(blogMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                db.collection("Blog").document(id).collection("comments").add(m);
            }
        });
        Toast.makeText(activity.getApplicationContext(), "Blog paýlaşyldy", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();

//        if(blog_list!=null && blog_list.size()>0)
//            blog_list.get(lastPosition).getBlogObject().setBlog_view_number(blog_list.get(lastPosition).getBlogObject().getBlog_view_number()+1);

        if(isLiked){
            blog_list.get(lastPosition).getBlogObject().setBlog_like_number(blog_list.get(lastPosition).getBlogObject().getBlog_like_number()+1);
        }

//        adapter.notifyDataSetChanged();
        getBlogs();

        isLiked = false;
    }

    private void getBlogs() {

        blog_list.clear();

        view.findViewById(R.id.llProgressBar).setVisibility(View.VISIBLE);

        db.collection("Blog").orderBy("blog_date", Query.Direction.ASCENDING).limit(1000).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                BlogObject blogObject = d.toObject(BlogObject.class);
                                blogObject.setBlog_id(d.getId());
                                BlogModel blogModel = new BlogModel();
                                blogModel.setBlogObject(blogObject);
                                if(blogObject.getBlog_body()!=null)
                                    blog_list.add(blogModel);
                            }

                            Collections.reverse(blog_list);

                            adapter.notifyDataSetChanged();

                            recyclerView.scrollToPosition(lastPosition);

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
    public void onItemClick(View view, int position, ArrayList<BlogModel> list) {

        MyBlogListAdapter.list.get(position).getBlogObject().setBlog_view_number(MyBlogListAdapter.list.get(position).getBlogObject().getBlog_view_number()+1);
        lastPosition = position;

        BlogObject blogObject = list.get(position).getBlogObject();
        Intent intent = new Intent(activity.getApplicationContext(), ActivityBlogDetail.class);
        intent.putExtra("blog_id", blogObject.getBlog_id());
        intent.putExtra("blog_author", blogObject.getBlog_author());
        intent.putExtra("blog_author_avatar", blogObject.getBlog_author_avatar());
        intent.putExtra("blog_comment_number", blogObject.getBlog_comment_number());
        intent.putExtra("blog_view_number", blogObject.getBlog_view_number());
        intent.putExtra("blog_like_number", blogObject.getBlog_like_number());
        intent.putExtra("blog_body", blogObject.getBlog_body());
        intent.putExtra("blog_date", blogObject.getBlog_date());
        startActivity(intent);
    }
}