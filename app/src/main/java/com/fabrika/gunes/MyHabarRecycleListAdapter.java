package com.fabrika.gunes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyHabarRecycleListAdapter extends RecyclerView.Adapter<MyHabarRecycleListAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<HabarObject> list;
    int selected_pos;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    SharedPreferences preferences;

    // data is passed into the constructor
    MyHabarRecycleListAdapter(Activity context, ArrayList<HabarObject> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_habar, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textview_body.setText(list.get(position).getBody());
        holder.t_bodyFull.setText(list.get(position).getBody() + list.get(position).getBody()+ list.get(position).getBody()+ list.get(position).getBody());
        holder.t_bodyTest.setText(list.get(position).getBody());
        holder.t_bodyFull2.setText(list.get(position).getBody() + list.get(position).getBody()+ list.get(position).getBody()+ list.get(position).getBody());

        if(position%5==0){
            holder.i_image.setImageResource(R.drawable.adam);
        }
        else if(position%5==1){
            holder.i_image.setImageResource(R.drawable.habarsurat);
        }
        else if(position%5==2){
            holder.i_image.setImageResource(R.drawable.habarsurat2);
        }
        else if(position%5==3){
            holder.i_image.setImageResource(R.drawable.habarsurat3);
        }
        else if(position%5==4){
            holder.i_image.setImageResource(R.drawable.habarsurat4);
        }

        Boolean isRead = preferences.getBoolean(""+list.get(position).getId(), false);

//        if(isRead){
//            ColorMatrix cm = new ColorMatrix();
//            cm.setSaturation(0);
//            Paint greyscalePaint = new Paint();
//            greyscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
//            // Create a hardware layer with the greyscale paint
//            holder.r_main.setLayerType(View.LAYER_TYPE_HARDWARE, greyscalePaint);
//        }
//        else{
//            ColorMatrix cm = new ColorMatrix();
//            cm.setSaturation(1000);
//            cm.setRGB2YUV();
//            Paint greyscalePaint = new Paint();
//            greyscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
//            // Create a hardware layer with the greyscale paint
//            holder.r_main.setLayerType(View.LAYER_TYPE_HARDWARE, greyscalePaint);
//        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textview_body, t_bodyFull, t_bodyTest, t_bodyFull2;
        ImageView i_image;
        RelativeLayout r_box, r_box2, r_main;

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

