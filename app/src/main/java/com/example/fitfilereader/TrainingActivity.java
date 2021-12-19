package com.example.fitfilereader;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;
import com.example.fitfilereader.db.UserData;
import com.example.fitfilereader.db.UserDatabase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    int trainingId;

    private static int warmUp;
    private static int activeRegenerationZone;
    private static int enduranceTraining;
    private static int improvedCardiovascularPerformance;
    private static int lactateThreshold;
    private static int VO2max;

    TextView tvt_date, tvt_calories, tvt_distance, tvt_avg_rate, tvt_max_rate, tvt_moving_time, tvt_elapsed_time, tvt_avg_pace, tvt_best_pace, tvt_avg_cadence;
    TextView tvt_pace_on_butterfly, tvt_pace_on_backstroke, tvt_pace_on_breaststroke, tvt_pace_on_freestyle;

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        warmUp = 0;
        activeRegenerationZone = 0;
        enduranceTraining = 0;
        improvedCardiovascularPerformance = 0;
        lactateThreshold = 0;
        VO2max = 0;

        tvt_date = findViewById(R.id.text_view_training_date);
        tvt_calories = findViewById(R.id.text_view_training_calories_data);
        tvt_distance = findViewById(R.id.text_view_training_distance_data);
        tvt_avg_rate = findViewById(R.id.text_view_training_avg_rate_data);
        tvt_max_rate = findViewById(R.id.text_view_training_max_rate_data);
        tvt_moving_time = findViewById(R.id.text_view_training_moving_time_data);
        tvt_elapsed_time = findViewById(R.id.text_view_training_elapsed_time_data);
        tvt_avg_pace = findViewById(R.id.text_view_training_avg_pace_data);
        tvt_best_pace = findViewById(R.id.text_view_training_best_pace_data);
        tvt_avg_cadence = findViewById(R.id.text_view_training_avg_cadence_data);

        tvt_pace_on_butterfly = findViewById(R.id.text_view_training_butterfly_data);
        tvt_pace_on_backstroke = findViewById(R.id.text_view_training_backstroke_data);
        tvt_pace_on_breaststroke = findViewById(R.id.text_view_training_breaststroke_data);
        tvt_pace_on_freestyle = findViewById(R.id.text_view_training_freestyle_data);

        pieChart = findViewById(R.id.training_activity_pie_chart);

        trainingId = getIntent().getIntExtra("ID_TRAINING", 0);
        LoadTrainingData(trainingId);

        setUpPieChart();
        loadPieChartData();
    }

    private void LoadTrainingData(int trainingId){

        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        UserDatabase userDatabase = UserDatabase.getDbInstance(this.getApplicationContext());

        List<FitFile> fitFileList = database.fileDao().getOneTraining(trainingId);

        if(fitFileList.isEmpty()){
            Log.d("DATABASE", "Is Empty");
        } else {
            String age = "0";

            String strKcal = String.format("%s kcal", database.fileDao().getTotalKcalSwim(trainingId));
            String strDistance = String.format("%s m", database.fileDao().getTotalSwumDistanceFile(trainingId));
            String strAvgRate = String.format("%s BPM", database.fileDao().getAvgHeartRate(trainingId));
            String strMaxRate = String.format("%s BPM", database.fileDao().getMaxHeartRate(trainingId));
            String strMovingTime = String.format("%s h", showTimeSwim(database.fileDao().getTotalSwumTime(trainingId)));
            String strElapsedTime = String.format("%s h", showTimeSwim(database.fileDao().getTotalTime(trainingId)));
            String strAvgPace = String.format("%s/100m", showPaceTimeSwim((int) (100 / database.fileDao().getAvgPace(trainingId))));
            String strBestPace = String.format("%s/100m", showPaceTimeSwim((int) (100 / database.fileDao().getMaxPace(trainingId))));

            tvt_date.setText(correctDate(database.fileDao().getTrainingDate(trainingId)));
            tvt_calories.setText(strKcal);
            tvt_distance.setText(strDistance);
            tvt_avg_rate.setText(strAvgRate);
            tvt_max_rate.setText(strMaxRate);
            tvt_moving_time.setText(strMovingTime);
            tvt_elapsed_time.setText(strElapsedTime);
            tvt_avg_pace.setText(strAvgPace);
            tvt_best_pace.setText(strBestPace);
            tvt_avg_cadence.setText(String.valueOf(database.fileDao().getAvgCadence(trainingId)));

            tvt_pace_on_butterfly.setText("-");
            tvt_pace_on_backstroke.setText("-");
            tvt_pace_on_breaststroke.setText("-");
            tvt_pace_on_freestyle.setText("-");

            if (database.fileDao().getAvgPaceOnButterfly(trainingId) != 0.0){
                String strPaceButterfly = String.format("%s/100m", showPaceTimeSwim((int) (100 / database.fileDao().getAvgPaceOnButterfly(trainingId))));
                tvt_pace_on_butterfly.setText(strPaceButterfly);
            }
            if (database.fileDao().getAvgPaceOnBackstroke(trainingId) != 0.0) {
                String strPaceBackstroke = String.format("%s/100m", showPaceTimeSwim((int) (100 / database.fileDao().getAvgPaceOnBackstroke(trainingId))));
                tvt_pace_on_backstroke.setText(strPaceBackstroke);
            }
            if (database.fileDao().getAvgPaceOnBreaststroke(trainingId) != 0.0) {
                String strPaceBreaststroke = String.format("%s/100m", showPaceTimeSwim((int) (100 / database.fileDao().getAvgPaceOnBreaststroke(trainingId))));
                tvt_pace_on_breaststroke.setText(strPaceBreaststroke);
            }
            if (database.fileDao().getAvgPaceOnFreestyle(trainingId) != 0.0) {
                String strPaceFreestyle = String.format("%s/100m", showPaceTimeSwim((int) (100 / database.fileDao().getAvgPaceOnFreestyle(trainingId))));
                tvt_pace_on_freestyle.setText(strPaceFreestyle);
            }

            age = splitDateAndGetAge(userDatabase.userDao().getUserBirthdayDate(), correctDate(database.fileDao().getTrainingDate(trainingId)));

            for (int i = 0; i < fitFileList.size(); i++) {
                typeOfTraining(Integer.parseInt(age), fitFileList.get(i).avgHeartRateDb);
            }

            Log.d("WARM-UP", String.valueOf(warmUp));
            Log.d("ACTI REG ZONE", String.valueOf(activeRegenerationZone));
            Log.d("ENDU TRAIN", String.valueOf(enduranceTraining));
            Log.d("IMPRO CARD PERF", String.valueOf(improvedCardiovascularPerformance));
            Log.d("LACT THRES", String.valueOf(lactateThreshold));
            Log.d("VOL2MAX", String.valueOf(VO2max));
        }
    }

    private String showTimeSwim(int seconds){
        int sec = seconds % 60;
        int min = (seconds % 3600) / 60;
        int hours = seconds / 3600;

        String swimTime = String.format("%02d:%02d:%02d", hours, min, sec);
        return swimTime;
    }

    public static String correctDate(String strDate){
        String [] arr = strDate.split("-");
        String newDateFormat = "";
        newDateFormat = arr[arr.length-1] + "/" + arr[1] + "/"+ arr[0];
        return newDateFormat;
    }

    private String showPaceTimeSwim(int seconds){
        int sec = seconds % 60;
        int min = (seconds % 3600) / 60;

        String swimTime = String.format("%02d:%02d", min, sec);
        return swimTime;
    }

    public void runTableActivity(View view){
        Intent intent = new Intent(this, TableActivity.class);
        intent.putExtra("ID_TRAINING", trainingId);
        startActivity(intent);
    }

    public static String splitDateAndGetAge(String strBirthdate, String strDate){
        String age = "0";

        String [] arr = strBirthdate.split("-");
        String [] arr2 = strDate.split("/");

        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int day = Integer.parseInt(arr[arr.length-1]);

        int yearTraining = Integer.parseInt(arr2[arr.length-1]);
        int monthTraining = Integer.parseInt(arr2[1]);
        int dayTraining = Integer.parseInt(arr2[0]);

        age = getAge(year, month, day, yearTraining, monthTraining, dayTraining);

        return age;
    }

    private static String getAge(int year, int month, int day, int yearTraining, int monthTraining, int dayTraining){
        Calendar birthdate = Calendar.getInstance();
        Calendar trainingDate = Calendar.getInstance();

        birthdate.set(year, month, day);
        trainingDate.set(yearTraining, monthTraining, dayTraining);

        int age = trainingDate.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);

        if (trainingDate.get(Calendar.DAY_OF_YEAR) < birthdate.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static int maxHeartRateInWater(int age) {
        int maxHeartRateInWater = 0;
        int waterDifference = 7;
        maxHeartRateInWater = 220 - age - waterDifference;
        return maxHeartRateInWater;
    }

    public static int maxHeartRate(int age) {
        int maxHeartRate = 0;
        maxHeartRate = 220 - age;
        return maxHeartRate;
    }

    public static void typeOfTraining(int age, int heartRate){
        int MHRIW = maxHeartRateInWater(age);
        if(heartRate < (int) (MHRIW * 0.60)) {
            warmUp += 1;
        } else if (heartRate >= ((int) (MHRIW * 0.60)) && heartRate <= ((int) (MHRIW * 0.65))) {
            activeRegenerationZone += 1;
        } else if (heartRate >= ((int) (MHRIW * 0.66)) && heartRate <= ((int) (MHRIW * 0.72))) {
            enduranceTraining += 1;
        } else if (heartRate >= ((int) (MHRIW * 0.73)) && heartRate <= ((int) (MHRIW * 0.83))) {
            improvedCardiovascularPerformance += 1;
        } else if (heartRate >= ((int) (MHRIW * 0.84)) && heartRate <= ((int) (MHRIW * 0.90))) {
            lactateThreshold += 1;
        } else if (heartRate >= ((int) (MHRIW * 0.91))) {
            VO2max += 1;
        }
    }
    private void setUpPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Training by Category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(warmUp, "Warm-Up"));
        entries.add(new PieEntry(activeRegenerationZone, "Active Regeneration Zone"));
        entries.add(new PieEntry(enduranceTraining, "Endurance Training"));
        entries.add(new PieEntry(improvedCardiovascularPerformance, "Improved Cardiovascular Performance"));
        entries.add(new PieEntry(lactateThreshold, "Lactate Threshold"));
        entries.add(new PieEntry(VO2max, "VO2max"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Training Zones");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart)); //value 0.21
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}