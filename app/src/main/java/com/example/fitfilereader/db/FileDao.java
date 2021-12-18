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

    @Query("SELECT * FROM fitFile WHERE training_id = :trainingIdToDatabase")
    List<FitFile> getOneTraining(int trainingIdToDatabase);

    @Query("SELECT MAX(training_id) FROM fitFile")
    int getLastID();

    @Query("SELECT SUM(total_swim_distance) FROM fitFile WHERE training_id = :trainingIdToDatabase")
    int getTotalSwumDistanceFile(int trainingIdToDatabase);

    @Query("SELECT SUM(elapsed_time_swimming) FROM fitFile WHERE training_id = :trainingIdToDatabase")
    int getTotalSwumTimeSwum(int trainingIdToDatabase);

    @Query("SELECT SUM(kcal_swim) FROM fitFile WHERE training_id = :trainingIdToDatabase")
    int getTotalKcalSwim(int trainingIdToDatabase);

    @Query("SELECT AVG(avarage_speed) FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke != 'BREAK'")
    float getAvgPace(int trainingIdToDatabase);

    @Query("SELECT swim_training_date FROM fitFile WHERE training_id = :trainingIdToDatabase")
    String getTrainingDate(int trainingIdToDatabase);

    @Insert
    void insertFile(FitFile... fitFiles);

    @Delete
    void delete(FitFile fitFile);

}
