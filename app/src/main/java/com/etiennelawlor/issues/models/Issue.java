package com.etiennelawlor.issues.models;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class Issue {

    // region Member Variables
    private String mTitle;
    private String mBody;
    private int mNumber;
    // endregion

    // region Constructors
    public Issue(String title, String body, int number){
        mTitle = title;
        mBody = body;
        mNumber = number;
    }
    // endregion

    // region Getters
    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    public int getNumber() {
        return mNumber;
    }
    // endregion

    // region Setters
    public void setTitle(String title) {
        mTitle = title;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public void setNumber(int number) {
        mNumber = number;
    }
    // endregion
}
