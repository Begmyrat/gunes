package com.fabrika.gunes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FragmentMakala extends Fragment implements MyRecMultiAdapter.ItemClickListener{

    View view;
    Toolbar toolbar;
    ArrayList<MakalaModel> makalaObjects;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MainActivity activity;
    MyRecMultiAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_makala, container, false);

        prepareMe();

        return view;
    }

    private void prepareMe() {
        setHasOptionsMenu(true);//Add this sentence to the menu
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        toolbar.setTitle("Iň täze makalalar");

        activity = (MainActivity) getActivity();
        recyclerView = view.findViewById(R.id.recyclerView);
        makalaObjects = new ArrayList<>();

        //int viewType, String makala_id, String makala_title, String makala_body, String makala_img_url, String makala_date, String makala_author

        makalaObjects.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        makalaObjects.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        ArrayList<MakalaModel> subMakala = new ArrayList<>();
        subMakala.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        subMakala.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        subMakala.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        subMakala.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        subMakala.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        MakalaModel m = new MakalaModel();
        m.setViewType(1);
        m.setListMakalaModel(subMakala);
        makalaObjects.add(m);
        makalaObjects.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        makalaObjects.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        makalaObjects.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));
        makalaObjects.add(new MakalaModel(0, "1","Adamlar bilen baglanshykly gyzykly maglumatlar","Adamlar bilen baglanshykly gyzykly maglumatlar","4","22.03.2021","Andrey Ivan"));

        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new MyRecMultiAdapter(view.getContext(), makalaObjects);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onItemClick(View view, int position, ArrayList<MakalaModel> list) {
        Toast.makeText(view.getContext(), "vertical: " + list.get(position).getMakala_title() + "\npos: " + position, Toast.LENGTH_SHORT).show();
    }
}