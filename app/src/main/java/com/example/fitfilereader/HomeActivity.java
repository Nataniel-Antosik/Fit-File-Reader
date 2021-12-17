package com.example.fitfilereader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<TrainingModel> trainingModel = new ArrayList<>();

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        setUpTrainingModels();

        T_RecyclerViewAdapter adapter = new T_RecyclerViewAdapter(this, trainingModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpTrainingModels(){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        List<FitFile> fitFileList = database.fileDao().getAllFitFileList();

        ArrayList<TrainingModel> tmp = new ArrayList<>();

        if(fitFileList.isEmpty()){
            Log.d("DATABASE", "Is Empty");
        } else {
            for (int i = 1; i <= database.fileDao().getLastID(); i++){
                String strDistance = String.format("%s m", database.fileDao().getTotalSwumDistanceFile(i));
                tmp.add(new TrainingModel(
                        database.fileDao().getTrainingDate(i),
                        String.valueOf(i),
                        strDistance,
                        i));
            }

            List<TrainingModel> sortedList = tmp.stream()
                    .sorted(Comparator.comparing(TrainingModel::getTrainingData)
                    .reversed())
                    .collect(Collectors.toList());

            for(int i = 0; i < sortedList.size(); i++){
                trainingModel.add(new TrainingModel(
                        correctDate(sortedList.get(i).getTrainingData()),
                        sortedList.get(i).getTrainingPace(),
                        sortedList.get(i).getTrainingDistance(),
                        sortedList.get(i).getId()));
            }

        }
    }

    public static String correctDate(String strDate){
        String [] arr = strDate.split("-");
        String newDateFormat = "";
        newDateFormat = arr[arr.length-1] + "/" + arr[1] + "/"+ arr[0];
        return newDateFormat;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
    int id = item.getItemId();

    if(id == R.id.nav_files){
        Intent intent = new Intent(this, FilesActivity.class);
        startActivity(intent);
    } else if(id == R.id.nav_progres){
        Intent intent = new Intent(this, ProgresActivity.class);
        startActivity(intent);
    } else if(id == R.id.nav_general_statistic){
        Intent intent = new Intent(this, GeneralStatisticActivity.class);
        startActivity(intent);
    }

    return true;
    }

}