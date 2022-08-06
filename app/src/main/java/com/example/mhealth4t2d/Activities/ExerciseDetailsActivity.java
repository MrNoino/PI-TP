package com.example.mhealth4t2d.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.GetExerciseResponse;
import com.example.mhealth4t2d.Utils.Requests;
import com.example.mhealth4t2d.Utils.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExerciseDetailsActivity extends AppCompatActivity {

    private int exerciseId;

    private ImageView imageView;

    private TextView txtview_title, txtview_description, txtview_category, txtview_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        Intent intent = getIntent();
        exerciseId = intent.getIntExtra("id", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.label_details);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar. setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imgview_image);

        txtview_title = findViewById(R.id.txtview_title_content);

        txtview_description = findViewById(R.id.txtview_description_content);

        txtview_category = findViewById(R.id.txtview_category_content);

        txtview_video = findViewById(R.id.txtview_video_link);

        Utilities.displayProgressDialog(this, "A carregar o conteúdo", "Por favor espere...",  R.drawable.ic_warning,false);

        Requests.JsonRequest(getApplicationContext(), findViewById(R.id.content), "https://mhealth4t2d-api.herokuapp.com/getExercises/"+ exerciseId, Request.Method.GET, null, response ->{

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            GetExerciseResponse res = gson.fromJson(String.valueOf(response), GetExerciseResponse.class);

            Utilities.removeProgressDialog();

            if(res.getCode() == 200){

                LoadImage loadImage = new LoadImage(imageView);

                loadImage.execute(res.getExercise().getImage_link());

                txtview_title.setText(res.getExercise().getTitle());

                txtview_description.setText(res.getExercise().getDescription());

                txtview_category.setText(res.getExercise().getCategory());

                if(res.getExercise().getVideo_link() != null){

                    txtview_video.setText(getResources().getString(R.string.label_click_link) + ": " + res.getExercise().getVideo_link());

                }


                Linkify.addLinks(txtview_video, Linkify.WEB_URLS);

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

    private class LoadImage extends AsyncTask<String, Void, Bitmap>{

        private ImageView imageView;

        public LoadImage(ImageView imageView) {

            this.imageView = imageView;

        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            String urlLink = strings[0];

            Bitmap bitmap = null;

            try {

                InputStream inputStream = new URL(urlLink).openStream();

                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (IOException e) {

                e.printStackTrace();

            }

            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if(bitmap != null){

                imageView.setImageBitmap(bitmap);

            }else{

                imageView.setImageResource(R.drawable.ic_noimage);

            }


        }
    }


}