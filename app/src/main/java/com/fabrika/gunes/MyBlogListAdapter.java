package com.fabrika.gunes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyBlogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public static ArrayList<BlogModel> list;
    static ItemClickListener mClickListener = null;
    int[] avatars;
    boolean isDetail = false;
    FirebaseFirestore db;
    int adapter_pos;
    SharedPreferences preferences;
    HashSet<String> mapBlog;

    public MyBlogListAdapter(Context context, ArrayList<BlogModel> list, boolean isDetail) {
        this.context = context;
        this.list = list;
        this.isDetail = isDetail;
        db = FirebaseFirestore.getInstance();
        avatars = new int[]{R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9, R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12, R.drawable.avatar13, R.drawable.avatar14, R.drawable.avatar15, R.drawable.avatar16, R.drawable.avatar17, R.drawable.avatar18, R.drawable.avatar19, R.drawable.avatar20, R.drawable.avatar21, R.drawable.avatar22, R.drawable.avatar23, R.drawable.avatar24, R.drawable.avatar25, R.drawable.avatar26, R.drawable.avatar27, R.drawable.avatar28, R.drawable.avatar29, R.drawable.avatar30, R.drawable.avatar31, R.drawable.avatar32, R.drawable.avatar33};
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        String s = preferences.getString("blog_id","");
//        String[] sp = s.split(" ");
//        mapBlog = new HashSet<>();
//        for(int i=0;i<sp.length;i++){
//            mapBlog.add(sp[i]);
//        }
    }

    public List<BlogModel> getItems(){
        return list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_blog, parent, false);
            BlogHolder postHolder = new BlogHolder(view);
            return postHolder;
        }
        else if(viewType==1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_comment, parent, false);
            CommentHolder authorHolder = new CommentHolder(view);
            return authorHolder;
        }
        return null;

    }

    public static ArrayList<BlogModel> getList(){
        return list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==0){
            BlogHolder blogHolder = (BlogHolder) holder;
            BlogObject blogObject = list.get(position).getBlogObject();
            blogHolder.i_avatar.setImageResource(avatars[((int) blogObject.getBlog_author_avatar())%33]);
            blogHolder.t_author.setText(blogObject.getBlog_author());
            blogHolder.t_date.setText(blogObject.getBlog_date().substring(0,10));
            blogHolder.t_body.setText(blogObject.getBlog_body());
            blogHolder.t_like_number.setText(""+blogObject.getBlog_like_number());
            blogHolder.t_comment_number.setText(""+blogObject.getBlog_comment_number());
            blogHolder.t_view_number.setText(""+blogObject.getBlog_view_number());
        }
        else{
            CommentHolder commentHolder = (CommentHolder) holder;
            CommentObject commentObject = list.get(position).getCommentObject();
            commentHolder.i_avatar.setImageResource(avatars[(int) commentObject.getComment_author_avatar()]);
            commentHolder.t_author.setText(commentObject.getComment_author());
            commentHolder.t_date.setText(commentObject.getComment_date().substring(0,10));
            commentHolder.t_body.setText(commentObject.getComment_body());
            commentHolder.t_like_number.setText(""+commentObject.getComment_like_number());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getView_type();
    }

    public class BlogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView i_avatar;
        TextView t_author, t_date, t_view_number, t_comment_number, t_like_number, t_body;
        LinearLayout l_like;

        public BlogHolder(@NonNull View itemView) {
            super(itemView);

            i_avatar = itemView.findViewById(R.id.i_avatar);
            t_author = itemView.findViewById(R.id.t_author);
            t_date = itemView.findViewById(R.id.t_date);
            t_view_number = itemView.findViewById(R.id.t_view);
            t_comment_number = itemView.findViewById(R.id.t_comment_number);
            t_like_number = itemView.findViewById(R.id.t_like_number);
            t_body = itemView.findViewById(R.id.t_body);
            l_like = itemView.findViewById(R.id.l_like);

            l_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter_pos = getAdapterPosition();
//                    likeBlog();

                    String ss = preferences.getString("blog_id","");
                    String[] sp = ss.split(" ");
                    mapBlog = new HashSet<>();
                    for(int i=0;i<sp.length;i++){
                        mapBlog.add(sp[i]);
                    }

                    if(mapBlog.contains(""+list.get(adapter_pos).getBlogObject().getBlog_id())){
                        Toast.makeText(context, "Bu blogu ozal hem halapsyňyz.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Berekella! Şeýdip halap dur", Toast.LENGTH_SHORT).show();
                        String s = preferences.getString("blog_id", "");
                        s += ""+list.get(adapter_pos).getBlogObject().getBlog_id()+" ";
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("blog_id", s);
                        editor.commit();

                        FragmentBlog.blog_list.get(adapter_pos).getBlogObject().setBlog_like_number(FragmentBlog.blog_list.get(adapter_pos).getBlogObject().getBlog_like_number()+1);

                        t_like_number.setText(""+(list.get(adapter_pos).getBlogObject().getBlog_like_number()));

                        mapBlog.add(""+list.get(adapter_pos).getBlogObject().getBlog_id());
                        increaseValueBlog("blog_like_number");
                    }

                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition(), getList());
        }
    }

    public class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView i_avatar;
        TextView t_author, t_date, t_like_number, t_body;
        LinearLayout l_like;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);

            i_avatar = itemView.findViewById(R.id.i_avatar);
            t_author = itemView.findViewById(R.id.t_author);
            t_date = itemView.findViewById(R.id.t_date);
            t_like_number = itemView.findViewById(R.id.t_like_number);
            t_body = itemView.findViewById(R.id.t_body);
            l_like = itemView.findViewById(R.id.l_like);

            l_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // like the comment
                    Toast.makeText(context, "comment is liked", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition(), getList());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, ArrayList<BlogModel> list);
    }

    // allows clicks events to be caught
    public void setClickListenerSub(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void increaseValueBlog(String operation) {

        final DocumentReference sfDocRef = db.collection("Blog")
                .document(list.get(adapter_pos).getBlogObject().getBlog_id());

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

//    @Override
//    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
//        super.onViewRecycled(holder);
//
//        if(FragmentBlog.isLiked){
//            list.get(FragmentBlog.lastPosition).getBlogObject().setBlog_like_number(list.get(FragmentBlog.lastPosition).getBlogObject().getBlog_like_number()+1);
//        }
//        list.get(FragmentBlog.lastPosition).getBlogObject().setBlog_like_number(list.get(FragmentBlog.lastPosition).getBlogObject().getBlog_like_number()+1);
//
//        notifyDataSetChanged();
//    }
}

