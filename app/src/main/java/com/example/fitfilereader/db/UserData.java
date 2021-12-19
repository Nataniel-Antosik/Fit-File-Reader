package com.example.fitfilereader.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserData {

    @PrimaryKey(autoGenerate = true)
    public int uID;

    @ColumnInfo(name = "user_birthday_date")
    public String userBirthdayDate = " ";
}

