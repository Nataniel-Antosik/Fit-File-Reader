package com.example.fitfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.fitfilereader.db.FitFile;
import com.example.fitfilereader.db.UserData;
import com.example.fitfilereader.db.UserDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BirthdayActivity extends AppCompatActivity {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        mDisplayDate = findViewById(R.id.text_view_user_birthday);

        checkUserBirthdayDate();

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                Locale locale = new Locale("ENG");
                Locale.setDefault(locale);

                Configuration config = new Configuration();
                config.locale = locale;
                getApplicationContext().getResources().updateConfiguration(config, null);

                DatePickerDialog dialog = new DatePickerDialog(
                        BirthdayActivity.this,
                        android.R.style.Theme_Holo_Dialog,
                        mDateSetListener,
                        year - 18,
                        month,
                        day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setTitle("Choose Your Birthday Date");
                dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", dialog);
                dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", dialog);
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                saveUserBirthday(year, month, dayOfMonth);
            }
        };
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void saveUserBirthday(int year, int month, int dayOfMonth){
        UserDatabase database = UserDatabase.getDbInstance(this.getApplicationContext());
        UserData userData = new UserData();
        int m = month + 1;
        String strBirthday = String.format("%s-%s-%s", year, m, dayOfMonth);

        userData.userBirthdayDate = strBirthday;

        database.userDao().insertUser(userData);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void checkUserBirthdayDate(){
        UserDatabase database = UserDatabase.getDbInstance(this.getApplicationContext());
        List<UserData> userFileList = database.userDao().getAllBirthdayDate();

        if(userFileList.size() != 0){
             finish();
        }
    }
}