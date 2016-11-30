package com.etiennelawlor.issues.models;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class Comment {

    // region Member Variables
    private String userName;
    private String body;
    // endregion

    // region Constructors
    public Comment(String userName, String body){
        this.userName = userName;
        this.body = body;
    }
    // endregion

    // region Getters
    public String getUserName() {
        return userName;
    }

    public String getBody() {
        return body;
    }
    // endregion

    // region Setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBody(String body) {
        this.body = body;
    }
    // endregion
}
