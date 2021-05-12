package com.fabrika.gunes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityBlogDetail extends AppCompatActivity {

    FirebaseFirestore db, dbc;
    SharedPreferences preferences;
    ArrayList<BlogModel> blogModels;
    Bundle extras;
    String blog_id="", blog_author="", blog_body="", blog_date="", comment_author="", comment_date="", comment_body="";
    long blog_author_avatar=0, blog_comment_number=0, blog_view_number=0, blog_like_number=0, comment_author_avatar=0;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyBlogListDetailAdapter adapter;
    EditText e_comment;
    private AdView mAdView;
    public static long comment_number = 0;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

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
        increaseValue("blog_view_number");
        getComments();
    }

    private void prepareMe() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBackground));// set status background white

//        setHasOptionsMenu(true);//Add this sentence to the menu
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setElevation(0);
        this.setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        db = FirebaseFirestore.getInstance();
        blogModels = new ArrayList<>();

        extras = getIntent().getExtras();
        if(extras!=null){
            blog_id = extras.getString("blog_id");
            blog_author = extras.getString("blog_author");
            blog_author_avatar = extras.getLong("blog_author_avatar");
            blog_body = extras.getString("blog_body");
            blog_date = extras.getString("blog_date");
            blog_comment_number = extras.getLong("blog_comment_number");
            blog_like_number = extras.getLong("blog_like_number");
            blog_view_number = extras.getLong("blog_view_number");
            comment_number = blog_comment_number;
        }

        e_comment = findViewById(R.id.e_comment);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new MyBlogListDetailAdapter(getApplicationContext(), blogModels, true, comment_number);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        comment_author = preferences.getString("username", "");
        comment_author_avatar = preferences.getLong("avatar", 0);
    }

    public void clickBack(View view) {
        finish();
    }

    public void increaseValue(String operation) {

        final DocumentReference sfDocRef = db.collection("Blog").document(blog_id);

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
    }

    private void getComments() {

        blogModels.clear();

        BlogObject blogObject = new BlogObject();
        blogObject.setBlog_id(blog_id);
        blogObject.setBlog_author(blog_author);
        blogObject.setBlog_author_avatar(blog_author_avatar);
        blogObject.setBlog_date(blog_date);
        blogObject.setBlog_body(blog_body);
        blogObject.setBlog_like_number(blog_like_number);
        blogObject.setBlog_view_number(blog_view_number);
        blogObject.setBlog_comment_number(blog_comment_number);
        BlogModel blogModel = new BlogModel();
        blogModel.setBlogObject(blogObject);
        blogModel.setDetail(true);
        blogModel.setView_type(0);
        blogModels.add(blogModel);

        db.collection("Blog").document(blog_id).collection("comments").orderBy("comment_date", Query.Direction.DESCENDING).limit(1000).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                CommentObject commentObject = d.toObject(CommentObject.class);
                                commentObject.setComment_id(d.getId());
                                BlogModel blogModel = new BlogModel();
                                blogModel.setView_type(1);
                                blogModel.setCommentObject(commentObject);
                                blogModel.setDetail(true);
                                if(commentObject.getComment_body()!=null){
                                    blogModels.add(blogModel);
                                }

                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }});
    }

    public void clickSendComment(View view) {
        String comment = e_comment.getText().toString();
        insertComment();
    }

    private void insertComment() {

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

        comment_date = year + "." + month + "." + day + "-" + hour+minute+second+millisecond;

        comment_body = e_comment.getText().toString();

        // Create a new user with a first and last name
        Map<String, Object> comment = new HashMap<>();
        comment.put("comment_author", comment_author);
        comment.put("comment_author_avatar", comment_author_avatar);
        comment.put("comment_body", comment_body);
        comment.put("comment_date", comment_date);
        comment.put("comment_like_number", 0);

        db.collection("Blog")
                .document(blog_id)
                .collection("comments")
                .add(comment);

        increaseValue("blog_comment_number");
        comment_number++;
        getComments();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        e_comment.setText("");
        e_comment.clearFocus();
        adapter.notifyDataSetChanged();
    }
}