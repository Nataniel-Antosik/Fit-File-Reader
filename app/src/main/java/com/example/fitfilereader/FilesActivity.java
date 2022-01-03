package com.example.fitfilereader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fitfilereader.db.FileDatabase;
import com.example.fitfilereader.db.FitFile;
import com.example.fitfilereader.db.ShortcutDatabase;
import com.example.fitfilereader.db.ShortcutFile;
import com.example.fitfilereader.db.UserDao;
import com.example.fitfilereader.db.UserData;
import com.example.fitfilereader.db.UserDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.garmin.fit.*;

public class FilesActivity extends AppCompatActivity {

    private static final String TAG = "Test";
    private static final String folderName = "/Fit File Storage/";

    public static int swimTableSize = 200;
    public static int dataCount = 0;

    ArrayList<String> fileArray;
    ListView listView;

    /* Tabels for swim data */
    public static String [] swimStorke = new String [swimTableSize];
    public static int [] activeLengthsSwimPool = new int [swimTableSize];
    public static int [] kcalSwim = new int [swimTableSize];
    public static DateTime [] swimTrainingDate = new DateTime [swimTableSize];
    public static float [] elapsedTimeSwimming = new float [swimTableSize];
    public static int [] maxHeartRate = new int [swimTableSize];
    public static int [] avgHeartRate = new int [swimTableSize];
    public static float [] avarageSpeed = new float [swimTableSize];
    public static int [] avarageCadence = new int [swimTableSize];
    public static float [] totalSwimDistance = new float [swimTableSize];

    InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.fileList);
        fileArray = new ArrayList<String>();
        inputStream = null;
        checkPermission();
        loadFiles();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= 30){
            if (Environment.isExternalStorageManager() == true) {
                createFolder();
                loadFiles();
            }
        } else {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                createFolder();
            }
        }
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
                        if (Build.VERSION.SDK_INT < 30) {
                            createFolder();
                        }
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

    public void takeData(View view) throws IOException, NoSuchAlgorithmException {
        // Update list of fit files
        loadFiles();

        //LoadFitFileList();

        File file = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!file.exists()){
            Toast.makeText(getApplicationContext(),
                    "Directory is not exist",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (fileArray.size() == 0){
                Toast.makeText(getApplicationContext(), "Directory is empty", Toast.LENGTH_SHORT).show();
            } else {

                for (int i = 0; i < fileArray.size(); i++){
                    String str = String.format("%s%s%s", Environment.getExternalStorageDirectory(), folderName, fileArray.get(i));
//                    Log.d("FILE", str);
//                    Log.d("SHORTCUT", generateShortcut(str));
//                    Log.d("VALIDATE", String.valueOf(validateFile(str)));
                    if (validateFile(str) == true) {
                        decodeFitFiles(str);
                    } else {
                        File tmpFile = new File(str);
                        tmpFile.delete();
                    }
                }
                loadFiles();
            }
        }
    }

    private void createFolder() {
        File file = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!file.exists()){
            file.mkdirs();
            if (file.isDirectory()){
                Toast.makeText(getApplicationContext(),
                        "Directory is created successfully.",
                        Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(FilesActivity.this);
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
        saveOneTrainingToDatabase();
        fitFile.delete();
    }

    private void saveOneTrainingToDatabase(){
        int tmpTrainingId = getLastIdInDatabase() + 1;
        int userAge = 0;
        for (int i = 0; i < dataCount; i++){
            saveFileData(
                    tmpTrainingId,
                    swimStorke[i],
                    activeLengthsSwimPool[i],
                    totalSwimDistance[i],
                    kcalSwim[i],
                    swimTrainingDate[i],
                    elapsedTimeSwimming[i],
                    maxHeartRate[i],
                    avgHeartRate[i],
                    avarageSpeed[i],
                    avarageCadence[i],
                    userAge);
        }
        dataCount = 0;
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

    /* Save data to database */
    private void saveFileData(
            int id,
            String inputswimStorke,
            int inputActiveLengthsSwimPoolDb,
            float inputTotalSwimDistanceDb,
            int inputKcalSwimDb,
            DateTime inputSwimTrainingDateDb,
            float inputElapsedTimeSwimmingDb,
            int inputMaxHeartRateDb,
            int inputAvgHeartRateDb,
            float inputAvarageSpeedDb,
            int inputAvarageCadenceDb,
            int inputUserAge) {

        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());

        FitFile fitFile = new FitFile();
        fitFile.trainingId = id;
        fitFile.swimStorkeDb = inputswimStorke;
        fitFile.activeLengthsSwimPoolDb = inputActiveLengthsSwimPoolDb;
        fitFile.totalSwimDistanceDb = inputTotalSwimDistanceDb;
        fitFile.kcalSwimDb = inputKcalSwimDb;
        fitFile.swimTrainingDateDb = converterDate(String.valueOf(inputSwimTrainingDateDb));
        fitFile.elapsedTimeSwimmingDb = inputElapsedTimeSwimmingDb;
        fitFile.maxHeartRateDb = inputMaxHeartRateDb;
        fitFile.avgHeartRateDb = inputAvgHeartRateDb;
        fitFile.avarageSpeedDb = inputAvarageSpeedDb;
        fitFile.avarageCadenceDb = inputAvarageCadenceDb;
        fitFile.userAge = inputUserAge;
        database.fileDao().insertFile(fitFile);
    }

    private void LoadFitFileList(){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        List<FitFile> fitFileList = database.fileDao().getAllFitFileList();

        if(fitFileList.isEmpty()){
            Log.d("DATABASE", "Is Empty");
        } else {
            for (int i = 0; i < fitFileList.size(); i++){
                String str = String.format("Training ID: " + fitFileList.get(i).trainingId +
                        ", Swim Storke: " + fitFileList.get(i).swimStorkeDb +
                        ", Lengths Swim Pool: " + fitFileList.get(i).activeLengthsSwimPoolDb +
                        ", Total Swim Distance: " + fitFileList.get(i).totalSwimDistanceDb +
                        ", Kcal:  " + fitFileList.get(i).kcalSwimDb +
                        ", Date:  " + fitFileList.get(i).swimTrainingDateDb +
                        ", Elapsed Time:  " + showTimeSwim((int) fitFileList.get(i).elapsedTimeSwimmingDb) +
                        ", Max Heart Rate: " + fitFileList.get(i).maxHeartRateDb +
                        ", Min Heart Rate: " + fitFileList.get(i).avgHeartRateDb +
                        ", Avg Speed: " + fitFileList.get(i).avarageSpeedDb +
                        ", Avg Cadence: " + fitFileList.get(i).avarageCadenceDb
                );
                Log.d("DATABASE", str);
            }
            for (int i = 1; i <= database.fileDao().getLastID(); i++){
                String str2 = String.format(
                        "Training ID: %s, Total Swum Distance: %s, Total Swum Time: %s, Total Burned Kcal: %s, Training Date: %s",
                        i,
                        database.fileDao().getTotalSwumDistanceFile(i),
                        showTimeSwim(database.fileDao().getTotalSwumTime(i)),
                        database.fileDao().getTotalKcalSwim(i),
                        database.fileDao().getTrainingDate(i));
                Log.d("DB SUMMARY", str2);
            }
        }
    }

    private int getLastIdInDatabase(){
        FileDatabase database = FileDatabase.getDbInstance(this.getApplicationContext());
        return database.fileDao().getLastID();
    }

    private String showTimeSwim(int seconds){
        int sec = seconds % 60;
        int min = (seconds % 3600) / 60;
        int hours = seconds / 3600;

        String swimTime = String.format("%02d:%02d:%02d", hours, min, sec);
        return swimTime;
    }

    public String converterDate(String strDate) {
        //String test = "Thu Oct 17 20:22:56 GMT+02:00 2019";
        String [] arr = strDate.split("\\s+");

        String newDateFormat = "";

        if(arr[1].equals("Jan")){
            newDateFormat = arr[arr.length-1] + "-" + "01" + "-"+ arr[2];
        } else if(arr[1].equals("Feb")){
            newDateFormat = arr[arr.length-1] + "-" + "02" + "-"+ arr[2];
        } else if(arr[1].equals("Mar")){
            newDateFormat = arr[arr.length-1] + "-" + "03" + "-"+ arr[2];
        } else if(arr[1].equals("Apr")){
            newDateFormat = arr[arr.length-1] + "-" + "04" + "-"+ arr[2];
        } else if(arr[1].equals("May")){
            newDateFormat = arr[arr.length-1] + "-" + "05" + "-"+ arr[2];
        } else if(arr[1].equals("Jun")){
            newDateFormat = arr[arr.length-1] + "-" + "06" + "-"+ arr[2];
        } else if(arr[1].equals("Jul")){
            newDateFormat = arr[arr.length-1] + "-" + "07" + "-"+ arr[2];
        } else if(arr[1].equals("Aug")){
            newDateFormat = arr[arr.length-1] + "-" + "08" + "-"+ arr[2];
        } else if(arr[1].equals("Sep")){
            newDateFormat = arr[arr.length-1] + "-" + "09" + "-"+ arr[2];
        } else if(arr[1].equals("Oct")){
            newDateFormat = arr[arr.length-1] + "-" + "10" + "-"+ arr[2];
        } else if(arr[1].equals("Nov")){
            newDateFormat = arr[arr.length-1] + "-" + "11" + "-"+ arr[2];
        } else if(arr[1].equals("Dec")){
            newDateFormat = arr[arr.length-1] + "/" + "12" + "/"+ arr[2];
        }
        return newDateFormat;
    }

    public boolean validateFile(String filePath) throws IOException, NoSuchAlgorithmException {
        ShortcutDatabase shortcutDatabase = ShortcutDatabase.getDbInstance(this.getApplicationContext());
        List<ShortcutFile> shortcutFileList = shortcutDatabase.shortcutDao().getAllShortcutFile();

        ShortcutFile shortcutFile = new ShortcutFile();

        boolean validate = false;

        if(shortcutFileList.isEmpty()){
            shortcutFile.shortcut = generateShortcut(filePath);
            shortcutDatabase.shortcutDao().insert(shortcutFile);
            validate = true;
        } else {
            String tmp = generateShortcut(filePath);
            for (int i = 0; i < shortcutFileList.size(); i++) {
                if (tmp.equals(shortcutFileList.get(i).shortcut)) {
                    return false;
                }
            }
            shortcutFile.shortcut = generateShortcut(filePath);
            shortcutDatabase.shortcutDao().insert(shortcutFile);
            validate = true;
        }
        return validate;
    }
    /*
    UserDatabase database = UserDatabase.getDbInstance(this.getApplicationContext());
        UserData userData = new UserData();
        int m = month + 1;
        String strBirthday = String.format("%s-%s-%s", year, m, dayOfMonth);

        userData.userBirthdayDate = strBirthday;

        database.userDao().insertUser(userData);
     */

    public String generateShortcut(String filePath) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String shaChecksum = getFileChecksum(shaDigest, file);
        return shaChecksum;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        // This code is from:
        // https://howtodoinjava.com/java/io/sha-md5-file-checksum-hash/
        //
        //

        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public static class Listener implements LapMesgListener, UserProfileMesgListener {

        @Override
        public void onMesg(UserProfileMesg mesg) {
        // User Profile Mesg Listener

            if (mesg.getAge() != null) {
                //userAge = mesg.getAge();
            }
        }

        @Override
        public void onMesg(LapMesg mesg) {
            if (mesg.getSubSport() != null){
                if (mesg.getSubSport().toString().equals("LAP_SWIMMING")){
                    dataCount = dataCount + 1;
                    if (mesg.getSwimStroke() == null) {
                        swimStorke[dataCount - 1] = "BREAK";
                    } else {
                        swimStorke[dataCount - 1] = mesg.getSwimStroke().toString();
                    }
                    if (mesg.getMaxSpeed() != null) {
                    }
                    if (mesg.getNumActiveLengths() != null){ //number of completed lengths of pools | mesg.getNumActiveLengths() * 25 m = x
                        activeLengthsSwimPool[dataCount - 1] = mesg.getNumActiveLengths();
                    }
                    if (mesg.getTotalDistance() != null){
                        totalSwimDistance[dataCount - 1] = mesg.getTotalDistance();
                    }
                    if (mesg.getTotalCalories() != null){
                        kcalSwim[dataCount - 1] = mesg.getTotalCalories();
                    }
                    if (mesg.getStartTime() != null){
                        //Maybe TO DO
                    }
                    if (mesg.getTimestamp() != null){
                        swimTrainingDate[dataCount - 1] = mesg.getTimestamp();
                    }
                    if (mesg.getTotalElapsedTime() != null){
                        elapsedTimeSwimming[dataCount - 1] = mesg.getTotalElapsedTime();
                    }
                    if (mesg.getAvgSpeed() != null){
                        avarageSpeed[dataCount - 1] = mesg.getAvgSpeed();
                    }
                    if (mesg.getAvgSpeed() == null && mesg.getNumActiveLengths() != null && mesg.getTotalElapsedTime() != null){
                        //It is my way to calculate Avarage speed
                        avarageSpeed[dataCount - 1] = mesg.getTotalDistance() / mesg.getTotalElapsedTime();
                    }
                    if (mesg.getMaxHeartRate() != null){
                        maxHeartRate[dataCount - 1] = mesg.getMaxHeartRate();
                    }
                    if (mesg.getAvgHeartRate() != null){
                        avgHeartRate[dataCount - 1] = mesg.getAvgHeartRate();
                    }
                    if (mesg.getAvgCadence() != null){
                        avarageCadence[dataCount - 1] = mesg.getAvgCadence();
                    }
                }
            }
        }
    }
}


