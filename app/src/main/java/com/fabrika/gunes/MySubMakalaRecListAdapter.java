package com.fabrika.gunes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MySubMakalaRecListAdapter extends RecyclerView.Adapter<MySubMakalaRecListAdapter.ViewHolder> {

    private ArrayList<MakalaObject> list;
    private LayoutInflater mInflater;
    private ItemClickListenerSub mClickListener;
    private Activity context;

    // data is passed into the constructor
    MySubMakalaRecListAdapter(Activity context, ArrayList<MakalaObject> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
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
        holder.t_title.setText(list.get(position).getTitle());
        holder.t_category.setText(list.get(position).getDate() + "  " + list.get(position).getCategory());

        if(position%3==0){
            holder.i_image.setImageResource(R.drawable.habarsurat);
        }
        else if(position%3==1){
            holder.i_image.setImageResource(R.drawable.habarsurat2);
        }
        else if(position%3==1){
            holder.i_image.setImageResource(R.drawable.habarsurat3);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_title, t_category;
        ImageView i_image;

        ViewHolder(View itemView) {
            super(itemView);
            t_title = itemView.findViewById(R.id.t_title);
            t_category = itemView.findViewById(R.id.t_category);
            i_image = itemView.findViewById(R.id.i_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), list);
        }
    }

    // convenience method for getting data at click position
    public MakalaObject getItem(int id) {
        return list.get(id);
    }

    // allows clicks events to be caught
    public void setClickListenerSub(ItemClickListenerSub itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListenerSub {
        void onItemClick(View view, int position, ArrayList<MakalaObject> list);
    }
}

