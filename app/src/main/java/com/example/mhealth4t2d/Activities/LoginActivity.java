package com.example.mhealth4t2d.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.DefaultResponse;
import com.example.mhealth4t2d.Responses.GetPositionResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class  LoginActivity extends AppCompatActivity {

    TextView input_email;
    TextView input_password;
    CheckBox checkbox_keep_logged;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.label_login);
        setSupportActionBar(toolbar);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        checkbox_keep_logged = findViewById(R.id.checkbox_keep_logged);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> {


            Preferences.editStringPref(getApplicationContext(), "email", "");
            Preferences.editStringPref(getApplicationContext(), "password", "");


            makeLoginRequest(input_email.getText().toString(), input_password.getText().toString());

            if(checkbox_keep_logged.isChecked()){

                Preferences.editIntPref(getApplicationContext(), "keep_logged", 1);

            }

            input_password.setText("");

        });

        TextView txtview_signup = findViewById(R.id.txtview_signup);
        txtview_signup.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);

        });

        Button btn_visibility = findViewById(R.id.btn_visibility);
        btn_visibility.setOnClickListener(view -> {

            if(input_password.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")){

                btn_visibility.setBackground(getResources().getDrawable(R.drawable.ic_visibility_off));
                input_password.setTransformationMethod(new SingleLineTransformationMethod());

            }else{

                btn_visibility.setBackground(getDrawable(R.drawable.ic_visibility));
                input_password.setTransformationMethod(new PasswordTransformationMethod());

            }

        });

        if(Preferences.getIntPref(this, "keep_logged") == 1){

            makeLoginRequest(Preferences.getStringPref(this, "email"), Preferences.getStringPref(this, "password"));

        }

    }



    private void makeLoginRequest(String email, String password){

        Utilities.displayProgressDialog(this, "A Iniciar Sessão", "Por favor espere...",  R.drawable.ic_login,false);

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody.put("email", email);
            jsonBody.put("password", password);

        } catch (JSONException e) {

            e.printStackTrace();

        }

        Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/login/", Request.Method.POST, jsonBody, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            DefaultResponse res = gson.fromJson(String.valueOf(response), DefaultResponse.class);

            Utilities.removeProgressDialog();

            if(res.getCode() == 200){

                Preferences.editStringPref(getApplicationContext(), "email", email);
                Preferences.editStringPref(getApplicationContext(), "password", password);

                JSONObject jsonBody2 = new JSONObject();

                try {

                    jsonBody2.put("email", Preferences.getStringPref(this, "email"));
                    jsonBody2.put("password",  Preferences.getStringPref(this, "password"));

                } catch (JSONException e) {

                    e.printStackTrace();

                }

                Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getPosition/", Request.Method.POST, jsonBody, response2 -> {

                    GetPositionResponse res2 = gson.fromJson(String.valueOf(response2), GetPositionResponse.class);

                    if (res2.getCode() == 200) {

                        if(res2.getPosition().equals("Administrador")) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                            startActivity(intent);

                        }

                    } else {

                        Utilities.displaySnackBar(findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                            Utilities.removeSnackBar();

                        });

                    }

                });

            }else{

                Preferences.editStringPref(getApplicationContext(), "email", "");
                Preferences.editStringPref(getApplicationContext(), "password", "");

                Utilities.displaySnackBar(findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }



        });
    }
}