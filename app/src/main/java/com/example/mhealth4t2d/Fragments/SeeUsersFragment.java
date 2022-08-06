package com.example.mhealth4t2d.Fragments;

import android.icu.lang.UCharacter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.example.mhealth4t2d.Models.User;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.RecyclerViewAdapters.UserRecyclerViewAdpater;
import com.example.mhealth4t2d.Responses.GetExerciseCategoriesResponse;
import com.example.mhealth4t2d.Responses.GetUsersResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeeUsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserRecyclerViewAdpater adapter;

    public SeeUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_see_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_users);

        Utilities.displayProgressDialog(view.getContext(), "A carregar conteúdo", "Por favor espere...", R.drawable.ic_warning, false);

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody.put("email", Preferences.getStringPref(this.getContext(), "email"));
            jsonBody.put("password",  Preferences.getStringPref(this.getContext(), "password"));

        } catch (JSONException e) {

            e.printStackTrace();

        }

        Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getUsers/", Request.Method.POST, jsonBody, response ->{

            Utilities.removeProgressDialog();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            GetUsersResponse res = gson.fromJson(String.valueOf(response), GetUsersResponse.class);

            if(res.getCode() == 200){

                ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                adapter = new UserRecyclerViewAdpater(recyclerView, res.getUsers());
                recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapter);

            }else{

                Utilities.displaySnackBar(view.findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }



        });



        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburguer);

        return view;

    }
}