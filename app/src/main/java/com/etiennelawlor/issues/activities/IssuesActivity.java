package com.etiennelawlor.issues.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.etiennelawlor.issues.R;
import com.etiennelawlor.issues.fragments.IssuesFragment;

public class IssuesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new IssuesFragment())
                    .commit();
        }
    }
}
