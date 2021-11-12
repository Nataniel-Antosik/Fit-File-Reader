package com.example.fitfilereader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.garmin.fit.*;
import com.garmin.fit.examples.DecodeExample;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test";
    private static final String folderName = "/Fit File Storage/";

    public static int swimTableSize = 200;
    public static int dataCount = 0;
    public static int userAge;

    ArrayList<String> fileArray;
    ListView listView;

    public static float userWeight;
    public static float userHeight;

    public static String userGender;

    /* Tabels for swim data */
    public static String [] swimStorke = new String [swimTableSize];
    public static int [] temperature = new int [swimTableSize];
    public static int [] activeLengthsSwimPool = new int [swimTableSize];
    public static int [] kcalSwim = new int [swimTableSize];
    public static float [] elapsedTimeSwimming = new float [swimTableSize];
    public static int [] maxHeartRate = new int [swimTableSize];
    public static int [] avgHeartRate = new int [swimTableSize];
    public static float [] avarageSpeed = new float [swimTableSize];
    public static int [] avarageCadence = new int [swimTableSize];
    public static float [] totalSwimDistance = new float [swimTableSize];

    /* Summery data from swim data */
    public static int distanceSwum;
    public static int allBurnKcal;
    public static float allTimeSwum;

    InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.d(TAG, "Test 1");
        listView = findViewById(R.id.fileList);
        fileArray = new ArrayList<String>();
        inputStream = null;
        System.out.println("TEST");
        checkPermission();
        loadFiles();
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
            }
        }
        if(Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    boolean readPer = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (readPer) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Permision Denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void takeData(View view) throws IOException {
        // Update list of fit files
        loadFiles();

        if (fileArray.size() == 0){
            Toast.makeText(getApplicationContext(), "Directory is empty", Toast.LENGTH_SHORT).show();
        } else {

            for (int i = 0; i < fileArray.size(); i++){
                String str = String.format("%s%s%s", Environment.getExternalStorageDirectory(), folderName, fileArray.get(i));
                decodeFitFiles(str);

            }

        }
    }

    public void createDir(View view){
        createFolder();
    }

    private void createFolder() {
        File file = new File(Environment.getExternalStorageDirectory(), folderName);
        if (file.exists()){
            Toast.makeText(getApplicationContext(),
                    "Directory is already exist",
                    Toast.LENGTH_SHORT).show();
        } else {
            file.mkdirs();
            if (file.isDirectory()){
                Toast.makeText(getApplicationContext(),
                        "Directory is created successfully.",
                        Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String sMessage = "Message : failed to create directory" +
                        "\nPath :" + Environment.getExternalStorageDirectory() +
                        "\nmkdirs :" + file.mkdirs();
                builder.setMessage(sMessage);
                builder.show();
            }
        }
    }

    private void loadFiles(){
        File dir = new File(Environment.getExternalStorageDirectory(), folderName);
        if (dir.exists()) {
            fileArray.clear();
            for (File f : dir.listFiles()) {
                if (f.isFile())
                    if (f.getName().substring(f.getName().lastIndexOf(".")).equals(".fit")){
                        fileArray.add(f.getName());
                    }
            }

            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, fileArray);
            listView.setAdapter(adapter);
        }
    }

    private void decodeFitFiles(String fileName) throws IOException {
        File fitFile = new File(fileName);

        inputStream = new FileInputStream(fitFile);

        Decode decode = new Decode();

        MesgBroadcaster mesgBroadcaster = new MesgBroadcaster(decode);
        Listener listener = new Listener();

        mesgBroadcaster.addListener((UserProfileMesgListener)listener);
        mesgBroadcaster.addListener((LapMesgListener)listener);

        try {
            decode.read(inputStream, mesgBroadcaster, mesgBroadcaster);
        } catch (FitRuntimeException e) {
            // If a file with 0 data size in it's header  has been encountered,
            // attempt to keep processing the file
            if (decode.getInvalidFileDataSize()) {
                decode.nextFile();
                decode.read(inputStream, mesgBroadcaster, mesgBroadcaster);
            } else {
                System.err.print("Exception decoding file: ");
                System.err.println(e.getMessage());

                return;
            }
        }
        inputStream.close();
        printData();
        fitFile.delete();
    }

    private void printData(){
        String [] tmpData = new String [swimTableSize];
        for (int i = 0; i < dataCount; i++){
            tmpData[i] = String.format(swimStorke[i] +
                            ", " + temperature[i] +
                            ", " + activeLengthsSwimPool[i] +
                            ", " + totalSwimDistance[i] +
                            ", " + kcalSwim[i] +
                            ", " + elapsedTimeSwimming[i] +
                            ", " + maxHeartRate[i] +
                            ", " + avgHeartRate[i] +
                            ", " + avarageSpeed[i] +
                            ", " + avarageCadence[i]
                    );
            distanceSwum += totalSwimDistance[i];
            allBurnKcal += kcalSwim[i];
            allTimeSwum += elapsedTimeSwimming[i];

        }
        allTimeSwum = allTimeSwum / 60;
        for (int i = 0; i < dataCount; i++){ Log.d("DATA", tmpData[i]); }
        Log.d("SUMMARY DATA", "\nAll swum distance : " + distanceSwum + " [m] " + "\nAll burned kcal in training: " + allBurnKcal + " [kcal]" + "\nTime: " + allTimeSwum + " [min]");
        String tmp = String.format("Age: %s, Gender: %s, Height: %s, Weight: %s", userAge, userGender, userHeight, userWeight);
        Log.d("DATA", tmp);
        Log.d("DATA COUNT", String.valueOf(dataCount));
        dataCount = 0;
        distanceSwum = 0;
        allBurnKcal = 0;
        allTimeSwum = 0;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public static class Listener implements LapMesgListener, UserProfileMesgListener {


        @Override
        public void onMesg(UserProfileMesg mesg) {

            if (mesg.getGender() != null) {
                if (mesg.getGender() == Gender.MALE) {
                    userGender = "Male";
                } else if (mesg.getGender() == Gender.FEMALE) {
                    userGender = "Female";
                }
            }

            if (mesg.getAge() != null) {
                userAge = mesg.getAge();
            }

            if (mesg.getWeight() != null) {
                userWeight = mesg.getWeight();
            }

            if (mesg.getHeight() != null){
                userHeight = mesg.getHeight();
            }
        }

        @Override
        public void onMesg(LapMesg mesg) {
            if (mesg.getSubSport() != null){
                if (mesg.getSubSport().toString().equals("LAP_SWIMMING")){
                    dataCount = dataCount + 1;
                    //System.out.println("Swim: ");
                    if (mesg.getSwimStroke() == null) {
                        //System.out.print("   Swim Stroke: ");
                        //System.out.println("BREAK");
                        swimStorke[dataCount - 1] = "BREAK";
                    } else {
                        //System.out.print("   Swim Stroke: ");
                        //System.out.println(mesg.getSwimStroke());
                        swimStorke[dataCount - 1] = mesg.getSwimStroke().toString();
                    }
                    if (mesg.getMaxSpeed() != null) {
                        //System.out.print("   Max Speed: ");
                        //System.out.println(mesg.getMaxSpeed() + " [m/s]");
                    }
                    if (mesg.getMaxTemperature() != null) {
                        //System.out.print("   Max Temperature: ");
                        //System.out.println(mesg.getMaxTemperature() + " [C]");
                        temperature[dataCount - 1] = mesg.getMaxTemperature();
                    }
                    if (mesg.getNumActiveLengths() != null){ //number of completed lengths of pools | mesg.getNumActiveLengths() * 25 m = x
                        //System.out.print("   Active lengths of swim pool: ");
                        //System.out.println(mesg.getNumActiveLengths() + " [m]");
                        activeLengthsSwimPool[dataCount - 1] = mesg.getNumActiveLengths();
                    }
                    if (mesg.getTotalDistance() != null){
                        //System.out.print("   Total Distance: ");
                        //System.out.println(mesg.getTotalDistance() + " [m]");
                        totalSwimDistance[dataCount - 1] = mesg.getTotalDistance();
                    }
                    if (mesg.getNumLengths() != null){
                        //System.out.print("   Lengths of swim pool: ");
                        //System.out.println(mesg.getNumLengths() + " [m]");
                    }
                    if (mesg.getTotalCalories() != null){
                        //System.out.print("   Total Calories: ");
                        //System.out.println(mesg.getTotalCalories() + " [kcal]");
                        kcalSwim[dataCount - 1] = mesg.getTotalCalories();
                    }
                    if (mesg.getStartTime() != null){
                        //System.out.print("   Start Time: ");
                        //System.out.println(mesg.getStartTime());
                    }
                    if (mesg.getTimestamp() != null){
                        //System.out.print("   End Time: ");
                        //System.out.println(mesg.getTimestamp());
                    }
                    if (mesg.getTotalElapsedTime() != null){
                        //System.out.print("   Total Elapsed Time: ");
                        //System.out.println(mesg.getTotalElapsedTime() + " [s]");
                        elapsedTimeSwimming[dataCount - 1] = mesg.getTotalElapsedTime();
                    }
                    if (mesg.getAvgSpeed() != null){
                        //System.out.print("   Avarage Speed: ");
                        //System.out.println(mesg.getAvgSpeed() + " [m/s]");
                    }
                    if (mesg.getAvgNegVerticalSpeed() != null){
                        //System.out.print("   Avg Neg Vertical Speed: ");
                        //System.out.println(mesg.getAvgNegVerticalSpeed() + " [m/s]");
                    }
                    if (mesg.getAvgPosVerticalSpeed() != null){
                        //System.out.print("   Avg Pos Vertical Speed: ");
                        //System.out.println(mesg.getAvgPosVerticalSpeed() + " [m/s]");
                    }
                    if (mesg.getNumActiveLengths() != null && mesg.getTotalElapsedTime() != null){
                        //System.out.print("   My Avarage Speed: ");
                        //System.out.println(mesg.getTotalDistance() / mesg.getTotalElapsedTime() + " [m/s]");
                        avarageSpeed[dataCount - 1] = mesg.getTotalDistance() / mesg.getTotalElapsedTime();
                    }
                    if (mesg.getMaxHeartRate() != null){
                        //System.out.print("   Max Heart Rate: ");
                        //System.out.println(mesg.getMaxHeartRate() + " [bpm]");
                        maxHeartRate[dataCount - 1] = mesg.getMaxHeartRate();
                    }
                    if (mesg.getMinHeartRate() != null){
                        //System.out.print("   Min Heart Rate: ");
                        //System.out.println(mesg.getMinHeartRate() + " [bpm]");
                    }
                    if (mesg.getAvgHeartRate() != null){
                        //System.out.print("   Avg Heart Rate: ");
                       // System.out.println(mesg.getAvgHeartRate() + " [bpm]");
                        avgHeartRate[dataCount - 1] = mesg.getAvgHeartRate();
                    }
                    if (mesg.getMaxHeartRate() != null && mesg.getMinHeartRate() != null){
                        float avgMaxHeartRate = ((mesg.getMaxHeartRate() + mesg.getMinHeartRate()) / 2);
                        //System.out.print("   My Avg Heart Rate: ");
                        //System.out.println(avgMaxHeartRate + " [bpm]");
                    }
                    if (mesg.getAvgCadence() != null){
                        //System.out.print("   Avarage Cadence: ");
                        //System.out.println(mesg.getAvgCadence());
                        avarageCadence[dataCount - 1] = mesg.getAvgCadence();
                    }
                    if (mesg.getStrokeCount() != null){
                        //System.out.print("   Stroke Count: ");
                        //System.out.println(mesg.getStrokeCount());
                    }
                    if (mesg.getAvgStrokeDistance() != null){
                        //System.out.print("   Avarage Stroke Distance: ");
                        //System.out.println(mesg.getAvgStrokeDistance() + " [m]");
                    }
                    //printValues(mesg, LapMesg.SwimStrokeFieldNum);
                }
            }
        }
    }
}


