package com.kdao.college_recommend_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;

public class RecommendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
    }

    /**
     * Function to navigate back home
     * @param v
     */
    public void navigateToMainActivity(View v) {
        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(newIntent);
    }
}
