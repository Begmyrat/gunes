package com.fabrika.gunes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyBlogListDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public static ArrayList<BlogModel> list;
    static ItemClickListener mClickListener = null;
    int[] avatars;
    boolean isDetail = false;
    FirebaseFirestore db;
    int adapter_pos = 0;
    SharedPreferences preferences;
    HashSet<String> mapBlog, mapComment;
    long comment_number;

    public MyBlogListDetailAdapter(Context context, ArrayList<BlogModel> list, boolean isDetail, long comment_number) {
        this.context = context;
        this.list = list;
        this.isDetail = isDetail;
        db = FirebaseFirestore.getInstance();
        mapBlog= new HashSet<>();
        mapComment = new HashSet<>();
        this.comment_number = comment_number;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String s = preferences.getString("blog_id","");
        String[] sp = s.split(" ");
        for(int i=0;i<sp.length;i++){
            mapBlog.add(sp[i]);
        }
        s = preferences.getString("comment_id","");
        sp = s.split(" ");
        for(int i=0;i<sp.length;i++){
            mapComment.add(sp[i]);
        }

        avatars = new int[]{R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9, R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12, R.drawable.avatar13, R.drawable.avatar14, R.drawable.avatar15, R.drawable.avatar16, R.drawable.avatar17, R.drawable.avatar18, R.drawable.avatar19, R.drawable.avatar20, R.drawable.avatar21, R.drawable.avatar22, R.drawable.avatar23, R.drawable.avatar24, R.drawable.avatar25, R.drawable.avatar26, R.drawable.avatar27, R.drawable.avatar28, R.drawable.avatar29, R.drawable.avatar30, R.drawable.avatar31, R.drawable.avatar32, R.drawable.avatar33};
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
            blogHolder.t_comment_number.setText(""+comment_number);
            blogHolder.t_view_number.setText(""+blogObject.getBlog_view_number());
            blogHolder.t_body.setMaxLines(100);
        }
        else{
            CommentHolder commentHolder = (CommentHolder) holder;
            CommentObject commentObject = list.get(position).getCommentObject();
            commentHolder.i_avatar.setImageResource(avatars[((int) commentObject.getComment_author_avatar())%33]);
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
//                    Toast.makeText(itemView.getContext(), "Clicked button at position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                    increaseValueBlog("blog_like_number");

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

                        FragmentBlog.isLiked = true;

                        t_like_number.setText(""+(list.get(adapter_pos).getBlogObject().getBlog_like_number()+1));

                        mapBlog.add(""+list.get(adapter_pos).getBlogObject().getBlog_id());
                        increaseValueBlog("blog_like_number");
                    }

                }
            });

//            itemView.setOnClickListener(this);
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
                    adapter_pos = getAdapterPosition();
                    if(adapter_pos==0){
//                        likeBlog();
                    }
                    else{
                        if(mapComment.contains(""+list.get(0).getBlogObject().getBlog_id()+"-"+list.get(adapter_pos).getCommentObject().getComment_id())){
                            Toast.makeText(context, "Bu teswiri ozal hem halapsyňyz.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mapComment.add(""+list.get(0).getBlogObject().getBlog_id()+"-"+list.get(adapter_pos).getCommentObject().getComment_id());

                            String s = preferences.getString("comment_id", "");
                            s += ""+list.get(0).getBlogObject().getBlog_id()+"-"+list.get(adapter_pos).getCommentObject().getComment_id() + " ";
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("comment_id", s);
                            editor.commit();
                            t_like_number.setText(""+(list.get(adapter_pos).getCommentObject().getComment_like_number()+1));
                            Toast.makeText(context, "Berekella! Şeýdip halap dur", Toast.LENGTH_SHORT).show();
                            increaseValue("comment_like_number");
                        }

                    }
                }
            });
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
        if(!isDetail)
            this.mClickListener = itemClickListener;
    }

    public void increaseValue(String operation) {

        final DocumentReference sfDocRef = db.collection("Blog")
                .document(list.get(0).getBlogObject().getBlog_id())
                .collection("comments")
                .document(list.get(adapter_pos).getCommentObject().getComment_id());

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

    public void increaseValueBlog(String operation) {

        final DocumentReference sfDocRef = db.collection("Blog")
                .document(list.get(0).getBlogObject().getBlog_id());

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
}

