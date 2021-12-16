package com.example.fitfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GeneralStatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_statistic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}