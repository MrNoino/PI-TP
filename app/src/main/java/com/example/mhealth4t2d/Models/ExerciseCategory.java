package com.example.mhealth4t2d.Models;

public class ExerciseCategory {

    private int id;

    private String category;

    public ExerciseCategory(int id, String category) {

        this.id = id;
        this.category = category;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getCategory() {

        return category;

    }

    public void setCategory(String category) {

        this.category = category;

    }

}
