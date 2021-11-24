package com.example.fitfilereader.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FileDao {

    @Query("SELECT * FROM fitFile")
    List<FitFile> getAllFitFileList();

    @Query("SELECT MAX(fID) FROM fitFile")
    int getLastID();

    @Insert
    void insertFile(FitFile... fitFiles);

    @Delete
    void delete(FitFile fitFile);

}
