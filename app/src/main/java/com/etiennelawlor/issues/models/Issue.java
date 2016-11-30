package com.etiennelawlor.issues.models;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class Issue {

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
