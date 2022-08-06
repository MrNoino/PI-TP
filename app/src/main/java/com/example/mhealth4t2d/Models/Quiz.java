package com.example.mhealth4t2d.Models;

import java.util.ArrayList;

public class Quiz {

    private String title;

    private ArrayList<Question> questions;

    public Quiz(String title, ArrayList<Question> questions) {

        this.title = title;
        this.questions = questions;

    }

    public String getTitle() {

        return title;

    }

    public void setTitle(String title) {

        this.title = title;

    }

    public ArrayList<Question> getQuestions() {

        return questions;

    }

    public void setQuestions(ArrayList<Question> questions) {

        this.questions = questions;

    }

    public ArrayList<Question> getQuestionsByCategory(String category){

        ArrayList<Question> questionsByCategory = new ArrayList<Question>();

        for (Question question : this.questions) {

            if(question.getCategory().equals(category)){

                questionsByCategory.add(question);

            }

        }

        return questionsByCategory;

    }

}
