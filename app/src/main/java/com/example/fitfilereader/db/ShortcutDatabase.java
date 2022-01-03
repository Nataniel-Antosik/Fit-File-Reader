package com.example.fitfilereader.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ShortcutFile.class}, version = 3)
public abstract class ShortcutDatabase extends RoomDatabase {
    public abstract ShortcutDao shortcutDao();

    private static ShortcutDatabase INSTANCE;

    public static ShortcutDatabase getDbInstance(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ShortcutDatabase.class, "DB_SHORTCUT")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
