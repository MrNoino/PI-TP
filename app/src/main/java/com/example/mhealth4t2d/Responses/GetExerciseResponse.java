package com.example.mhealth4t2d.Responses;

import com.example.mhealth4t2d.Models.Exercise;

public class GetExerciseResponse {

    private int code;

    private String message;

    private Exercise exercise;

    public GetExerciseResponse(int code, String message, Exercise exercise) {

        this.code = code;
        this.message = message;
        this.exercise = exercise;

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

    public Exercise getExercise() {

        return exercise;

    }

    public void setExercise(Exercise exercise) {

        this.exercise = exercise;

    }

}
