package com.example.fitfilereader;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment2Months extends Fragment {

    public static final String TAG = "Fragment 2 Months";
    public static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    private BarChart barChartDistanceMonths;
    int sumJan, sumFeb, sumMarch, sumApril, sumMay, sumJune, sumJuly, sumAug, sumSept, sumOct, sumNov, sumDec;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2_months, container, false);
        barChartDistanceMonths = view.findViewById(R.id.bar_chart_distance_months);
        setupDistanceGraph();
        return view;
    }

    private void setupDistanceGraph() {
        ArrayList<BarEntry> entriesPace = new ArrayList<>();
        sumDistanceMonths();
        entriesPace.add(new BarEntry(0, sumJan));
        entriesPace.add(new BarEntry(1, sumFeb));
        entriesPace.add(new BarEntry(2, sumMarch));
        entriesPace.add(new BarEntry(3, sumApril));
        entriesPace.add(new BarEntry(4, sumMay));
        entriesPace.add(new BarEntry(5, sumJune));
        entriesPace.add(new BarEntry(6, sumJuly));
        entriesPace.add(new BarEntry(7, sumAug));
        entriesPace.add(new BarEntry(8, sumSept));
        entriesPace.add(new BarEntry(9, sumOct));
        entriesPace.add(new BarEntry(10, sumNov));
        entriesPace.add(new BarEntry(11, sumDec));
        loadBarChartDistanceWeek(entriesPace);
    }

    private void loadBarChartDistanceWeek(ArrayList<BarEntry> entriesPace) {
        final String[] monthsLabels = new String[] {
                "Jan", "Feb", "March", "April", "May",
                "June", "July", "Aug", "Sept", "Oct",
                "Nov", "Dec"};

        BarDataSet barDataSet = new BarDataSet(entriesPace, "Distance Months");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        for (int i = 0; i < monthsLabels.length; i++) {
            Log.d(TAG, monthsLabels[i]);
            xAxisLabel.add(monthsLabels[i]);
        }

        barChartDistanceMonths.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        XAxis xAxis = barChartDistanceMonths.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        barChartDistanceMonths.setFitBars(true);
        barChartDistanceMonths.setData(barData);
        barChartDistanceMonths.getDescription().setText("");
        barChartDistanceMonths.animateY(1000);
    }

    public static int splitDateYear(String date){
        String [] arr = date.split("-");
        int month = Integer.parseInt(arr[0]);
        return month;
    }

    public static int splitDateMonth(String date){
        String [] arr = date.split("-");
        int year = Integer.parseInt(arr[1]);
        return year;
    }

    public void sumDistanceMonths() {
        FileDatabase database = FileDatabase.getDbInstance(getActivity().getApplicationContext());
        int countTraining = database.fileDao().getLastID();
        for (int i = 1; i <= countTraining; i++){
            String trainingDate = database.fileDao().getTrainingDate(i);
            if (splitDateYear(trainingDate) == currentYear) {
                if(splitDateMonth(trainingDate) == 1){
                    sumJan += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 2){
                    sumFeb += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 3){
                    sumMarch += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 4){
                    sumApril += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 5){
                    sumMay += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 6){
                    sumJune += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 7){
                    sumJuly += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 8){
                    sumAug += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 9){
                    sumSept += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 10){
                    sumOct += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 11){
                    sumNov += database.fileDao().getTotalSwumDistanceFile(i);
                } else if(splitDateMonth(trainingDate) == 12){
                    sumDec += database.fileDao().getTotalSwumDistanceFile(i);
                }
            }
        }
    }
}


