package com.example.mhealth4t2d.Responses;

import com.example.mhealth4t2d.Models.ExerciseCategory;

import java.util.ArrayList;

public class GetExerciseCategoriesResponse {

    private int code;

    private String message;

    private ArrayList<ExerciseCategory> categories;

    public GetExerciseCategoriesResponse(int code, String message, ArrayList<ExerciseCategory> categories) {

        this.code = code;
        this.message = message;
        this.categories = categories;

    }

    public int getCode() {

        return code;

    }

    public void setCode(int code) {

        this.code = code;

    }

    public String getMessage() {

        return message;

    }

    public void setMessage(String message) {

        this.message = message;

    }

    public ArrayList<ExerciseCategory> getExerciseCategories() {

        return categories;

    }

    public void setExerciseCategories(ArrayList<ExerciseCategory> categories) {

        this.categories = categories;

    }
}
