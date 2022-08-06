package com.example.mhealth4t2d.Models;

public class User {

    private int id;

    private String name;

    private String email;

    private String gender;

    private String birthdate;

    private int goal_steps;

    private int goal_kal;

    private int goal_sleep;

    private String position;

    public User(int id, String name, String gender, String email, String birthdate, int goal_steps, int goal_kal, int goal_sleep, String position) {

        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.birthdate = birthdate;
        this.goal_steps = goal_steps;
        this.goal_kal = goal_kal;
        this.goal_sleep = goal_sleep;
        this.position = position;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getBirthdate() {

        return birthdate;

    }

    public void setBirthdate(String birthdate) {

        this.birthdate = birthdate;

    }

    public int getGoal_steps() {

        return goal_steps;

    }

    public void setGoal_steps(int goal_steps) {

        this.goal_steps = goal_steps;

    }

    public int getGoal_kal() {

        return goal_kal;

    }

    public void setGoal_kal(int goal_kal) {

        this.goal_kal = goal_kal;

    }

    public int getGoal_sleep() {

        return goal_sleep;

    }

    public void setGoal_sleep(int goal_sleep) {

        this.goal_sleep = goal_sleep;

    }

    public String getPosition() {

        return position;

    }

    public void setPosition(String position) {

        this.position = position;
        
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
