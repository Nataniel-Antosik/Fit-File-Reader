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

public class Fragment3Years extends Fragment {
    public static final String TAG = "Fragment 3 Years";
    public static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int yearOne, yearTwo, yearThree, yearFour, yearFive, yearSix;
    private BarChart barChartDistanceYears;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3_years, container, false);
        barChartDistanceYears = view.findViewById(R.id.bar_chart_distance_years);
        setupDistanceGraph();
        return view;
    }

    private void setupDistanceGraph() {
        ArrayList<BarEntry> entriesPace = new ArrayList<>();
        sumDistanceYears();
        entriesPace.add(new BarEntry(5, yearOne));
        entriesPace.add(new BarEntry(4, yearTwo));
        entriesPace.add(new BarEntry(3, yearThree));
        entriesPace.add(new BarEntry(2, yearFour));
        entriesPace.add(new BarEntry(1, yearFive));
        entriesPace.add(new BarEntry(0, yearSix));
        loadBarChartDistanceWeek(entriesPace);
    }

    private void loadBarChartDistanceWeek(ArrayList<BarEntry> entriesPace) {
        String str1 = String.valueOf(currentYear - 5);
        String str2 = String.valueOf(currentYear - 4);
        String str3 = String.valueOf(currentYear - 3);
        String str4 = String.valueOf(currentYear - 2);
        String str5 = String.valueOf(currentYear - 1);
        String str6 = String.valueOf(currentYear);
        final String[] yearsLabels = new String[] {str1, str2, str3, str4, str5, str6};

        BarDataSet barDataSet = new BarDataSet(entriesPace, "Distance Years");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        for (int i = 0; i < yearsLabels.length; i++) {
            Log.d(TAG, yearsLabels[i]);
            xAxisLabel.add(yearsLabels[i]);
        }

        barChartDistanceYears.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        XAxis xAxis = barChartDistanceYears.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        barChartDistanceYears.setFitBars(true);
        barChartDistanceYears.setData(barData);
        barChartDistanceYears.getDescription().setText("");
        barChartDistanceYears.animateY(1000);
    }

    public static int splitDateYear(String date){
        String [] arr = date.split("-");
        int month = Integer.parseInt(arr[0]);
        return month;
    }

    public void sumDistanceYears() {
        FileDatabase database = FileDatabase.getDbInstance(getActivity().getApplicationContext());
        int countTraining = database.fileDao().getLastID();
        for (int i = 1; i <= countTraining; i++){
            String trainingDate = database.fileDao().getTrainingDate(i);
            if (splitDateYear(trainingDate) == currentYear) {
                yearOne += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (splitDateYear(trainingDate) == currentYear - 1) {
                yearTwo += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (splitDateYear(trainingDate) == currentYear - 2) {
                yearThree += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (splitDateYear(trainingDate) == currentYear - 3) {
                yearFour += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (splitDateYear(trainingDate) == currentYear - 4) {
                yearFive += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (splitDateYear(trainingDate) == currentYear - 5) {
                yearSix += database.fileDao().getTotalSwumDistanceFile(i);
            }
        }
    }


}
