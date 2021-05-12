package com.fabrika.gunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyCustomGridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Integer> avatars;
    LayoutInflater inflter;
    public static long secilenPos = -1;

    public MyCustomGridViewAdapter(Context applicationContext, ArrayList<Integer> avatars, long secilenPos) {
        this.context = applicationContext;
        this.avatars = avatars;
        inflter = (LayoutInflater.from(applicationContext));
        this.secilenPos = secilenPos;
    }
    @Override
    public int getCount() {
        return avatars.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_list_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageResource(avatars.get(i)); // set logo images
        ImageView i_clicked = (ImageView) view.findViewById(R.id.i_clicked);
        if(i==secilenPos){
            i_clicked.setVisibility(View.VISIBLE);
        }
        else{
            i_clicked.setVisibility(View.GONE);
        }

        return view;
    }
}
