package com.example.fitfilereader.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FitFile.class}, version = 1)
public abstract class FileDatabase extends RoomDatabase {

    public abstract FileDao fileDao();

    private static FileDatabase INSTANCE;

    public static FileDatabase getDbInstance(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FileDatabase.class, "DB_FILE")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
