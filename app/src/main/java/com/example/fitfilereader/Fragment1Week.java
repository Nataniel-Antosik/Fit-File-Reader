package com.example.fitfilereader;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment1Week extends Fragment {

    private static final String TAG = "Fragment 1 Week";
    private static String[] currentWeek = new String[7];
    private BarChart barChartDistanceWeek;
    private int sumMon, sumTue, sumWed, sumThu, sumFri, sumSat, sumSun;

    private static String[] testWeek = new String[]{
            "2021-11-22",
            "2021-11-23",
            "2021-11-24",
            "2021-11-25",
            "2021-11-26",
            "2021-11-27",
            "2021-11-28"
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1_week, container, false);
        barChartDistanceWeek = view.findViewById(R.id.bar_chart_distance_week);
        getCurrentWeek();
        sumDistanceWeek();
        setupDistanceGraph();
        return view;
    }

    private void setupDistanceGraph() {
        ArrayList<BarEntry> entriesPace = new ArrayList<>();
        entriesPace.add(new BarEntry(0, sumMon));
        entriesPace.add(new BarEntry(1, sumTue));
        entriesPace.add(new BarEntry(2, sumWed));
        entriesPace.add(new BarEntry(3, sumThu));
        entriesPace.add(new BarEntry(4, sumFri));
        entriesPace.add(new BarEntry(5, sumSat));
        entriesPace.add(new BarEntry(6, sumSun));
        loadBarChartDistanceWeek(entriesPace);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCurrentWeek() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < currentWeek.length; i++) {
            currentWeek[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void sumDistanceWeek() {
        FileDatabase database = FileDatabase.getDbInstance(getActivity().getApplicationContext());
        int countTraining = database.fileDao().getLastID();
        for (int i = 1; i <= countTraining; i++){
            String strDate = database.fileDao().getTrainingDate(i);
            if (strDate.equals(currentWeek[0])) {
                sumMon += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (strDate.equals(currentWeek[1])) {
                sumTue += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (strDate.equals(currentWeek[2])) {
                sumWed += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (strDate.equals(currentWeek[3])) {
                sumThu += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (strDate.equals(currentWeek[4])) {
                sumFri += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (strDate.equals(currentWeek[5])) {
                sumSat += database.fileDao().getTotalSwumDistanceFile(i);
            } else if (strDate.equals(currentWeek[6])) {
                sumSun += database.fileDao().getTotalSwumDistanceFile(i);
            }
        }
    }

    private void loadBarChartDistanceWeek(ArrayList<BarEntry> entriesPace) {

        BarDataSet barDataSet = new BarDataSet(entriesPace, "Distance Week");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");
        xAxisLabel.add("Sun");

        barChartDistanceWeek.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        XAxis xAxis = barChartDistanceWeek.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        barChartDistanceWeek.setFitBars(true);
        barChartDistanceWeek.setData(barData);
        barChartDistanceWeek.getDescription().setText("");
        barChartDistanceWeek.animateY(1000);
    }


}
