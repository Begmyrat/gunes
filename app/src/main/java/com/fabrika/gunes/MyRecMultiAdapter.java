package com.fabrika.gunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRecMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MySubMultiRecListAdapter.ItemClickListenerSub{

    private Context context;
    public static ArrayList<MakalaModel> list;
    static ItemClickListener mClickListener;

    public MyRecMultiAdapter(Context context, ArrayList<MakalaModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_makala, parent, false);
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
            postHolder.t_title.setText(postModel.getMakala_title());
            postHolder.t_author.setText(postModel.getMakala_author());
            postHolder.t_date.setText(postModel.getMakala_date());
            postHolder.t_body.setText(postModel.getMakala_body());
            postHolder.t_category.setText(postModel.getMakala_category());
            postHolder.t_category.setText("DURMUŞY KYSSALAR");

            if(position%4==0){
                postHolder.i_image.setImageResource(R.drawable.resim1);
            }
            else if(position%4==1){
                postHolder.i_image.setImageResource(R.drawable.habarsurat2);
            }
            else if(position%4==3){
                postHolder.i_image.setImageResource(R.drawable.resim2);
            }
            else if(position%4==2){
                postHolder.i_image.setImageResource(R.drawable.habarsurat3);
            }

        }
        else{
            ArrayList<MakalaModel> listMakala = list.get(position).getListMakalaModel();
            MySubMultiRecListAdapter adapter = new MySubMultiRecListAdapter(context, listMakala);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            ((RecMakalaHolder) holder).recyclerView.setAdapter(adapter);
            ((RecMakalaHolder) holder).recyclerView.setLayoutManager(layoutManager);
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
        Toast.makeText(context, "rec: " + list.get(position).getMakala_title(), Toast.LENGTH_SHORT).show();
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

    public class NormMakalaHolder extends RecyclerView.ViewHolder{

        TextView t_category, t_title, t_body, t_author, t_date;
        ImageView i_image;

        public NormMakalaHolder(@NonNull View itemView) {
            super(itemView);

            t_category = itemView.findViewById(R.id.t_category);
            t_title = itemView.findViewById(R.id.t_title);
            t_body = itemView.findViewById(R.id.t_body);
            t_author = itemView.findViewById(R.id.t_author);
            t_date = itemView.findViewById(R.id.t_date);
            i_image = itemView.findViewById(R.id.i_image);
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
