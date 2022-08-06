package com.example.mhealth4t2d.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.GetGendersResponse;
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
import java.time.Year;
import java.util.Locale;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.label_signup);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar. setDisplayHomeAsUpEnabled(true);

        Spinner spinner_gender = findViewById(R.id.spinner_gender);

        initSpinner(spinner_gender, R.layout.spinner_item);

        EditText input_name = findViewById(R.id.input_name);
        EditText input_email = findViewById(R.id.input_email);
        EditText input_password = findViewById(R.id.input_password);
        EditText input_confirm_pwd = findViewById(R.id.input_confirm_pwd);
        EditText input_day = findViewById(R.id.input_day);
        EditText input_month = findViewById(R.id.input_month);
        EditText input_year = findViewById(R.id.input_year);
        CheckBox checkbos_terms = findViewById(R.id.checkbox_terms);

        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(v -> {

            if(input_name.getText().toString().isEmpty() || input_email.getText().toString().isEmpty() || input_password.getText().toString().isEmpty() || input_day.getText().toString().isEmpty() || input_month.getText().toString().isEmpty() || input_year.getText().toString().isEmpty()){

                Utilities.displaySnackBar(findViewById(R.id.content), getString(R.string.error_empty_fields), Snackbar.LENGTH_INDEFINITE, getString(R.string.label_close).toUpperCase(), l -> {

                    Utilities.removeSnackBar();

                });

                return;

            }

            Pattern email_regex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            if(!email_regex.matcher(input_email.getText()).matches()){

                Utilities.displaySnackBar(findViewById(R.id.content), getString(R.string.error_invalid_email), Snackbar.LENGTH_INDEFINITE, getString(R.string.label_close).toUpperCase(), l -> {

                    Utilities.removeSnackBar();

                });

                return;

            }

            if(!input_password.getText().toString().equals(input_confirm_pwd.getText().toString())){

                Utilities.displaySnackBar(findViewById(R.id.content), getString(R.string.error_confirm_pwd_not_match), Snackbar.LENGTH_INDEFINITE, getString(R.string.label_close).toUpperCase(), l -> {

                    Utilities.removeSnackBar();

                });

                return;

            }


            if(Integer.parseInt(input_year.getText().toString()) < (Year.now().getValue() - 200)){

                Utilities.displaySnackBar(findViewById(R.id.content), getString(R.string.error_invalid_date), Snackbar.LENGTH_INDEFINITE, getString(R.string.label_close).toUpperCase(), l -> {

                    Utilities.removeSnackBar();

                });

                return;

            }

            String birthdate = input_year.getText() + "-" + input_month.getText() + "-" + input_day.getText();

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            format.setLenient(false);
            try {

                format.parse(birthdate);

            } catch (ParseException e) {

                Utilities.displaySnackBar(findViewById(R.id.content), getString(R.string.error_invalid_date), Snackbar.LENGTH_INDEFINITE, getString(R.string.label_close).toUpperCase(), l -> {

                    Utilities.removeSnackBar();

                });


                return;
            }

            if(!checkbos_terms.isChecked()){

                Utilities.displaySnackBar(findViewById(R.id.content), getString(R.string.error_terms_not_accepted), Snackbar.LENGTH_INDEFINITE, getString(R.string.label_close).toUpperCase(), l -> {

                    Utilities.removeSnackBar();

                });

                return;

            }

            Utilities.displayAlertDialog(this, "Aviso", "Deseja mesmo criar conta?", true, "Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    JSONObject jsonBody = new JSONObject();

                    try {

                        jsonBody.put("name", input_name.getText().toString());
                        jsonBody.put("email", input_email.getText().toString());
                        jsonBody.put("password", input_password.getText().toString());
                        jsonBody.put("gender",spinner_gender.getSelectedItem().toString());
                        jsonBody.put("birthdate", birthdate);

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/signup/", Request.Method.POST, jsonBody, response ->{});

                }
            }, "", null, "Cancelar", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Utilities.dismissAlertDialog();

                }

            }, R.drawable.ic_warning);

        });

        TextView txtview_terms = (TextView) findViewById(R.id.txt_view_terms);
        txtview_terms.setOnClickListener(v -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mhealth4t2d.sytes.net/termos_politica_privacidade.pdf"));
            startActivity(browserIntent);

        });



    }

    private void initSpinner(Spinner spinner, int spinner_layout){

        Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getGenders/", Request.Method.GET, null, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            GetGendersResponse res = gson.fromJson(String.valueOf(response), GetGendersResponse.class);

            if(res.getCode() == 200) {

                ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, spinner_layout, res.getGenders());

                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(spinner_adapter);

            }else {

                Toast.makeText(this, res.getMessage(), Toast.LENGTH_SHORT).show();
                this.finish();

            }

        });

    }

}