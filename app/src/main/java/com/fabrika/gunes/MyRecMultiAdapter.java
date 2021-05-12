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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyRecMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MySubMultiRecListAdapter.ItemClickListenerSub{

    private Context context;
    public static ArrayList<MakalaModel> list;
    static ItemClickListener mClickListener;
    SharedPreferences preferences;
    String isWifiEnabled = "0", isOnlyWifi = "0";

    public MyRecMultiAdapter(Context context, ArrayList<MakalaModel> list) {
        this.context = context;
        this.list = list;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        isWifiEnabled = preferences.getString("wifi_enabled", "0");
        isOnlyWifi = preferences.getString("wifi_only", "0");
    }

    public List<MakalaModel> getItems(){
        return list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_makala_vertical, parent, false);
            NormMakalaHolder postHolder = new NormMakalaHolder(view);
            return postHolder;
        }
        else if(viewType==1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.makala_rec_row, parent, false);
            RecMakalaHolder authorHolder = new RecMakalaHolder(view);
            return authorHolder;
        }
        return null;

    }

    public static ArrayList<MakalaModel> getList(){
        return list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==0){
            MakalaModel postModel = list.get(position);
            NormMakalaHolder postHolder = (NormMakalaHolder) holder;
            postHolder.t_category.setText(postModel.getArticle_author());
            String firstWord = postModel.getArticle_title();
            String secondWord = postModel.getArticle_body().substring(0,150)+"...";
            postHolder.t_view.setText(""+postModel.getArticle_view_number());

// Create a new spannable with the two strings
            Spannable spannable = new SpannableString(firstWord+ ". " +secondWord);
// Set the custom typeface to span over a section of the spannable object
            Typeface typeface1 = ResourcesCompat.getFont(context, R.font.nunitosans_extra_bold);
            Typeface typeface2 = ResourcesCompat.getFont(context, R.font.nunitosans_light);
            spannable.setSpan( new CustomTypefaceSpan("sans-serif", typeface1), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan( new CustomTypefaceSpan("sans-serif", typeface2), firstWord.length(), firstWord.length() + secondWord.length()+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set the text of a textView with the spannable object
            postHolder.t_title.setText( spannable );

            String url = list.get(position).getArticle_img_url();
            if(isOnlyWifi.equals("1") && isWifiEnabled.equals("0")){
                url = "sas";
            }

            if(position==0){
                postHolder.v.setVisibility(View.GONE);
            }
            else{
                postHolder.v.setVisibility(View.VISIBLE);
            }

            Picasso.get()
                    .load(url)
                    .error(R.drawable.mini_map)
                    .placeholder(R.drawable.mini_map)
                    .fit().centerCrop()
                    .into(postHolder.i_image);

        }
        else{
            ArrayList<MakalaModel> listMakala = list.get(position).getListMakalaModel();
            MySubMultiRecListAdapter adapter = new MySubMultiRecListAdapter(context, listMakala);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            ((RecMakalaHolder) holder).recyclerView.setAdapter(adapter);
            ((RecMakalaHolder) holder).recyclerView.setLayoutManager(layoutManager);
//            LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
//            linearSnapHelper.attachToRecyclerView(((RecMakalaHolder) holder).recyclerView);
            adapter.setClickListenerSub(this);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }

    @Override
    public void onItemClick(View view, int position, ArrayList<MakalaModel> list) {

        Intent intent = new Intent(context, ActivityMakala.class);
        intent.putExtra("id", list.get(position).getMakala_id());
        intent.putExtra("type", "Articles");
        intent.putExtra("category", list.get(position).getArticle_category());
        intent.putExtra("date", list.get(position).getArticle_date());
        intent.putExtra("title", list.get(position).getArticle_title());
        intent.putExtra("author", list.get(position).getArticle_author());
        intent.putExtra("body", list.get(position).getArticle_body());
        intent.putExtra("img_url", list.get(position).getArticle_img_url());
        context.startActivity(intent);
    }

    public class RecMakalaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RecyclerView recyclerView;

        public RecMakalaHolder(@NonNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recyclerView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (MyRecMultiAdapter.mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition(), MyRecMultiAdapter.getList());
        }
    }

    public class NormMakalaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView t_category, t_title, t_view;
        ImageView i_image;
        View v;

        public NormMakalaHolder(@NonNull View itemView) {
            super(itemView);

            t_category = itemView.findViewById(R.id.t_category);
            t_title = itemView.findViewById(R.id.t_title);
            i_image = itemView.findViewById(R.id.i_image);
            t_view = itemView.findViewById(R.id.t_view);
            v = itemView.findViewById(R.id.v_2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (MyRecMultiAdapter.mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition(), MyRecMultiAdapter.getList());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, ArrayList<MakalaModel> list);
    }

    // allows clicks events to be caught
    public void setClickListenerSub(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

}
