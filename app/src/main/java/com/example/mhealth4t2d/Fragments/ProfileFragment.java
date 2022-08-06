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
import com.example.mhealth4t2d.Responses.GetExercisesResponse;
import com.example.mhealth4t2d.Responses.GetGendersResponse;
import com.example.mhealth4t2d.Responses.GetUserResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ProfileFragment extends Fragment {

    private EditText input_name, input_email, input_day, input_month, input_year, input_goal_steps, input_goal_kal, input_goal_sleep, input_position;

    private Spinner spinner_gender;

    private Button btn_edit;

    private GetGendersResponse gender_res;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburguer);

        input_name = view.findViewById(R.id.input_name);

        input_email = view.findViewById(R.id.input_email);

        spinner_gender = view.findViewById(R.id.spinner_gender);

        input_day = view.findViewById(R.id.input_day);

        input_month = view.findViewById(R.id.input_month);

        input_year = view.findViewById(R.id.input_year);

        input_goal_steps = view.findViewById(R.id.input_goal_steps);

        input_goal_kal = view.findViewById(R.id.input_goal_kal);

        input_goal_sleep = view.findViewById(R.id.input_goal_sleep);

        input_position = view.findViewById(R.id.input_position);

        btn_edit = view.findViewById(R.id.btn_edit);

        Utilities.displayProgressDialog(view.getContext(), "A carregar o perfil", "Por favor espere...",  R.drawable.ic_warning,false);

        Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getGenders/", Request.Method.GET, null, response -> {

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            gender_res = gson.fromJson(String.valueOf(response), GetGendersResponse.class);

            if (gender_res.getCode() == 200) {

                ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, gender_res.getGenders());

                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_gender.setAdapter(spinner_adapter);

            }else{

                Utilities.displaySnackBar(view.findViewById(R.id.content), gender_res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }

        });

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody.put("email", Preferences.getStringPref(view.getContext(), "email"));
            jsonBody.put("password",  Preferences.getStringPref(view.getContext(), "password"));

        } catch (JSONException e) {

            e.printStackTrace();

        }

        Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getProfile/", Request.Method.POST, jsonBody, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            GetUserResponse res = gson.fromJson(String.valueOf(response), GetUserResponse.class);

            Utilities.removeProgressDialog();

            if(res.getCode() == 200){

                input_name.setText(res.getUser().getName());

                input_email.setText(res.getUser().getEmail());

                for(int i = 0; i < gender_res.getGenders().length; i++){

                    if(gender_res.getGenders()[i].equals(res.getUser().getGender())){

                        spinner_gender.setSelection(i);

                        break;

                    }

                }

                try {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    Date date = (Date) format.parse(res.getUser().getBirthdate());

                    Calendar cal = Calendar.getInstance();

                    cal.setTime(date);

                    input_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

                    input_month.setText(String.valueOf(cal.get(Calendar.MONTH) +1));

                    input_year.setText(String.valueOf(cal.get(Calendar.YEAR)));

                } catch (ParseException e) {

                    e.printStackTrace();
                }

                input_goal_steps.setText(String.valueOf(res.getUser().getGoal_steps()));

                input_goal_kal.setText(String.valueOf(res.getUser().getGoal_kal()));

                input_goal_sleep.setText(String.valueOf(res.getUser().getGoal_sleep()));

                input_position.setText(res.getUser().getPosition());

            }else{

                Utilities.displaySnackBar(view.findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }

        });

        btn_edit.setOnClickListener(l ->{

            String birthdate = input_year.getText() + "-" + input_month.getText() + "-" + input_day.getText();

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            format.setLenient(false);
            try {

                format.parse(birthdate);

            } catch (ParseException e) {

                Utilities.displaySnackBar(view.findViewById(R.id.content), getString(R.string.error_invalid_date), Snackbar.LENGTH_INDEFINITE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });


                return;
            }

            JSONObject jsonBody2 = new JSONObject();

            try {

                jsonBody2.put("email", Preferences.getStringPref(view.getContext(), "email"));
                jsonBody2.put("password",  Preferences.getStringPref(view.getContext(), "password"));
                jsonBody2.put("name", input_name.getText());
                jsonBody2.put("gender", spinner_gender.getSelectedItem());
                jsonBody2.put("birthdate", birthdate);
                jsonBody2.put("steps_goal", input_goal_steps.getText());
                jsonBody2.put("kal_goal", input_goal_kal.getText());
                jsonBody2.put("sleep_goal", input_goal_sleep.getText());

            } catch (JSONException e) {

                e.printStackTrace();

            }

            Requests.JsonRequest(view.getContext(), view.findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/editProfile/", Request.Method.POST, jsonBody2, response ->{

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