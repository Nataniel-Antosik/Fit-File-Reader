package com.example.fitfilereader.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserData.class}, version = 3)
public abstract class UserDatabase extends RoomDatabase{
    public abstract UserDao userDao();

    private static UserDatabase INSTANCE;

    public static UserDatabase getDbInstance(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "DB_USER")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}

