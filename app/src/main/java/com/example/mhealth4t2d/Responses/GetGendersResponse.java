package com.example.mhealth4t2d.Responses;

public class GetGendersResponse {

    private int code;

    private String message;

    private String[] genders;

    public GetGendersResponse(int code, String message, String[] genders) {

        this.code = code;
        this.message = message;
        this.genders = genders;

    }

    public int getCode() {

        return this.code;

    }

    public void setCode(int code) {

        this.code = code;

    }

    public String getMessage() {

        return this.message;

    }

    public void setMessage(String message) {

        this.message = message;

    }

    public String[] getGenders() {

        return this.genders;

    }

    public void setGenders(String[] genders) {

        this.genders = genders;

    }
}
