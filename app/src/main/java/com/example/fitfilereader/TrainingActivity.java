package com.example.fitfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;

import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    int trainingId;
    TextView tvt_date, tvt_calories, tvt_distance, tvt_avg_rate, tvt_max_rate, tvt_moving_time, tvt_elapsed_time, tvt_avg_pace, tvt_best_pace, tvt_avg_cadence;
    TextView tvt_pace_on_butterfly, tvt_pace_on_backstroke, tvt_pace_on_breaststroke, tvt_pace_on_freestyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

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

        trainingId = getIntent().getIntExtra("ID_TRAINING", 0);
        LoadTrainingData(trainingId);
    }

    private void LoadTrainingData(int trainingId){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        List<FitFile> fitFileList = database.fileDao().getOneTraining(trainingId);

        if(fitFileList.isEmpty()){
            Log.d("DATABASE", "Is Empty");
        } else {

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
}