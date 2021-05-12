package com.fabrika.gunes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MySubMultiRecListAdapter extends RecyclerView.Adapter<MySubMultiRecListAdapter.ViewHolder> {

    private ArrayList<MakalaModel> list;
    private LayoutInflater mInflater;
    private ItemClickListenerSub mClickListener;
    private Context context;
    SharedPreferences preferences;
    String isWifiEnabled = "0", isOnlyWifi = "0";

    // data is passed into the constructor
    MySubMultiRecListAdapter(Context context, ArrayList<MakalaModel> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        isWifiEnabled = preferences.getString("wifi_enabled", "0");
        isOnlyWifi = preferences.getString("wifi_only", "0");
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_makala_recyc, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        AuthorModel authorModel = (AuthorModel) list.get(position);
        MakalaModel m = list.get(position);
        holder.t_title.setText(m.getArticle_title());
        holder.t_author.setText(m.getArticle_author());
        holder.t_category.setText(m.getArticle_category());

        String url = list.get(position).getArticle_img_url();
        if(isOnlyWifi.equals("1") && isWifiEnabled.equals("0")){
            url = "sasa";
        }

        try {
            Glide
                    .with(context)
                    .load(new URL(url))
                    .centerCrop()
                    .placeholder(R.drawable.mini_map)
                    .into(holder.i_image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView t_title, t_author, t_category;
        ImageView i_image;

        ViewHolder(View itemView) {
            super(itemView);

            t_title = itemView.findViewById(R.id.t_title);
            t_author = itemView.findViewById(R.id.t_author);
            i_image = itemView.findViewById(R.id.i_image);
            t_category = itemView.findViewById(R.id.t_category);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), list);
        }
    }

    // convenience method for getting data at click position
    public MakalaModel getItem(int id) {
        return list.get(id);
    }

    // allows clicks events to be caught
    public void setClickListenerSub(ItemClickListenerSub itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListenerSub {
        void onItemClick(View view, int position, ArrayList<MakalaModel> list);
    }
}

