package com.example.mhealth4t2d.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.DefaultResponse;
import com.example.mhealth4t2d.Responses.GetExerciseCategoriesResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterExercisesFragment extends Fragment {

    private Spinner spinner_category;

    private Button btn_register;

    private EditText input_title, input_description, input_image_link, input_video_link;

    private GetExerciseCategoriesResponse exercise_categories_res;

    public RegisterExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_exercises, container, false);

        spinner_category = view.findViewById(R.id.spinner_category);

        btn_register = view.findViewById(R.id.btn_register);

        input_title = view.findViewById(R.id.input_title);

        input_description = view.findViewById(R.id.input_description);

        input_image_link = view.findViewById(R.id.input_image);

        input_video_link = view.findViewById(R.id.input_video);

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburguer);

        }
        
        Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getExerciseCategories/", Request.Method.GET, null, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            exercise_categories_res = gson.fromJson(String.valueOf(response), GetExerciseCategoriesResponse.class);

            if(exercise_categories_res.getCode() == 200){

                String[] categories = new String[exercise_categories_res.getExerciseCategories().size()];

                for(int i = 0; i < exercise_categories_res.getExerciseCategories().size(); i++){

                    categories[i] = exercise_categories_res.getExerciseCategories().get(i).getCategory();

                }

                ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, categories);

                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_category.setAdapter(spinner_adapter);

            }else{

                Utilities.displaySnackBar(view.findViewById(R.id.content), exercise_categories_res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }



        });

        btn_register.setOnClickListener(l ->{

            JSONObject jsonBody = new JSONObject();

            try {

                jsonBody.put("email", Preferences.getStringPref(this.getContext(), "email"));
                jsonBody.put("password",  Preferences.getStringPref(this.getContext(), "password"));
                jsonBody.put("title", input_title.getText());
                jsonBody.put("description", input_description.getText());
                jsonBody.put("category_id", exercise_categories_res.getExerciseCategories().get(spinner_category.getSelectedItemPosition()).getId());

                if(!input_image_link.getText().toString().isEmpty()){

                    jsonBody.put("image_link", input_image_link.getText());

                }

                if(!input_video_link.getText().toString().isEmpty()){

                    jsonBody.put("video_link", input_video_link.getText());

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/insertExercises/", Request.Method.POST, jsonBody, response ->{

                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();

                Gson gson = builder.create();
                DefaultResponse res = gson.fromJson(String.valueOf(response), DefaultResponse.class);

                Utilities.displaySnackBar(view.findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            });

        });

        return view;

    }
}