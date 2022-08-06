package com.example.mhealth4t2d.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.GetPositionResponse;
import com.example.mhealth4t2d.Responses.QuizzesResponse;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private View fragment_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nv_view);

        fragment_holder = findViewById(R.id.fragment_holder);

        setSupportActionBar(toolbar);

        Utilities.displayProgressDialog(this, "A carregar o conteúdo", "Por favor espere...",  R.drawable.ic_warning,false);

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody.put("email", Preferences.getStringPref(this, "email"));
            jsonBody.put("password",  Preferences.getStringPref(this, "password"));

        } catch (JSONException e) {

            e.printStackTrace();

        }

        Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getPosition/", Request.Method.POST, jsonBody, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            GetPositionResponse res = gson.fromJson(String.valueOf(response), GetPositionResponse.class);

            Utilities.removeProgressDialog();

            if(res.getCode() == 200){

                NavController navController = Navigation.findNavController(this, R.id.fragment_holder);

                if(res.getPosition().equals("Administrador")){

                    navController.setGraph(R.navigation.mobile_navigation_admin);

                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.navigation_menu_admin);

                    mAppBarConfiguration = new AppBarConfiguration.Builder(
                            R.id.nav_register_exercises, R.id.nav_edit_exercises, R.id.nav_see_users, R.id.nav_profile, R.id.nav_settings)
                            .setOpenableLayout(drawer)
                            .build();



                }else{

                    navController.setGraph(R.navigation.mobile_navigation_user);

                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.navigation_menu_user);

                    mAppBarConfiguration = new AppBarConfiguration.Builder(
                            R.id.nav_plan, R.id.nav_exercises, R.id.nav_goals, R.id.nav_profile, R.id.nav_settings)
                            .setOpenableLayout(drawer)
                            .build();

                }

                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

                NavigationUI.setupWithNavController(navigationView, navController);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburguer);

            }else{

                Utilities.displaySnackBar(findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_holder);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.logout:
                Preferences.editStringPref(getApplicationContext(), "email", "");
                Preferences.editStringPref(getApplicationContext(), "password", "");
                Preferences.editIntPref(getApplicationContext(), "keep_logged", 0);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }



}