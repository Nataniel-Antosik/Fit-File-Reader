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

    @Query("SELECT SUM(elapsed_time_swimming) FROM fitFile WHERE training_id = :trainingIdToDatabase AND swim_storke != 'BREAK'")
    int getTotalSwumTime(int trainingIdToDatabase);

    @Query("SELECT SUM(elapsed_time_swimming) FROM fitFile WHERE training_id = :trainingIdToDatabase")
    int getTotalTime(int trainingIdToDatabase);

    @Query("SELECT SUM(kcal_swim) FROM fitFile WHERE training_id = :trainingIdToDatabase")
    int getTotalKcalSwim(int trainingIdToDatabase);

    @Query("SELECT AVG(avarage_speed) FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke != 'BREAK'")
    float getAvgPace(int trainingIdToDatabase);

    @Query("SELECT AVG(avarage_speed) FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke == 'BUTTERFLY'")
    float getAvgPaceOnButterfly(int trainingIdToDatabase);

    @Query("SELECT AVG(avarage_speed) FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke == 'BACKSTROKE'")
    float getAvgPaceOnBackstroke(int trainingIdToDatabase);

    @Query("SELECT AVG(avarage_speed) FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke == 'BREASTSTROKE'")
    float getAvgPaceOnBreaststroke(int trainingIdToDatabase);

    @Query("SELECT AVG(avarage_speed) FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke == 'FREESTYLE'")
    float getAvgPaceOnFreestyle(int trainingIdToDatabase);

    @Query("SELECT AVG(avg_heart_rate) FROM fitfile WHERE training_id = :trainingIdToDatabase")
    int getAvgHeartRate(int trainingIdToDatabase);

    @Query("SELECT AVG(avarage_cadence) FROM fitfile WHERE training_id = :trainingIdToDatabase")
    int getAvgCadence(int trainingIdToDatabase);

    @Query("SELECT MAX(avg_heart_rate) FROM fitfile WHERE training_id = :trainingIdToDatabase")
    int getMaxHeartRate(int trainingIdToDatabase);

    @Query("SELECT MAX(avarage_speed) FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke != 'BREAK'")
    float getMaxPace(int trainingIdToDatabase);

    @Query("SELECT MAX(avarage_speed) FROM fitfile WHERE swim_storke == 'BUTTERFLY'")
    float getBestPaceButterfly();

    @Query("SELECT MAX(avarage_speed) FROM fitfile WHERE swim_storke == 'BACKSTROKE'")
    float getBestPaceBackstroke();

    @Query("SELECT MAX(avarage_speed) FROM fitfile WHERE swim_storke == 'BREASTSTROKE'")
    float getBestPaceBreaststroke();

    @Query("SELECT MAX(avarage_speed) FROM fitfile WHERE swim_storke == 'FREESTYLE'")
    float getBestPaceFreestyle();

    @Query("SELECT * FROM fitfile WHERE training_id = :trainingIdToDatabase AND swim_storke != 'BREAK'")
    List<FitFile> getOneTrainingWithoutBreak(int trainingIdToDatabase);

    @Query("SELECT SUM(kcal_swim) FROM fitfile")
    int getAllBurnedKcal();

    @Query("SELECT SUM(total_swim_distance) FROM fitfile")
    int getAllSwumDistance();

    @Query("SELECT swim_training_date FROM fitFile WHERE training_id = :trainingIdToDatabase")
    String getTrainingDate(int trainingIdToDatabase);

    @Insert
    void insertFile(FitFile... fitFiles);

    @Delete
    void delete(FitFile fitFile);

}