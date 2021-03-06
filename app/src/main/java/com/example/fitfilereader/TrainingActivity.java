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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
    private static int countPieChartColors;

    TextView tvt_date, tvt_calories, tvt_distance, tvt_avg_rate, tvt_max_rate, tvt_moving_time, tvt_elapsed_time, tvt_avg_pace, tvt_best_pace, tvt_avg_cadence;
    TextView tvt_pace_on_butterfly, tvt_pace_on_backstroke, tvt_pace_on_breaststroke, tvt_pace_on_freestyle;

    private PieChart pieChart;
    private BarChart barChartHeartRate, barChartAvgSpeed, barChartAvgCadence;

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
        barChartHeartRate = findViewById(R.id.training_activity_bar_chart_heart_rate);
        barChartAvgSpeed = findViewById(R.id.training_activity_bar_chart_avg_speed_m_s);
        barChartAvgCadence = findViewById(R.id.training_activity_bar_chart_avg_cadence);

        trainingId = getIntent().getIntExtra("ID_TRAINING", 0);
        LoadTrainingData(trainingId);

        setUpPieChart();
        loadPieChartData();
        loadBarChartHeartRate(trainingId);
        loadBarChartAvgSpeed();
        loadBarChartAvgCadence();

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

    public static int maxHeartRate(int age) { return (220 - age); }

    public static void typeOfTraining(int age, int heartRate){
        int MHRIW = maxHeartRateInWater(age);
        if(heartRate < (int) (MHRIW * 0.60)) {
            warmUp += 1;
            String tmp = String.format("%s,  (inf,%s)", heartRate, ((int) (MHRIW * 0.60)));
            Log.d("warmUp", tmp);
        } else if (heartRate >= ((int) (MHRIW * 0.60)) && heartRate <= ((int) (MHRIW * 0.65))) {
            activeRegenerationZone += 1;
            String tmp = String.format("%s,  <%s,%s>", heartRate, ((int) (MHRIW * 0.60)), ((int) (MHRIW * 0.65)));
            Log.d("activeRegenerationZone", tmp);
        } else if (heartRate >= ((int) (MHRIW * 0.66)) && heartRate <= ((int) (MHRIW * 0.72))) {
            enduranceTraining += 1;
            String tmp = String.format("%s,  <%s,%s>", heartRate, ((int) (MHRIW * 0.66)), ((int) (MHRIW * 0.72)));
            Log.d("enduranceTraining", tmp);
        } else if (heartRate >= ((int) (MHRIW * 0.73)) && heartRate <= ((int) (MHRIW * 0.83))) {
            improvedCardiovascularPerformance += 1;
            String tmp = String.format("%s,  <%s,%s>", heartRate, ((int) (MHRIW * 0.73)), ((int) (MHRIW * 0.83)));
            Log.d("impCardiovrPerforma", tmp);
        } else if (heartRate >= ((int) (MHRIW * 0.84)) && heartRate <= ((int) (MHRIW * 0.90))) {
            lactateThreshold += 1;
            String tmp = String.format("%s,  <%s,%s>", heartRate, ((int) (MHRIW * 0.84)), ((int) (MHRIW * 0.90)));
            Log.d("lactateThreshold", tmp);
        } else if (heartRate >= ((int) (MHRIW * 0.91))) {
            VO2max += 1;
            String tmp = String.format("%s,  <%s,inf>", heartRate, ((int) (MHRIW * 0.91)));
            Log.d("VO2max", tmp);
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
        ArrayList<Integer> colors = new ArrayList<>();

        if (warmUp != 0) {
            entries.add(new PieEntry(warmUp, "Warm-Up"));
            colors.add(Color.BLUE);
        }
        if (activeRegenerationZone != 0) {
            entries.add(new PieEntry(activeRegenerationZone, "Active Regeneration Zone"));
            colors.add(Color.CYAN);
        }
        if (enduranceTraining != 0) {
            entries.add(new PieEntry(enduranceTraining, "Endurance Training"));
            colors.add(Color.MAGENTA);
        }
        if (improvedCardiovascularPerformance != 0) {
            entries.add(new PieEntry(improvedCardiovascularPerformance, "Improved Cardiovascular Performance"));
            colors.add(Color.GREEN);
        }
        if (lactateThreshold != 0) {
            entries.add(new PieEntry(lactateThreshold, "Lactate Threshold"));
            colors.add(Color.YELLOW);
        }
        if (VO2max != 0) {
            entries.add(new PieEntry(VO2max, "VO2max"));
            colors.add(Color.RED);
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

    private void loadBarChartHeartRate(int trainingId) {
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        UserDatabase userDatabase = UserDatabase.getDbInstance(this.getApplicationContext());
        String age = splitDateAndGetAge(userDatabase.userDao().getUserBirthdayDate(), correctDate(database.fileDao().getTrainingDate(trainingId)));

        int MHRIW = maxHeartRateInWater(Integer.parseInt(age));

        int countColor = 0;
        
        ArrayList<Integer> colors = new ArrayList<>();

        List<FitFile> fitFileList = database.fileDao().getOneTraining(trainingId);

        ArrayList<BarEntry> entriesPace = new ArrayList<>();
        Log.d("+++++++++++++++", "+++++++++++++++");

        for (int i = 0; i < fitFileList.size(); i++){

            if (fitFileList.get(i).avgHeartRateDb < (int) (MHRIW * 0.60)) {
                colors.add(Color.BLUE);
                String tmp = String.format("%s,  (inf,%s)", fitFileList.get(i).avgHeartRateDb, ((int) (MHRIW * 0.60)));
                Log.d("warmUp", tmp);
                entriesPace.add(new BarEntry(i, fitFileList.get(i).avgHeartRateDb));
            }

            if(fitFileList.get(i).avgHeartRateDb >= ((int)(MHRIW*0.60)) && fitFileList.get(i).avgHeartRateDb <= ((int)(MHRIW*0.65))){
                colors.add(Color.CYAN);
                String tmp = String.format("%s,  <%s,%s>", fitFileList.get(i).avgHeartRateDb, ((int) (MHRIW * 0.60)), ((int) (MHRIW * 0.65)));
                Log.d("activeRegenerationZone", tmp);
                entriesPace.add(new BarEntry(i, fitFileList.get(i).avgHeartRateDb));
            }

            if (fitFileList.get(i).avgHeartRateDb >= ((int) (MHRIW * 0.66)) && fitFileList.get(i).avgHeartRateDb <= ((int) (MHRIW * 0.72))) {
                colors.add(Color.MAGENTA);
                String tmp = String.format("%s,  <%s,%s>", fitFileList.get(i).avgHeartRateDb, ((int) (MHRIW * 0.66)), ((int) (MHRIW * 0.72)));
                Log.d("enduranceTraining", tmp);
                entriesPace.add(new BarEntry(i, fitFileList.get(i).avgHeartRateDb));
            }

            if (fitFileList.get(i).avgHeartRateDb >= ((int) (MHRIW * 0.73)) && fitFileList.get(i).avgHeartRateDb <= ((int) (MHRIW * 0.83))) {
                colors.add(Color.GREEN);
                String tmp = String.format("%s,  <%s,%s>", fitFileList.get(i).avgHeartRateDb, ((int) (MHRIW * 0.73)), ((int) (MHRIW * 0.83)));
                Log.d("impCardiovrPerforma", tmp);
                entriesPace.add(new BarEntry(i, fitFileList.get(i).avgHeartRateDb));
            }

            if (fitFileList.get(i).avgHeartRateDb >= ((int) (MHRIW * 0.84)) && fitFileList.get(i).avgHeartRateDb <= ((int) (MHRIW * 0.90))) {
                colors.add(Color.YELLOW);
                String tmp = String.format("%s,  <%s,%s>", fitFileList.get(i).avgHeartRateDb, ((int) (MHRIW * 0.84)), ((int) (MHRIW * 0.90)));
                Log.d("lactateThreshold", tmp);
                entriesPace.add(new BarEntry(i, fitFileList.get(i).avgHeartRateDb));
            }

            if (fitFileList.get(i).avgHeartRateDb >= ((int) (MHRIW * 0.91))) {
                colors.add(Color.RED);
                String tmp = String.format("%s,  <%s,inf>", fitFileList.get(i).avgHeartRateDb, ((int) (MHRIW * 0.91)));
                Log.d("VO2max", tmp);
                entriesPace.add(new BarEntry(i, fitFileList.get(i).avgHeartRateDb));
            }
        }

        Log.d("===", "===");
        Log.d("fitFile size", String.valueOf(fitFileList.size()));
        Log.d("Count color", String.valueOf(colors.size()));

        BarDataSet barDataSet = new BarDataSet(entriesPace, "Heart Rate");
        barDataSet.setColors(colors);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(0);

        BarData barData = new BarData(barDataSet);

        barChartHeartRate.setFitBars(true);
        barChartHeartRate.setData(barData);
        barChartHeartRate.getDescription().setText("Bar Chart Heart Rate");
        barChartHeartRate.animateY(2000);

        YAxis yAxis = barChartHeartRate.getAxisLeft();
        yAxis.setLabelCount(entriesPace.size());
        yAxis.setGranularity(1f);

        if (warmUp != 0){
            countColor += 1;
        }
        if (activeRegenerationZone != 0){
            countColor += 1;
        }
        if (enduranceTraining != 0) {
            countColor += 1;
        }
        if (improvedCardiovascularPerformance != 0){
            countColor += 1;
        }
        if (lactateThreshold != 0){
            countColor += 1;
        }
        if (VO2max != 0){
            countColor += 1;
        }

        Legend l = barChartHeartRate.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setWordWrapEnabled(true);
        l.setMaxSizePercent(0.20f);
        l.setDrawInside(false);
        l.setEnabled(true);

        LegendEntry[] legendEntry = new LegendEntry[countColor];

        int iterator = 0;
        if (warmUp != 0) {
            LegendEntry entry = new LegendEntry();
            entry.label = "Warm-Up";
            entry.formColor = Color.BLUE;
            legendEntry[iterator] = entry;
            iterator += 1;
        }
        if (activeRegenerationZone != 0) {
            LegendEntry entry = new LegendEntry();
            entry.label = "Active Regeneration Zone";
            entry.formColor = Color.CYAN;
            legendEntry[iterator] = entry;
            iterator += 1;
        }
        if (enduranceTraining != 0) {
            LegendEntry entry = new LegendEntry();
            entry.label = "Endurance Training";
            entry.formColor = Color.MAGENTA;
            legendEntry[iterator] = entry;
            iterator += 1;
        }
        if (improvedCardiovascularPerformance != 0) {
            LegendEntry entry = new LegendEntry();
            entry.label = "Improved Cardiovascular Performance";
            entry.formColor = Color.GREEN;
            legendEntry[iterator] = entry;
            iterator += 1;
        }
        if (lactateThreshold != 0) {
            LegendEntry entry = new LegendEntry();
            entry.label = "Lactate Threshold";
            entry.formColor = Color.YELLOW;
            legendEntry[iterator] = entry;
            iterator += 1;
        }
        if (VO2max != 0) {
            LegendEntry entry = new LegendEntry();
            entry.label = "VO2max";
            entry.formColor = Color.RED;
            legendEntry[iterator] = entry;
            iterator += 1;
        }

        l.setCustom(legendEntry);

    }

    private void loadBarChartAvgSpeed() {
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());

        List<FitFile> fitFileList = database.fileDao().getOneTrainingWithoutBreak(trainingId);

        ArrayList<BarEntry> entriesPace = new ArrayList<>();

        for (int i = 0; i < fitFileList.size(); i++) {
            entriesPace.add(new BarEntry(i, fitFileList.get(i).avarageSpeedDb));
        }

        BarDataSet barDataSet = new BarDataSet(entriesPace, "Average Speed");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);

        BarData barData = new BarData(barDataSet);

        barChartAvgSpeed.setFitBars(true);
        barChartAvgSpeed.setData(barData);
        barChartAvgSpeed.getDescription().setText("Bar Chart Average Speed");
        barChartAvgSpeed.animateY(2000);
    }

    public void loadBarChartAvgCadence() {
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());

        List<FitFile> fitFileList = database.fileDao().getOneTrainingWithoutBreak(trainingId);

        ArrayList<BarEntry> entriesPace = new ArrayList<>();

        for (int i = 0; i < fitFileList.size(); i++) {
            entriesPace.add(new BarEntry(i, fitFileList.get(i).avarageCadenceDb));
        }

        BarDataSet barDataSet = new BarDataSet(entriesPace, "Average Cadence");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);

        BarData barData = new BarData(barDataSet);

        barChartAvgCadence.setFitBars(true);
        barChartAvgCadence.setData(barData);
        barChartAvgCadence.getDescription().setText("Bar Chart Average Cadence");
        barChartAvgCadence.animateY(2000);
    }
}