package com.example.dell.letschat;

/**
 * Created by DELL on 11/12/2017.
 */

public class Messages {
    private String message;
    private String from;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {

        return from;
    }

    public Messages(String message,String from) {
        this.message = message;
        this.from=from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Messages()
    {

    }

}
