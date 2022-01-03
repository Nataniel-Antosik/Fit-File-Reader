package com.example.fitfilereader.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShortcutFile {

    @PrimaryKey(autoGenerate = true)
    public int sID;

    @ColumnInfo(name = "shortcut_file")
    public String shortcut = " ";
}
