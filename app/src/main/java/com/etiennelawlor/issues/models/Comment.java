package com.etiennelawlor.issues.models;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class Comment {

    // region Member Variables
    private String mUserName;
    private String mBody;
    // endregion

    // region Constructors
    public Comment(String userName, String body){
        mUserName = userName;
        mBody = body;
    }
    // endregion

    // region Getters
    public String getUserName() {
        return mUserName;
    }

    public String getBody() {
        return mBody;
    }
    // endregion

    // region Setters
    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setBody(String body) {
        mBody = body;
    }
    // endregion
}
