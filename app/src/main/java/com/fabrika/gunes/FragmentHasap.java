package com.fabrika.gunes;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import link.fls.swipestack.SwipeStack;


public class FragmentHasap extends Fragment {

    View view;
    SwipeStack swipeStack;
    MainActivity activity;
    ArrayList<InformationObject> informationList;
    FloatingActionButton floatingActionButton;
    RelativeLayout r_main;
    FirebaseFirestore db;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    SwipeStackAdapter adapter;
    long infoNumber=0;
    TextView t_title;
    // progressDialog // progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_hasap, container, false);

        prepareMe();

        // progressDialog = new // progressDialog(activity.getApplicationContext());
        // progressDialog.setMessage("Az wagtdan...");
        // progressDialog.setTitle("Gazet");

        getInfo();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                swipeStack.resetStack();

                insertNewInfo();
            }
        });

        t_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void insertNewInfo() {

        Map<String, Object> info = new HashMap<>();
        info.put("information_author", "Merjen YAZLYÝEWA");
        info.put("information_body", "");

        db.collection("Information")
                .document(""+infoNumber)
                .set(info);

        infoNumber++;

        Map<String, Object> article_num = new HashMap<>();
        article_num.put("information_number", infoNumber);


        db.collection("Information")
                .document("0")
                .set(article_num);

    }

    private void prepareMe() {

        db = FirebaseFirestore.getInstance();

        activity = (MainActivity) getActivity();
        informationList = new ArrayList<>();
        swipeStack = (SwipeStack) view.findViewById(R.id.swipeStack);
        adapter = new SwipeStackAdapter(activity.getApplicationContext(), informationList);
        swipeStack.setAdapter(adapter);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        t_title = view.findViewById(R.id.t_title);
    }

    public void getInfo(){

        view.findViewById(R.id.llProgressBar).setVisibility(View.VISIBLE);

        // progressDialog.show();

        informationList.clear();

        db.collection("Information").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                InformationObject informationObject = d.toObject(InformationObject.class);
                                informationObject.setInformation_id(d.getId());

                                if(!informationObject.getInformation_id().equals("0"))
                                    informationList.add(informationObject);
                                else{
                                    infoNumber = informationObject.getInformation_number();
                                }
                            }

//                            Collections.reverse(informationList);

                            informationList.sort((schedule1, schedule2)->{
                                int returnValue = 0;
                                if(Integer.parseInt(schedule1.getInformation_id()) > Integer.parseInt(schedule2.getInformation_id()))	return -1;
                                else if(Integer.parseInt(schedule1.getInformation_id()) < Integer.parseInt(schedule2.getInformation_id()))	return 1;
                                return returnValue;
                            });

                            swipeStack.resetStack();

                            adapter.notifyDataSetChanged();
                            view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);

                            // progressDialog.hide();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.llProgressBar).setVisibility(View.GONE);
            }});
    }
}