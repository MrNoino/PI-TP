package com.example.mhealth4t2d.Responses;

import com.example.mhealth4t2d.Models.User;

import java.util.ArrayList;

public class GetUsersResponse {

    private int code;

    private String message;

    private ArrayList<User> users;

    public GetUsersResponse(int code, String message, ArrayList<User> users) {

        this.code = code;
        this.message = message;
        this.users = users;

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

    public ArrayList<User> getUsers() {

        return users;

    }

    public void setUsers(ArrayList<User> users) {

        this.users = users;

    }
}
