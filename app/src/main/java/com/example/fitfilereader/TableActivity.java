package com.example.fitfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    int trainingId;
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        trainingId = getIntent().getIntExtra("ID_TRAINING", 0);

        table = findViewById(R.id.training_table);

        loadTrainingDataFromDatabase(trainingId);
    }

    private String showPaceTimeSwim(int seconds){
        int sec = seconds % 60;
        int min = (seconds % 3600) / 60;

        String swimTime = String.format("%02d:%02d", min, sec);
        return swimTime;
    }

    private void loadTrainingDataFromDatabase(int trainingId){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        List<FitFile> fitFileList = database.fileDao().getOneTraining(trainingId);

        ArrayList<String> headerList = new ArrayList<String>();

        headerList.add("Interval ");
        headerList.add("Swim Storke ");
        headerList.add("Total Swum Distance ");
        headerList.add("Average Pace /100m ");
        headerList.add("Average Heart Rate ");
        headerList.add("Average Cadence ");
        headerList.add("Burned Kcal ");

        for(int i = 0; i < fitFileList.size(); i++) {

            if(i == 0){
                TextView tvId = new TextView(this, null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvSwimStorke = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvTotalSwumDistance = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvAveragePace = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvAverageHeartRate = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvAverageCadence = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvBurnedKcal = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);

                tvId.setGravity(Gravity.CENTER);
                tvSwimStorke.setGravity(Gravity.CENTER);
                tvTotalSwumDistance.setGravity(Gravity.CENTER);
                tvAveragePace.setGravity(Gravity.CENTER);
                tvAverageHeartRate.setGravity(Gravity.CENTER);
                tvAverageCadence.setGravity(Gravity.CENTER);
                tvBurnedKcal.setGravity(Gravity.CENTER);

                TableRow row = new TableRow(this);

                tvId.setText(headerList.get(0));
                tvSwimStorke.setText(headerList.get(1));
                tvTotalSwumDistance.setText(headerList.get(2));
                tvAveragePace.setText(headerList.get(3));
                tvAverageHeartRate.setText(headerList.get(4));
                tvAverageCadence.setText(headerList.get(5));
                tvBurnedKcal.setText(headerList.get(6));

                row.addView(tvId);
                row.addView(tvSwimStorke);
                row.addView(tvTotalSwumDistance);
                row.addView(tvAveragePace);
                row.addView(tvAverageHeartRate);
                row.addView(tvAverageCadence);
                row.addView(tvBurnedKcal);

                table.addView(row);
            }
            if(i % 2 == 0) {
                TextView tvId = new TextView(this, null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvSwimStorke = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvTotalSwumDistance = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvAveragePace = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvAverageHeartRate = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvAverageCadence = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);
                TextView tvBurnedKcal = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeader);

                tvId.setGravity(Gravity.CENTER);
                tvSwimStorke.setGravity(Gravity.CENTER);
                tvTotalSwumDistance.setGravity(Gravity.CENTER);
                tvAveragePace.setGravity(Gravity.CENTER);
                tvAverageHeartRate.setGravity(Gravity.CENTER);
                tvAverageCadence.setGravity(Gravity.CENTER);
                tvBurnedKcal.setGravity(Gravity.CENTER);

                TableRow row = new TableRow(this);

                String strID = String.format("%s", i + 1);
                tvId.setText(strID);
                tvSwimStorke.setText(fitFileList.get(i).swimStorkeDb);
                tvTotalSwumDistance.setText(String.valueOf(fitFileList.get(i).totalSwimDistanceDb));
                if(fitFileList.get(i).swimStorkeDb.equals("BREAK")) {
                    tvAveragePace.setText("-");
                } else {
                    tvAveragePace.setText(showPaceTimeSwim((int) (100 / fitFileList.get(i).avarageSpeedDb)));
                }
                tvAverageHeartRate.setText(String.valueOf(fitFileList.get(i).avgHeartRateDb));
                tvAverageCadence.setText(String.valueOf(fitFileList.get(i).avarageCadenceDb));
                tvBurnedKcal.setText(String.valueOf(fitFileList.get(i).kcalSwimDb));

                row.addView(tvId);
                row.addView(tvSwimStorke);
                row.addView(tvTotalSwumDistance);
                row.addView(tvAveragePace);
                row.addView(tvAverageHeartRate);
                row.addView(tvAverageCadence);
                row.addView(tvBurnedKcal);

                table.addView(row);
            } else {
                TextView tvId = new TextView(this, null, 0, R.style.NewFontStyleInTableActivityHeaderLight);
                TextView tvSwimStorke = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeaderLight);
                TextView tvTotalSwumDistance = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeaderLight);
                TextView tvAveragePace = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeaderLight);
                TextView tvAverageHeartRate = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeaderLight);
                TextView tvAverageCadence = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeaderLight);
                TextView tvBurnedKcal = new TextView(this,null, 0, R.style.NewFontStyleInTableActivityHeaderLight);

                tvId.setGravity(Gravity.CENTER);
                tvSwimStorke.setGravity(Gravity.CENTER);
                tvTotalSwumDistance.setGravity(Gravity.CENTER);
                tvAveragePace.setGravity(Gravity.CENTER);
                tvAverageHeartRate.setGravity(Gravity.CENTER);
                tvAverageCadence.setGravity(Gravity.CENTER);
                tvBurnedKcal.setGravity(Gravity.CENTER);

                TableRow row = new TableRow(this);

                String strID = String.format("%s", i + 1);
                tvId.setText(strID);
                tvSwimStorke.setText(fitFileList.get(i).swimStorkeDb);
                tvTotalSwumDistance.setText(String.valueOf(fitFileList.get(i).totalSwimDistanceDb));
                if(fitFileList.get(i).swimStorkeDb.equals("BREAK")) {
                    tvAveragePace.setText("-");
                } else {
                    tvAveragePace.setText(showPaceTimeSwim((int) (100 / fitFileList.get(i).avarageSpeedDb)));
                }
                tvAverageHeartRate.setText(String.valueOf(fitFileList.get(i).avgHeartRateDb));
                tvAverageCadence.setText(String.valueOf(fitFileList.get(i).avarageCadenceDb));
                tvBurnedKcal.setText(String.valueOf(fitFileList.get(i).kcalSwimDb));

                row.addView(tvId);
                row.addView(tvSwimStorke);
                row.addView(tvTotalSwumDistance);
                row.addView(tvAveragePace);
                row.addView(tvAverageHeartRate);
                row.addView(tvAverageCadence);
                row.addView(tvBurnedKcal);

                table.addView(row);
            }
        }
    }
}