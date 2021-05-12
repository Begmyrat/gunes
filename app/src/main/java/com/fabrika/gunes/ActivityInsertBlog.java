package com.fabrika.gunes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityInsertBlog extends AppCompatActivity {

    EditText e_blog;
    String blog, username;
    Long avatar;
    SharedPreferences preferences;
    FirebaseFirestore db;
    Calendar calendar;
    ImageView i_avatar;
    TextView t_author, t_date;
    int[] avatars = new int[]{R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9, R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12, R.drawable.avatar13, R.drawable.avatar14, R.drawable.avatar15, R.drawable.avatar16, R.drawable.avatar17, R.drawable.avatar18, R.drawable.avatar19, R.drawable.avatar20, R.drawable.avatar21, R.drawable.avatar22, R.drawable.avatar23, R.drawable.avatar24, R.drawable.avatar25, R.drawable.avatar26, R.drawable.avatar27, R.drawable.avatar28, R.drawable.avatar29, R.drawable.avatar30, R.drawable.avatar31, R.drawable.avatar32, R.drawable.avatar33};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_blog);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBackground));// set status background white

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = preferences.getString("username", "");
        avatar = preferences.getLong("avatar", 0);
        e_blog = findViewById(R.id.e_blog);
        db = FirebaseFirestore.getInstance();
        i_avatar = findViewById(R.id.i_avatar);
        t_author = findViewById(R.id.t_author);
        t_date = findViewById(R.id.t_date);
        t_author.setText(username);
        i_avatar.setImageResource(avatars[Math.toIntExact(avatar)]);
        calendar = Calendar.getInstance();
        String day = ""+calendar.get(Calendar.DAY_OF_MONTH);
        if(day.length()==1){
            day = "0"+day;
        }
        String month = ""+(calendar.get(Calendar.MONTH)+1);
        if(month.length()==1){
            month = "0" + month;
        }
        String year = ""+calendar.get(Calendar.YEAR);
        t_date.setText(day+"."+month+"."+year);
    }

    public void clickBack(View view) {
        finish();
    }

    public void clickInsert(View view) {

        blog = e_blog.getText().toString();
        if(blog.length()>10){

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
            blogMap.put("blog_author", username);
            blogMap.put("blog_author_avatar", avatar);
            blogMap.put("blog_body", blog);
            blogMap.put("blog_comment_number", 0);
            blogMap.put("blog_date", date);
            blogMap.put("blog_like_number", 0);
            blogMap.put("blog_view_number", 0);

            Map<String, Object> m = new HashMap<>();
            m.put("comment_number", 0);

            final String[] id = {""};

            db.collection("Blog").add(blogMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    id[0] = documentReference.getId();
                    db.collection("Blog").document(id[0]).collection("comments").add(m);
                }
            });
            Toast.makeText(this, "Blog paýlaşyldy", Toast.LENGTH_SHORT).show();

//            BlogObject blogObject = d.toObject(BlogObject.class);
//            blogObject.setBlog_id(d.getId());
//            BlogModel blogModel = new BlogModel();
//            blogModel.setBlogObject(blogObject);

            BlogObject blogObject = new BlogObject();
            blogObject.setBlog_id(id[0]);
            blogObject.setBlog_like_number(0);
            blogObject.setBlog_view_number(0);
            blogObject.setBlog_comment_number(0);
            blogObject.setBlog_body(blog);
            blogObject.setBlog_date(date);
            blogObject.setBlog_author_avatar(avatar);
            BlogModel blogModel = new BlogModel();
            blogModel.setBlogObject(blogObject);
            FragmentBlog.blog_list.add(0, blogModel);

            finish();

        }
        else{
            Toast.makeText(this, "Blog gaty gysga boldy. Biraz uzynrak ýazsaň?", Toast.LENGTH_SHORT).show();
        }


    }
}