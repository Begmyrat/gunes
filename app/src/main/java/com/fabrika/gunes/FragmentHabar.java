package com.fabrika.gunes;

import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.preference.PreferenceManager;
import android.transition.Fade;
import android.transition.PathMotion;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentHabar extends Fragment implements MyHabarRecycleListAdapter.ItemClickListener{

    View view;
    Toolbar toolbar;
    EditText e_search;
    Boolean isSearchVisible;
    RecyclerView recyclerView;
    ArrayList<HabarObject> habarList;
    MyHabarRecycleListAdapter habarAdapter;
    StaggeredGridLayoutManager layoutManager;
    MainActivity activity;
    HashMap<String, Boolean> clickMap;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_habar, container, false);

        prepareMe();

        // Inflate the layout for this fragment
        return view;
    }

    private void prepareMe() {
        setHasOptionsMenu(true);//Add this sentence to the menu
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        activity = (MainActivity) getActivity();

        e_search = view.findViewById(R.id.e_search);
        toolbar.setTitle("Habarlar");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
//        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        habarList = new ArrayList<>();
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getHabar();
        habarAdapter = new MyHabarRecycleListAdapter(activity, habarList);
        recyclerView.setAdapter(habarAdapter);
        habarAdapter.setClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        clickMap = new HashMap<>();
    }

    private void getHabar() {

        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah h ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Emel'inestolar Hizbullah ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Elar Hizbullah ve Emel'in Avn'a mesajı mı?Lübnan'daki so'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Himı?otestolar Hi otestolar Hi",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah h ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah vki so'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Himı?otestolar Hi otestolar Hi",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah h ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?Lübnan'daki son protestolar Hizbullah ve Emel'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Hizbullah ve Elar Hizbullah ve Emel'in Avn'a mesajı mı?Lübnan'daki so'in Avn'a mesajı mı?",""));
        habarList.add(new HabarObject("","","","","Lübnan'daki son protestolar Himı?otestolar Hi otestolar Hi",""));

    }

    @Override
    public void onItemClick(View view, int position) {

        if(clickMap.containsKey(habarList.get(position).getId())){
            // Create a paint object with 0 saturation (black and white)
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            Paint greyscalePaint = new Paint();
            greyscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            // Create a hardware layer with the greyscale paint
            view.setLayerType(View.LAYER_TYPE_HARDWARE, greyscalePaint);
        }

        if(clickMap.containsKey(habarList.get(position).getId()) && clickMap.get(habarList.get(position).getId())){
            Transition transition = new Slide();
            transition.setDuration(500);
            transition.addTarget(view.findViewById(R.id.r_box2));
            TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
            view.findViewById(R.id.r_box2).setVisibility(View.INVISIBLE);
            clickMap.put(habarList.get(position).getId(), false);
        }
        else if(clickMap.containsKey(habarList.get(position).getId()) && !clickMap.get(habarList.get(position).getId())){
            Transition transition = new Slide();
            transition.setDuration(500);
            transition.addTarget(view.findViewById(R.id.r_box2));
            TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
            view.findViewById(R.id.r_box2).setVisibility(View.VISIBLE);
            clickMap.put(habarList.get(position).getId(), true);
        }
        else{
            Transition transition = new Slide();
            transition.setDuration(500);
            transition.addTarget(view.findViewById(R.id.r_box2));
            TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
            view.findViewById(R.id.r_box2).setVisibility(View.VISIBLE);
            clickMap.put(habarList.get(position).getId(), true);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(""+habarList.get(position).getId(), true);
            editor.commit();
        }
    }
}