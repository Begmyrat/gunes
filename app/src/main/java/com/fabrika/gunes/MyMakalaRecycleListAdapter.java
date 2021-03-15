package com.fabrika.gunes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;

public class MyMakalaRecycleListAdapter extends RecyclerView.Adapter<MyMakalaRecycleListAdapter.ViewHolder> implements MySubMakalaRecListAdapter.ItemClickListenerSub{

    private Activity context;
    private ArrayList<MakalaObject> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyMakalaRecycleListAdapter(Activity context, ArrayList<MakalaObject> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_makala, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MakalaObject makala = list.get(position);
        ArrayList<MakalaObject> subMakala = list.get(position).getSubMakala();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(subMakala.size());
        MySubMakalaRecListAdapter subRecycleListAdapter = new MySubMakalaRecListAdapter(context, subMakala);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(holder.recyclerView);

        if(subMakala.size()>0) {
            holder.recyclerView.setLayoutManager(linearLayoutManager);
            holder.recyclerView.setAdapter(subRecycleListAdapter);
        }
        else{
            holder.recyclerView.setVisibility(View.GONE);
        }
        subRecycleListAdapter.setClickListenerSub(this);

        if(makala.getTitle()==null){
            holder.linear_box.setVisibility(View.GONE);
        }
        else{
            holder.linear_box.setVisibility(View.VISIBLE);
        }

        if(makala.getRowTitle()==null || makala.getRowTitle().length()==0){
            holder.t_titleRow.setVisibility(View.GONE);
        }

        if(makala.getSubMakala().size()>0){
            holder.v_1.setVisibility(View.GONE);
        }

        if(position%4==0){
            holder.i_image.setImageResource(R.drawable.adam);
        }
        else if(position%4==1){
            holder.i_image.setImageResource(R.drawable.habarsurat2);
        }
        else if(position%4==2){
            holder.i_image.setImageResource(R.drawable.habarsurat3);
        }
        else if(position%4==3){
            holder.i_image.setImageResource(R.drawable.habarsurat4);
        }

        holder.t_titleRow.setText(makala.getRowTitle());
        holder.t_title.setText(makala.getTitle());
        holder.t_date.setText(makala.getDate());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemClick(View view, int position, ArrayList<MakalaObject> list) {
        Toast.makeText(context, ""+list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_title, t_date, t_titleRow;
        ImageView i_image;
        RecyclerView recyclerView;
        LinearLayout linear_box;
        View v_1;

        ViewHolder(View itemView) {
            super(itemView);
//            textview_title = itemView.findViewById(R.id.t_title);
//            i_exchange = itemView.findViewById(R.id.i_exchange);
            t_title = itemView.findViewById(R.id.t_title);
            i_image = itemView.findViewById(R.id.i_image);
            t_titleRow = itemView.findViewById(R.id.t_titleRow);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            t_date = itemView.findViewById(R.id.t_date);
            linear_box = itemView.findViewById(R.id.linear_box);
            v_1 = itemView.findViewById(R.id.v_1);

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
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, ArrayList<MakalaObject> list);
    }
}

