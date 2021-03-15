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
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentMakala extends Fragment implements MyMakalaRecycleListAdapter.ItemClickListener{

    View view;
    Toolbar toolbar;
    ArrayList<MakalaObject> makalaObjects;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MyMakalaRecycleListAdapter adapter;

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

        recyclerView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        makalaObjects = new ArrayList<>();

        getMakala();

        adapter = new MyMakalaRecycleListAdapter(getActivity(), makalaObjects);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

    }

    private void getMakala() {
        ArrayList<MakalaObject> subMakala = new ArrayList<>();

        MakalaObject m3 = new MakalaObject();
        m3.setTitle("Adamzat taryhynda ilkinji bolan waka");
        m3.setDate("#muzik #ceremony");

        subMakala.add(m3);
        subMakala.add(m3);
        subMakala.add(m3);
        subMakala.add(m3);
        subMakala.add(m3);
        subMakala.add(m3);

        MakalaObject m1 = new MakalaObject();
        m1.setSubMakala(subMakala);
        makalaObjects.add(m1);

        MakalaObject m2 = new MakalaObject();
        m2.setRowTitle("Hemmesi");
        makalaObjects.add(m2);

        for(int i=0;i<10;i++){
            MakalaObject m = new MakalaObject();
            m.setTitle("Hello: " + i);
            m.setDate("Date: " + (i+2020));
            makalaObjects.add(m);
        }


    }

    @Override
    public void onItemClick(View view, int position, ArrayList<MakalaObject> list) {
        Toast.makeText(view.getContext(), ""+list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}