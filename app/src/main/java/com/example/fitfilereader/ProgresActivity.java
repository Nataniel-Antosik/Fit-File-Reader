package com.example.fitfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProgresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progres);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}