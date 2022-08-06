package com.example.mhealth4t2d.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Utilities {

    private static ProgressDialog progress;

    private static Snackbar snackbar;

    private static AlertDialog alert;

    public static void displayProgressDialog(Context context, String title, String message, int icon, boolean cancelable){

        progress = new ProgressDialog(context);
        progress.setTitle(title);
        progress.setMessage(message);
        progress.setIcon(icon);
        progress.setCancelable(cancelable);

        progress.show();

    }

    public static void removeProgressDialog(){

        progress.dismiss();

    }

    public static void displaySnackBar(View view, String message, int duration, String action, View.OnClickListener listener){

        snackbar = Snackbar.make(view, message, duration).setAction(action, listener);

        snackbar.show();

    }

    public static void removeSnackBar(){

        snackbar.dismiss();

    }

    public static void displayAlertDialog(Context context, String title, String message, boolean cancelable, String positiveBtnLabel, final DialogInterface.OnClickListener positiveBtnListener, String neutralBtnLabel, DialogInterface.OnClickListener neutralBtnListener, String negativeBtnLabel, DialogInterface.OnClickListener negativeBtnListener, int icon){

        alert = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveBtnLabel, positiveBtnListener)
                .setNeutralButton(neutralBtnLabel, neutralBtnListener)
                .setNegativeButton(negativeBtnLabel, negativeBtnListener)
                .setIcon(icon)
                .show();

    }

    public static void dismissAlertDialog(){

        alert.dismiss();

    }

}
