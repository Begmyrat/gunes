package com.fabrika.gunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class SwipeStackAdapter extends BaseAdapter {

    private List<InformationObject> mData;
    Context context;

    public SwipeStackAdapter(Context context, List<InformationObject> data) {
        this.mData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public InformationObject getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_card, parent, false);
        TextView textViewCard = (TextView) convertView.findViewById(R.id.t_title);
        RelativeLayout r_box = convertView.findViewById(R.id.r_box);
        TextView t_author = convertView.findViewById(R.id.t_author);

        textViewCard.setText(mData.get(position).getInformation_body());
        t_author.setText(mData.get(position).getInformation_author());

        if(position%5==0){
            r_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalette2));
        }
        else if(position%5==1){
            r_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalette3));
        }
        else if(position%5==2){
            r_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalette4));
        }
        else if(position%5==3){
            r_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalette5));
        }
        else{
            r_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalette6));
        }

        return convertView;
    }
}