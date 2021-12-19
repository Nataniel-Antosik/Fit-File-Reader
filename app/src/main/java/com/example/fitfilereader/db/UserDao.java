package com.example.fitfilereader.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM userData")
    List<UserData> getAllBirthdayDate();

    @Query("SELECT user_birthday_date FROM userData")
    String getUserBirthdayDate();

    @Insert
    void insertUser(UserData... userData);
}
