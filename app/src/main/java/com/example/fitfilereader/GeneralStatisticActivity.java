package com.example.fitfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;

import java.util.List;

public class GeneralStatisticActivity extends AppCompatActivity {

    TextView gsBestPaceButterfly, gsBestPaceBackstroke, gsBestPaceBreaststroke, gsBestPaceFreestyle, gsBurnKcal, gsSwumDistance, gsAllTrainings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_statistic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gsBestPaceButterfly = findViewById(R.id.text_view_training_butterfly_best_data);
        gsBestPaceBackstroke = findViewById(R.id.text_view_training_backstroke_best_data);
        gsBestPaceBreaststroke = findViewById(R.id.text_view_training_breaststroke_best_data);
        gsBestPaceFreestyle = findViewById(R.id.text_view_training_freestyle_best_data);
        gsBurnKcal = findViewById(R.id.all_burn_kcal_data);
        gsSwumDistance = findViewById(R.id.all_swum_distance_data);
        gsAllTrainings = findViewById(R.id.all_trainings_data);

        loadGeneralData();
    }

    private void loadGeneralData(){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        List<FitFile> fitFileList = database.fileDao().getAllFitFileList();

        if (fitFileList.isEmpty()){
            Log.d("DATABASE", "Is Empty");
        } else {
            String strPaceButterfly = String.format("%s/100 m", showPaceTimeSwim((int) (100 / database.fileDao().getBestPaceButterfly())));
            String strPaceBackstroke = String.format("%s/100 m", showPaceTimeSwim((int) (100 / database.fileDao().getBestPaceBackstroke())));
            String strPaceBreaststroke = String.format("%s/100 m", showPaceTimeSwim((int) (100 / database.fileDao().getBestPaceBreaststroke())));
            String strPaceFreestyle = String.format("%s/100 m", showPaceTimeSwim((int) (100 / database.fileDao().getBestPaceFreestyle())));
            String strBurnKcal = String.format("%s kcal", database.fileDao().getAllBurnedKcal());
            String strSwumDistance = String.format("%s m", database.fileDao().getAllSwumDistance());

            gsBestPaceButterfly.setText(strPaceButterfly);
            gsBestPaceBackstroke.setText(strPaceBackstroke);
            gsBestPaceBreaststroke.setText(strPaceBreaststroke);
            gsBestPaceFreestyle.setText(strPaceFreestyle);
            gsBurnKcal.setText(strBurnKcal);
            gsSwumDistance.setText(strSwumDistance);
            gsAllTrainings.setText(String.valueOf(database.fileDao().getLastID()));
        }
    }

    private String showPaceTimeSwim(int seconds){
        int sec = seconds % 60;
        int min = (seconds % 3600) / 60;

        String swimTime = String.format("%02d:%02d", min, sec);
        return swimTime;
    }

    public void runDistanceGraphActivity(View view) {
        Intent intent = new Intent(this, DistanceGraphActivity.class);
        startActivity(intent);
    }

}