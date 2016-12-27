package com.etiennelawlor.issues.models;

import com.etiennelawlor.issues.utilities.DateUtility;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class Issue {

    // region Constants
    public static final String PATTERN = "yyyy-MM-dd'T'hh:mm:ss'Z'";
    // endregion

    // region Member Variables
    private String title;
    private String body;
    private int number;
    private String createdAt;
    private String updatedAt;
    private User user;
    // endregion

    // region Constructors
    public Issue(){}
    // endregion

    // region Getters
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getNumber() {
        return number;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public String getFormatedCreatedAt(){
        String formatedCreatedAt = DateUtility.getFormattedDateAndTime(DateUtility.getCalendar(createdAt, PATTERN), DateUtility.FORMAT_RELATIVE);
        return formatedCreatedAt;
    }

    public String getFormatedUpdatedAt(){
        String formatedUpdatedAt = DateUtility.getFormattedDateAndTime(DateUtility.getCalendar(updatedAt, PATTERN), DateUtility.FORMAT_RELATIVE);
        return formatedUpdatedAt;
    }

    // endregion

    // region Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // endregion
}
