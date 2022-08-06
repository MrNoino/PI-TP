package com.example.mhealth4t2d.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.DefaultResponse;
import com.example.mhealth4t2d.Responses.GetExercisesResponse;
import com.example.mhealth4t2d.Responses.GetUsersResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AssociateExerciseActivity extends AppCompatActivity {

    private int userId;

    private TimePicker timepicker_time;

    private CalendarView calendarView;

    private Spinner spinner_exercises, spinner_users;

    private Button btn_associate;

    GetExercisesResponse exercises_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_exercise);

        timepicker_time = findViewById(R.id.timepicker_time);

        timepicker_time.setIs24HourView(true);

        calendarView = findViewById(R.id.calendar_date);

        spinner_exercises = findViewById(R.id.spinner_exercises);

        btn_associate = findViewById(R.id.btn_associate_exercise);

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.label_associate_exercise);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar. setDisplayHomeAsUpEnabled(true);

        Utilities.displayProgressDialog(this, "A carregar o conteúdo", "Por favor espere...",  R.drawable.ic_warning,false);

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody.put("email", Preferences.getStringPref(this, "email"));
            jsonBody.put("password",  Preferences.getStringPref(this, "password"));

        } catch (JSONException e) {

            e.printStackTrace();

        }

        Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getExercises/", Request.Method.GET, null, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            exercises_res = gson.fromJson(String.valueOf(response), GetExercisesResponse.class);

            Utilities.removeProgressDialog();

            if(exercises_res.getCode() == 200){

                String[] titles = new String[exercises_res.getExercises().size()];

                for(int i = 0; i < titles.length; i++){

                    titles[i] = exercises_res.getExercises().get(i).getTitle();

                }

                ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, titles);

                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_exercises.setAdapter(spinner_adapter);

            }else{

                Utilities.displaySnackBar(findViewById(R.id.content), exercises_res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }

        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                view.setDate(calendar.getTimeInMillis());

            }
        });


        btn_associate.setOnClickListener(l ->{

            JSONObject jsonBody2 = new JSONObject();

            try {

                jsonBody2.put("email", Preferences.getStringPref(this, "email"));
                jsonBody2.put("password",  Preferences.getStringPref(this, "password"));
                jsonBody2.put("datetime", new SimpleDateFormat("yyyy/MM/dd").format(new Date(calendarView.getDate())) + " " + String.valueOf(timepicker_time.getCurrentHour()) + ":" + String.valueOf(timepicker_time.getCurrentMinute()));
                jsonBody2.put("exercise_id",  exercises_res.getExercises().get(spinner_exercises.getSelectedItemPosition()).getId());
                jsonBody2.put("user_id",  userId);

            } catch (JSONException e) {

                e.printStackTrace();

            }

            Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/associateExercise/", Request.Method.POST, jsonBody2, response ->{

                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();

                Gson gson = builder.create();
                DefaultResponse res = gson.fromJson(String.valueOf(response), DefaultResponse.class);

                Utilities.removeProgressDialog();

                Utilities.displaySnackBar(findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            });

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                this.onBackPressed();
                return true;

        }

        return false;

    }

}