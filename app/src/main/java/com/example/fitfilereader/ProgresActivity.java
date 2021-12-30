package com.example.fitfilereader;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;
import com.garmin.fit.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgresActivity extends AppCompatActivity {

    TextView tvButterflyOldRecord, tvBackstrokeOldRecord, tvBreaststrokeOldRecord, tvFreestyleOldRecord;
    TextView tvButterflyDifferenceRecord, tvBackstrokeDifferenceRecord, tvBreaststrokeDifferenceRecord, tvFreestyleDifferenceRecord;
    TextView tvButterflyNewRecord, tvBackstrokeNewRecord, tvBreaststrokeNewRecord, tvFreestyleNewRecord;
    TextView tvButterflyOldRecordDate, tvBackstrokeOldRecordDate, tvBreaststrokeOldRecordDate, tvFreestyleOldRecordDate;
    TextView tvButterflyNewRecordDate, tvBackstrokeNewRecordDate, tvBreaststrokeNewRecordDate, tvFreestyleNewRecordDate;

    public static ArrayList<TrainingProgressModel> trainingProgressModel = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progres);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvButterflyOldRecord = findViewById(R.id.text_view_butterfly_old_record);
        tvBackstrokeOldRecord = findViewById(R.id.text_view_backstroke_old_record);
        tvBreaststrokeOldRecord = findViewById(R.id.text_view_breaststroke_old_record);
        tvFreestyleOldRecord = findViewById(R.id.text_view_freestyle_old_record);

        tvButterflyDifferenceRecord = findViewById(R.id.text_view_butterfly_difference_records);
        tvBackstrokeDifferenceRecord = findViewById(R.id.text_view_backstroke_difference_records);
        tvBreaststrokeDifferenceRecord = findViewById(R.id.text_view_breaststroke_difference_records);
        tvFreestyleDifferenceRecord = findViewById(R.id.text_view_freestyle_difference_records);

        tvButterflyNewRecord = findViewById(R.id.text_view_butterfly_new_record);
        tvBackstrokeNewRecord = findViewById(R.id.text_view_backstroke_new_record);
        tvBreaststrokeNewRecord = findViewById(R.id.text_view_breaststroke_new_record);
        tvFreestyleNewRecord = findViewById(R.id.text_view_freestyle_new_record);

        tvButterflyOldRecordDate = findViewById(R.id.text_view_butterfly_old_record_date);
        tvBackstrokeOldRecordDate = findViewById(R.id.text_view_backstroke_old_record_date);
        tvBreaststrokeOldRecordDate = findViewById(R.id.text_view_breaststroke_old_record_date);
        tvFreestyleOldRecordDate = findViewById(R.id.text_view_freestyle_old_record_date);

        tvButterflyNewRecordDate = findViewById(R.id.text_view_butterfly_new_record_date);
        tvBackstrokeNewRecordDate = findViewById(R.id.text_view_backstroke_new_record_date);
        tvBreaststrokeNewRecordDate = findViewById(R.id.text_view_breaststroke_new_record_date);
        tvFreestyleNewRecordDate = findViewById(R.id.text_view_freestyle_new_record_date);

        try {
            twoLastMaxValuePace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void twoLastMaxValuePace() throws ParseException {
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());

        int countTraining = database.fileDao().getLastID();
        if(countTraining == 0){
            Log.d("DATABASE", "Is Empty");
        } else {
            if (countTraining > 2) {
                for (int i = 1; i <= countTraining; i++){
                    List<FitFile> oneTraining = database.fileDao().getOneTrainingWithoutBreak(i);

                    trainingProgressModel.add(new TrainingProgressModel(
                            (double) database.fileDao().getMaxPaceOnButterfly(i),
                            (double) database.fileDao().getMaxPaceOnBackstroke(i),
                            (double) database.fileDao().getMaxPaceOnBreaststroke(i),
                            (double) database.fileDao().getMaxPaceOnFreestyle(i),
                            oneTraining.get(0).swimTrainingDateDb
                    ));
                }

                trainingProgressModel.sort((o1, o2) -> Double.compare(o1.getSwimBestPaceButterfly(), o2.getSwimBestPaceButterfly()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

//                for(int i = 0; i < trainingProgressModel.size(); i++) {
//                    if (dateFormat.parse(trainingProgressModel.get(i).getSwimDateProgress()).before(dateFormat.parse(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()))) {
//                        String tmp = String.format("Training: %s, Date: %s, Pace Butt: %s", i + 1, trainingProgressModel.get(i).getSwimDateProgress(), trainingProgressModel.get(i).getSwimBestPaceButterfly());
//                        Log.d("TEST", tmp);
//                    }
//                }

                if (trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceButterfly() != 0.0 ||
                        trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceButterfly() != 0.0) {
                    if(trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceButterfly() == 0.0) {
                        tvButterflyOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceButterfly())));
                        tvButterflyOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                    } else {
                        int lastPlusOne = 2;
                        for (int i = 0; i < trainingProgressModel.size(); i++){
                            if (!dateFormat.parse(trainingProgressModel.get(i).getSwimDateProgress()).before(dateFormat.parse(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()))) {
                                lastPlusOne += 1;
//                                if ((int)(100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceButterfly()) == (int) (100 / trainingProgressModel.get(i).getSwimBestPaceButterfly())) {
//                                    lastPlusOne += 1;
//                                }
                            }
                        }
                        if (trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceButterfly() == 0) {
                            tvButterflyOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceButterfly())));
                            tvButterflyOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        } else {
                            tvButterflyOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceButterfly())));
                            String strDiff = String.format("- %s", showPaceTimeSwim((int) ((100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceButterfly()) -
                                    (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceButterfly()))));
                            tvButterflyDifferenceRecord.setText(strDiff);
                            tvButterflyNewRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceButterfly())));
                            tvButterflyOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimDateProgress()));
                            tvButterflyNewRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        }
                    }
                }

                trainingProgressModel.sort((o1, o2) -> Double.compare(o1.getSwimBestPaceBackstroke(), o2.getSwimBestPaceBackstroke()));

                if (trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBackstroke() != 0.0 ||
                        trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceBackstroke() != 0.0) {
                    if(trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceBackstroke() == 0.0) {
                        tvBackstrokeOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBackstroke())));
                        tvBackstrokeOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                    } else {
                        int lastPlusOne = 2;
                        for (int i = 0; i < trainingProgressModel.size() - 1; i++){
                            if (!dateFormat.parse(trainingProgressModel.get(i).getSwimDateProgress()).before(dateFormat.parse(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()))) {
                                lastPlusOne += 1;
                            }
                        }
                        if (trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceBackstroke() == 0) {
                            tvBackstrokeOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBackstroke())));
                            tvBackstrokeOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        } else {
                            tvBackstrokeOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceBackstroke())));
                            String strDiff = String.format("- %s", showPaceTimeSwim((int) ((100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceBackstroke()) -
                                    (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBackstroke()))));
                            tvBackstrokeDifferenceRecord.setText(strDiff);
                            tvBackstrokeNewRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBackstroke())));
                            tvBackstrokeOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimDateProgress()));
                            tvBackstrokeNewRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        }
                    }
                }

                trainingProgressModel.sort((o1, o2) -> Double.compare(o1.getSwimBestPaceBreaststroke(), o2.getSwimBestPaceBreaststroke()));

                if (trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBreaststroke() != 0.0 ||
                        trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceBreaststroke() != 0.0) {
                    if(trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceBreaststroke() == 0.0) {
                        tvBreaststrokeOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBreaststroke())));
                        tvBreaststrokeOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                    } else {
                        int lastPlusOne = 2;
                        for (int i = 0; i < trainingProgressModel.size() - 1; i++){
                            if (!dateFormat.parse(trainingProgressModel.get(i).getSwimDateProgress()).before(dateFormat.parse(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()))) {
                                lastPlusOne += 1;
                            }
                        }
                        if (trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceBreaststroke() == 0) {
                            tvBreaststrokeOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBreaststroke())));
                            tvBreaststrokeOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        } else {
                            tvBreaststrokeOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceBreaststroke())));
                            String strDiff = String.format("- %s", showPaceTimeSwim((int) ((100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceBreaststroke()) -
                                    (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBreaststroke()))));
                            tvBreaststrokeDifferenceRecord.setText(strDiff);
                            tvBreaststrokeNewRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceBreaststroke())));
                            tvBreaststrokeOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimDateProgress()));
                            tvBreaststrokeNewRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        }
                    }
                }

                trainingProgressModel.sort((o1, o2) -> Double.compare(o1.getSwimBestPaceFreestyle(), o2.getSwimBestPaceFreestyle()));

                if (trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceFreestyle() != 0.0 ||
                        trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceFreestyle() != 0.0) {
                    if(trainingProgressModel.get(trainingProgressModel.size() - 2).getSwimBestPaceFreestyle() == 0.0) {
                        tvFreestyleOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceFreestyle())));
                        tvFreestyleOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                    } else {
                        int lastPlusOne = 2;
                        for (int i = 0; i < trainingProgressModel.size() - 1; i++){
                            if (!dateFormat.parse(trainingProgressModel.get(i).getSwimDateProgress()).before(dateFormat.parse(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()))) {
                                lastPlusOne += 1;
                            }
                        }
                        if (trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceFreestyle() == 0) {
                            tvFreestyleOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceFreestyle())));
                            tvFreestyleOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        } else {
                            tvFreestyleOldRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceFreestyle())));
                            String strDiff = String.format("- %s", showPaceTimeSwim((int) ((100 / trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimBestPaceFreestyle()) -
                                    (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceFreestyle()))));
                            tvFreestyleDifferenceRecord.setText(strDiff);
                            tvFreestyleNewRecord.setText(showPaceTimeSwim((int) (100 / trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimBestPaceFreestyle())));
                            tvFreestyleOldRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - lastPlusOne).getSwimDateProgress()));
                            tvFreestyleNewRecordDate.setText(correctDate(trainingProgressModel.get(trainingProgressModel.size() - 1).getSwimDateProgress()));
                        }
                    }
                }
                trainingProgressModel.clear();
            }
        }
    }

    private String showPaceTimeSwim(int seconds){
        int sec = seconds % 60;
        int min = (seconds % 3600) / 60;

        String swimTime = String.format("%02d:%02d", min, sec);
        return swimTime;
    }

    public static String correctDate(String strDate){
        String [] arr = strDate.split("-");
        String newDateFormat = "";
        newDateFormat = arr[arr.length-1] + "/" + arr[1] + "/"+ arr[0];
        return newDateFormat;
    }
}