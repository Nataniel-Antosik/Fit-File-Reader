package com.example.fitfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;

import java.util.List;

public class TableActivity extends AppCompatActivity {

    int trainingId;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        txt = findViewById(R.id.textView2);

        trainingId = getIntent().getIntExtra("ID_TRAINING", 0);
        loadTrainingDataFromDatabase(trainingId);
    }

    private void loadTrainingDataFromDatabase(int trainingId){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        List<FitFile> fitFileList = database.fileDao().getOneTraining(trainingId);


    }
}