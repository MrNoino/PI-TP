package com.example.mhealth4t2d.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.mhealth4t2d.Models.Question;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.RecyclerViewAdapters.QuizRecyclerViewAdapter;
import com.example.mhealth4t2d.Responses.QuizzesResponse;
import com.example.mhealth4t2d.Models.Quiz;
import com.example.mhealth4t2d.Utils.Preferences;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    private int quizPosition = 0;

    private SectionedRecyclerViewAdapter adapter;

    private Button nextBtn;

    private QuizzesResponse res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        nextBtn = findViewById(R.id.btn_next);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_quizzes);

        adapter = new SectionedRecyclerViewAdapter();

        Utilities.displayProgressDialog(this, "A carregar os questionários", "Por favor espere...",  R.drawable.ic_warning,false);

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody.put("email", Preferences.getStringPref(this, "email"));
            jsonBody.put("password",  Preferences.getStringPref(this, "password"));
            jsonBody.put("date", formatter.format(new Date()));

        } catch (JSONException e) {

            e.printStackTrace();

        }

        Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getQuizzes/", Request.Method.POST, jsonBody, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            res = gson.fromJson(String.valueOf(response), QuizzesResponse.class);

            Utilities.removeProgressDialog();

            if(res.getCode() == 200){

                if(res.getQuizzes().size() != 0){

                    toolbar.setTitle(res.getQuizzes().get(quizPosition).getTitle());

                    this.loadAdpater(res.getQuizzes());

                }else{

                    Intent intent = new Intent(this, MainActivity.class);

                    startActivity(intent);

                }

            }else{

                Utilities.displaySnackBar(findViewById(R.id.content), res.getMessage(), Snackbar.ANIMATION_MODE_SLIDE, getString(R.string.label_close).toUpperCase(), v -> {

                    Utilities.removeSnackBar();

                });

            }



        });


        nextBtn.setOnClickListener(v ->{

            quizPosition++;

            if(res.getQuizzes().size() > quizPosition){

                toolbar.setTitle(res.getQuizzes().get(quizPosition).getTitle());

                this.loadAdpater(res.getQuizzes());

            }else{

                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);

            }

        });

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


    private void loadAdpater(ArrayList<Quiz> quizzes){

        adapter.removeAllSections();

        if(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Sim e Não").size() != 0){

            adapter.addSection(new QuizRecyclerViewAdapter(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Sim e Não"), "Resposta Sim e Não", R.layout.quiz_section, R.layout.yes_no_answer_item));

        }

        if(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Sim e Não com Complemento").size() != 0){

            adapter.addSection(new QuizRecyclerViewAdapter(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Sim e Não com Complemento"), "Resposta Sim e Não com Complemento", R.layout.quiz_section, R.layout.yes_no_answer_complement_item));


        }

        if(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Escrita").size() != 0){

            adapter.addSection(new QuizRecyclerViewAdapter(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Escrita"), "Resposta Escrita", R.layout.quiz_section, R.layout.written_answer_item));

        }

        if(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Numérica").size() != 0){

            adapter.addSection(new QuizRecyclerViewAdapter(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Numérica"), "Resposta Numérica", R.layout.quiz_section, R.layout.numeric_answer_item));

        }

        if(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Númerica Dupla").size() != 0){

            adapter.addSection(new QuizRecyclerViewAdapter(quizzes.get(quizPosition).getQuestionsByCategory("Resposta Númerica Dupla"), "Resposta Númerica Dupla", R.layout.quiz_section, R.layout.double_numeric_answer_item));

        }

        if(quizzes.get(quizPosition).getQuestionsByCategory("Resposta do Tipo de Passo").size() != 0){

            adapter.addSection(new QuizRecyclerViewAdapter(quizzes.get(quizPosition).getQuestionsByCategory("Resposta do Tipo de Passo"), "Resposta do Tipo de Passo", R.layout.quiz_section, R.layout.spinner_answer_item));

        }

        if(quizzes.get(quizPosition).getQuestionsByCategory("Resposta na Escala Borg").size() != 0){

            adapter.addSection(new QuizRecyclerViewAdapter(quizzes.get(quizPosition).getQuestionsByCategory("Resposta na Escala Borg"), "Resposta na Escala Borg", R.layout.quiz_section, R.layout.spinner_answer_item));

        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

    }

}