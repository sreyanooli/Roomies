package edu.niu.cs.shreya.roomies;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Ram on 5/8/2017.
 */

public class Post {

    private String email;
    private String message;
    private Long postedTime;

    public Long getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(Long postedTime) {
        this.postedTime = postedTime;
    }

    @Override
    public String toString() {

        String stringDate = DateFormat.getDateTimeInstance().format(postedTime);

        return "From: " + email + "\nMessage: " + message + "\n" + stringDate + "\n\n\n\n";
    }

    public Post() {

    }

    public Post(String email, String message) {
        this.email = email;
        this.message = message;

        postedTime = new Date().getTime();

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
