package com.example.mhealth4t2d.Responses;

import com.example.mhealth4t2d.Models.Exercise;
import com.example.mhealth4t2d.Models.Question;

import java.util.ArrayList;

public class GetExercisesResponse {

    private int code;

    private String message;

    private ArrayList<Exercise> exercises;

    public GetExercisesResponse(int code, String message, ArrayList<Exercise> exercises) {

        this.code = code;
        this.message = message;
        this.exercises = exercises;

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

    public ArrayList<Exercise> getExercises() {

        return exercises;

    }

    public void setExercises(ArrayList<Exercise> exercises) {

        this.exercises = exercises;

    }

    public ArrayList<Exercise> getExercisesByCategory(String category){

        ArrayList<Exercise> exercisesByCategory = new ArrayList<Exercise>();

        for (Exercise exercise : this.exercises) {

            if(exercise.getCategory().equals(category)){

                exercisesByCategory.add(exercise);

            }

        }

        return exercisesByCategory;

    }

}
