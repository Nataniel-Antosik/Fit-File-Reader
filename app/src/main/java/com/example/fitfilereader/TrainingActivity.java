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
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        textView = findViewById(R.id.textView3);
        trainingId = getIntent().getIntExtra("ID_TRAINING", 0);
        LoadFitFileList(trainingId);
    }

    private void LoadFitFileList(int trainingId){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        List<FitFile> fitFileList = database.fileDao().getOneTraining(trainingId);

        if(fitFileList.isEmpty()){
            Log.d("DATABASE", "Is Empty");
        } else {
            String str2 = String.format(
                    "Training ID: %s\nTotal Swum Distance: %s\nTotal Swum Time: %s\nTotal Burned Kcal: %s\nTraining Date: %s",
                    trainingId,
                    database.fileDao().getTotalSwumDistanceFile(trainingId),
                    showTimeSwim(database.fileDao().getTotalSwumTimeSwum(trainingId)),
                    database.fileDao().getTotalKcalSwim(trainingId),
                    correctDate(database.fileDao().getTrainingDate(trainingId)));

            textView.setText(str2);
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
}