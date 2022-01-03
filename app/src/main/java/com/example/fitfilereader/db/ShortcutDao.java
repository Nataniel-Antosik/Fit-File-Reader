package com.example.fitfilereader.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShortcutDao {

    @Query("SELECT * FROM shortcutFile")
    List<ShortcutFile> getAllShortcutFile();

    @Insert
    void insert(ShortcutFile... shortcutFile);
}
