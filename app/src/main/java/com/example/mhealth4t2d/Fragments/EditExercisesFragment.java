package com.example.mhealth4t2d.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.DefaultResponse;
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

public class EditExercisesFragment extends Fragment {

    private Spinner spinner_category, spinner_exercises;

    private GetExerciseCategoriesResponse exercise_categories_res;

    private GetExercisesResponse exercises_res;

    private Button btn_edit, btn_delete;

    private EditText input_title, input_description, input_image_link, input_video_link;

    public EditExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_exercises, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburguer);

        spinner_exercises = view.findViewById(R.id.spinner_exercises);

        spinner_category = view.findViewById(R.id.spinner_category);

        btn_edit = view.findViewById(R.id.btn_edit);

        btn_delete = view.findViewById(R.id.btn_delete);

        input_title = view.findViewById(R.id.input_title);

        input_description = view.findViewById(R.id.input_description);

        input_image_link = view.findViewById(R.id.input_image);

        input_video_link = view.findViewById(R.id.input_video);

        Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getExerciseCategories/", Request.Method.GET, null, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            exercise_categories_res = gson.fromJson(String.valueOf(response), GetExerciseCategoriesResponse.class);

            if(exercise_categories_res.getCode() == 200){

                initSpinnerCategories(view.getContext());

            }else{

                Utilities.displaySnackBar(view.findViewById(R.id.content), exercise_categories_res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }

        });

        Utilities.displayProgressDialog(view.getContext(), "A carregar conteúdo", "Por favor espere...", R.drawable.ic_warning, false);

        Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getExercises/", Request.Method.GET, null, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            exercises_res = gson.fromJson(String.valueOf(response), GetExercisesResponse.class);

            Utilities.removeProgressDialog();

            if(exercises_res.getCode() == 200){

                initSpinnerExercises(view.getContext());

            }else{

                Utilities.displaySnackBar(view.findViewById(R.id.content), exercises_res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }

        });

        spinner_exercises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if(exercises_res != null && exercise_categories_res != null){

                    for(int i = 0; i < exercise_categories_res.getExerciseCategories().size(); i++){

                        if(exercise_categories_res.getExerciseCategories().get(i).getCategory().equals(exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getCategory())){

                            spinner_category.setSelection(i);

                        }

                    }

                    input_title.setText(exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getTitle());
                    input_description.setText(exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getDescription());
                    input_image_link.setText(exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getImage_link());
                    input_video_link.setText(exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getVideo_link());

                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        btn_edit.setOnClickListener(l ->{

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

            Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/updateExercise/" + exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getId(), Request.Method.POST, jsonBody, response ->{

                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();

                Gson gson = builder.create();
                DefaultResponse res = gson.fromJson(String.valueOf(response), DefaultResponse.class);

                if(res.getCode() == 200){

                    exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).setTitle(input_title.getText().toString());
                    exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).setDescription(input_description.getText().toString());
                    exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).setCategory(spinner_category.getSelectedItem().toString());
                    exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).setDescription(input_description.getText().toString());

                    initSpinnerExercises(view.getContext());

                }

                Utilities.displaySnackBar(view.findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            });

        });

        btn_delete.setOnClickListener(l ->{

            JSONObject jsonBody = new JSONObject();

            try {

                jsonBody.put("email", Preferences.getStringPref(this.getContext(), "email"));
                jsonBody.put("password",  Preferences.getStringPref(this.getContext(), "password"));

            } catch (JSONException e) {

                e.printStackTrace();

            }

            Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/deleteExercise/" + exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getId(), Request.Method.POST, jsonBody, response ->{

                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();

                Gson gson = builder.create();
                DefaultResponse res = gson.fromJson(String.valueOf(response), DefaultResponse.class);

                if(res.getCode() == 200){

                    initSpinnerExercises(view.getContext());

                }

                Utilities.displaySnackBar(view.findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            });

        });

        return view;

    }

    public void initSpinnerExercises(Context context){

        String[] exercises = new String[exercises_res.getExercises().size()];

        for(int i = 0; i < exercises_res.getExercises().size(); i++){

            exercises[i] = exercises_res.getExercises().get(i).getTitle();

        }

        initSpinner(context, exercises, spinner_exercises);

    }

    public void initSpinnerCategories(Context context){

        String[] categories = new String[exercise_categories_res.getExerciseCategories().size()];

        for(int i = 0; i < exercise_categories_res.getExerciseCategories().size(); i++){

            categories[i] = exercise_categories_res.getExerciseCategories().get(i).getCategory();

        }

        initSpinner(context, categories, spinner_category);

    }

    public void initSpinner(Context context, String[] data, Spinner spinner){

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(context, R.layout.spinner_item, data);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinner_adapter);

    }

}