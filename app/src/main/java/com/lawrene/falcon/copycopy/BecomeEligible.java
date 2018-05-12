package com.lawrene.falcon.copycopy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class BecomeEligible extends AppCompatActivity {

    Button eligibilityButton;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_eligible);

        mToolbar = (Toolbar) findViewById(R.id.become_eligible_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Become Eligible");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
