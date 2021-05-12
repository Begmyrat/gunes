package com.fabrika.gunes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MyHabarRecycleListAdapter extends RecyclerView.Adapter<MyHabarRecycleListAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<HabarObject> list;
    int selected_pos;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    SharedPreferences preferences;
    HashMap<String, Boolean> clickMap;
    String[] okunanlar;
    HashMap<String, Boolean> readMap;
    FirebaseFirestore db;
    String isWifiEnabled = "0", isOnlyWifi = "0";

    // data is passed into the constructor
    MyHabarRecycleListAdapter(Activity context, ArrayList<HabarObject> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        clickMap = new HashMap<>();
        db = FirebaseFirestore.getInstance();

        isWifiEnabled = preferences.getString("wifi_enabled", "0");
        isOnlyWifi = preferences.getString("wifi_only", "0");

//        String s = preferences.getString("read_news","");
//        okunanlar = s.split(" ");
        readMap = new HashMap<>();
//        for(int i=0;i<okunanlar.length;i++){
//            readMap.put(okunanlar[i], true);
//        }
    }

//    public void init(){
//        String s = preferences.getString("read_news","");
//        okunanlar = s.split(" ");
//
//        readMap = new HashMap<>();
//
//        for(int i=0;i<okunanlar.length;i++){
//            readMap.put(okunanlar[i], true);
//        }
//    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_habar, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.textview_body.setText(list.get(position).getNews_title());
        holder.t_viewed.setText(""+list.get(position).getNews_view_number());
        holder.t_category.setText("#"+list.get(position).getNews_category());
        holder.t_date.setText(list.get(position).getNews_date());

        String url = list.get(position).getNews_img_url();
        if(isOnlyWifi.equals("1") && isWifiEnabled.equals("0")){
            url = "asda";
        }

//        try {
//            Glide
//                    .with(context)
//                    .load(new URL(url))
//                    .centerCrop()
//                    .placeholder(R.drawable.mini_map)
//                    .into(holder.i_image);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        Picasso.get()
                .load(url)
                .error(R.drawable.mini_map)
                .placeholder(R.drawable.mini_map)
                .fit().centerCrop()
                .into(holder.i_image);



        if(readMap.containsKey(list.get(position).getId())){
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            Toast.makeText(context, "map: " + readMap.get(list.get(position).getId()) + " id: " + list.get(position).getId(), Toast.LENGTH_SHORT).show();
            Paint greyscalePaint = new Paint();
            greyscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            // Create a hardware layer with the greyscale paint
            holder.r_box.setLayerType(View.LAYER_TYPE_HARDWARE, greyscalePaint);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textview_body, t_bodyFull, t_bodyTest, t_bodyFull2, t_viewed, t_category, t_date;
        ImageView i_image, i_bookmark;
        RelativeLayout r_box, r_box2, r_main;
        LinearLayout l_bookmark;

        ViewHolder(View itemView) {
            super(itemView);
//            textview_title = itemView.findViewById(R.id.t_title);
//            i_exchange = itemView.findViewById(R.id.i_exchange);
            textview_body = itemView.findViewById(R.id.t_body);
            i_image = itemView.findViewById(R.id.i_image);
            t_bodyFull = itemView.findViewById(R.id.t_body_full);
            r_box = itemView.findViewById(R.id.r_box);
            r_box2 = itemView.findViewById(R.id.r_box2);
            t_bodyTest = itemView.findViewById(R.id.t_body2);
            t_bodyFull2 = itemView.findViewById(R.id.t_body_full2);
            r_main = itemView.findViewById(R.id.r_main);
            l_bookmark = itemView.findViewById(R.id.l_bookmark);
            i_bookmark = itemView.findViewById(R.id.i_bookmark);
            t_viewed = itemView.findViewById(R.id.t_viewed);
            t_category = itemView.findViewById(R.id.t_category);
            t_date = itemView.findViewById(R.id.t_date);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public HabarObject getItem(int id) {
        return list.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

