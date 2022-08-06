package com.example.mhealth4t2d.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.example.mhealth4t2d.Models.ExerciseCategory;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.RecyclerViewAdapters.ExerciseRecyclerViewAdapter;
import com.example.mhealth4t2d.Responses.GetExerciseCategoriesResponse;
import com.example.mhealth4t2d.Responses.GetExercisesResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ExercisesFragment extends Fragment {

    private RecyclerView recyclerView;

    private SectionedRecyclerViewAdapter adapter;

    private GetExerciseCategoriesResponse exercise_categories_res;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exercises, container, false);

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){

            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburguer);

        }

        recyclerView = view.findViewById(R.id.recycler_view_exercises);

        adapter = new SectionedRecyclerViewAdapter();

        Utilities.displayProgressDialog(view.getContext(), "A carregar os exercícios", "Por favor espere...",  R.drawable.ic_warning,false);

        Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getExerciseCategories/", Request.Method.GET, null, response -> {

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            exercise_categories_res = gson.fromJson(String.valueOf(response), GetExerciseCategoriesResponse.class);

            if(exercise_categories_res.getCode() == 200){

                Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getExercises/", Request.Method.GET, null, response2 ->{

                    GsonBuilder builder2 = new GsonBuilder();
                    builder2.setPrettyPrinting();

                    Gson gson2 = builder2.create();
                    GetExercisesResponse res = gson2.fromJson(String.valueOf(response2), GetExercisesResponse.class);

                    Utilities.removeProgressDialog();

                    if(res.getCode() == 200){

                        adapter.removeAllSections();

                        for (ExerciseCategory category : exercise_categories_res.getExerciseCategories()) {

                            if(res.getExercisesByCategory(category.getCategory()).size() != 0){

                                adapter.addSection(new ExerciseRecyclerViewAdapter(res.getExercisesByCategory(category.getCategory()), category.getCategory(), R.layout.quiz_section, R.layout.exercise_item));

                            }

                        }

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

                    }else{

                        Utilities.displaySnackBar(view.findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                            Utilities.removeSnackBar();

                        });

                    }

                });

            }

        });

        return view;

    }
}