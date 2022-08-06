package com.example.mhealth4t2d.Responses;

import com.example.mhealth4t2d.Models.User;

public class GetUserResponse {

    private int code;

    private String message;

    private User user;

    public GetUserResponse(int code, String message, User user) {

        this.code = code;
        this.message = message;
        this.user = user;

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

    public User getUser() {

        return user;

    }

    public void setUser(User user) {

        this.user = user;

    }
}
