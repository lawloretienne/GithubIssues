package com.etiennelawlor.issues;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import java.io.File;

/**
 * Created by etiennelawlor on 1/20/16.
 */
public class GithubIssuesApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    // region Static Variables
    private static GithubIssuesApplication currentApplication = null;
    // endregion

    // region Member Variables
    // endregion

    // region Lifecycle Methods
    @Override
    public void onCreate() {
        super.onCreate();

        currentApplication = this;
    }
    // endregion

    // region Helper Methods
    public static GithubIssuesApplication getInstance() {
        return currentApplication;
    }

    public static File getCacheDirectory() {
        return currentApplication.getCacheDir();
    }

}
