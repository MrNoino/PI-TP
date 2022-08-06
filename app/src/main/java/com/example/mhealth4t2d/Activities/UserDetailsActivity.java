package com.example.mhealth4t2d.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.GetUserResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserDetailsActivity extends AppCompatActivity {

    private int userId;

    private EditText input_name, input_email, input_gender, input_day, input_month, input_year, input_goal_steps, input_goal_kal, input_goal_sleep, input_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.label_details);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar. setDisplayHomeAsUpEnabled(true);

        input_name = findViewById(R.id.input_name);

        input_email = findViewById(R.id.input_email);

        input_gender = findViewById(R.id.input_gender);

        input_day = findViewById(R.id.input_day);

        input_month = findViewById(R.id.input_month);

        input_year = findViewById(R.id.input_year);

        input_goal_steps = findViewById(R.id.input_goal_steps);

        input_goal_kal = findViewById(R.id.input_goal_kal);

        input_goal_sleep = findViewById(R.id.input_goal_sleep);

        input_position = findViewById(R.id.input_position);

        Utilities.displayProgressDialog(this, "A carregar o conteúdo", "Por favor espere...",  R.drawable.ic_warning,false);

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody.put("email", Preferences.getStringPref(this, "email"));
            jsonBody.put("password",  Preferences.getStringPref(this, "password"));

        } catch (JSONException e) {

            e.printStackTrace();

        }

        Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getUsers/"+ userId, Request.Method.POST, jsonBody, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            GetUserResponse res = gson.fromJson(String.valueOf(response), GetUserResponse.class);

            Utilities.removeProgressDialog();

            if(res.getCode() == 200){

                toolbar.setTitle(res.getUser().getName());

                input_name.setText(res.getUser().getName());

                input_email.setText(res.getUser().getEmail());

                input_gender.setText(res.getUser().getGender());

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

                Utilities.displaySnackBar(findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }

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