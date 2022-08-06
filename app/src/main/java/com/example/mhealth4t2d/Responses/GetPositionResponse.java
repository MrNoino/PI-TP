package com.example.mhealth4t2d.Responses;

public class GetPositionResponse {

    private int code;

    private String message;

    private String position;

    public GetPositionResponse(int code, String message, String position) {

        this.code = code;
        this.message = message;
        this.position = position;

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

    public String getPosition() {

        return position;

    }

    public void setPosition(String position) {

        this.position = position;

    }
}
