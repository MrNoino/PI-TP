 package com.example.mhealth4t2d.Utils;

 import android.content.Context;
 import android.view.View;

 import com.android.volley.AuthFailureError;
 import com.android.volley.NetworkError;
 import com.android.volley.ParseError;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.ServerError;
 import com.android.volley.TimeoutError;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.android.volley.toolbox.Volley;
 import com.example.mhealth4t2d.R;
 import com.google.android.material.snackbar.Snackbar;

 import org.json.JSONObject;

 public class Requests {

     private static RequestQueue requestQueue;

     public static void JsonRequest(Context context, View view, String url, int method, JSONObject jsonBody, Response.Listener<JSONObject> onResponse){

         if(requestQueue == null){

             requestQueue = Volley.newRequestQueue(context);

         }

         JsonObjectRequest jsonRequest = new JsonObjectRequest(method, url, jsonBody, onResponse, error -> {

             Utilities.removeProgressDialog();

             Utilities.displaySnackBar(view, context.getString(getIdError(error)), Snackbar.ANIMATION_MODE_SLIDE, context.getString(R.string.label_close).toUpperCase(), v -> {

                 Utilities.removeSnackBar();

             });

         });

         requestQueue.add(jsonRequest);

     }

    private static int getIdError(VolleyError error){


        if (error instanceof NetworkError || error instanceof AuthFailureError) {

            return R.string.error_networkerror;

        } else if (error instanceof ServerError) {

            return R.string.error_servererror;

        } else if (error instanceof ParseError) {

            return R.string.error_parsingerror;

        } else if (error instanceof TimeoutError) {

            return R.string.error_timeouterror;

        }else{

            return R.string.error_defaulterror;

        }

    }

 }
