package com.example.mhealth4t2d.Responses;

import com.example.mhealth4t2d.Models.Quiz;

import java.util.ArrayList;

public class QuizzesResponse {

    private int code;

    private String message;

    private ArrayList<Quiz> quizzes;

    public QuizzesResponse(int code, String message, ArrayList<Quiz> quizzes) {

        this.code = code;
        this.message = message;
        this.quizzes = quizzes;

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

    public ArrayList<Quiz> getQuizzes() {

        return quizzes;

    }

    public void setQuizzes(ArrayList<Quiz> quizzes) {

        this.quizzes = quizzes;

    }

}
