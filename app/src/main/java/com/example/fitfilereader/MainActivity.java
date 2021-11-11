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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test";
    private static final String folderName = "/Fit File Storage/";

    ArrayList<String> fileArray;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.d(TAG, "Test 1");
        listView = findViewById(R.id.fileList);
        fileArray = new ArrayList<String>();

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

    public void takeData(View view){

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
    public void loadFiles(){
        File dir = new File(Environment.getExternalStorageDirectory(), folderName);
        if (dir.exists()) {
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
}


