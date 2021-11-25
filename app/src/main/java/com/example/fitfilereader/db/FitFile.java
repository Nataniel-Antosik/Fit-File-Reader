package com.example.fitfilereader.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FitFile {

    @PrimaryKey(autoGenerate = true)
    public int fID;

    @ColumnInfo(name = "training_id")
    public int trainingId = 0;

    @ColumnInfo(name = "swim_storke")
    public String swimStorkeDb;

    @ColumnInfo(name = "active_lengths_swim_pool")
    public int activeLengthsSwimPoolDb;

    @ColumnInfo(name = "total_swim_distance")
    public float totalSwimDistanceDb;

    @ColumnInfo(name = "kcal_swim")
    public int kcalSwimDb;

    @ColumnInfo(name = "elapsed_time_swimming")
    public float elapsedTimeSwimmingDb;

    @ColumnInfo(name = "max_heart_rate")
    public int maxHeartRateDb;

    @ColumnInfo(name = "avg_heart_rate")
    public int avgHeartRateDb;

    @ColumnInfo(name = "avarage_speed")
    public float avarageSpeedDb;

    @ColumnInfo(name = "avarage_cadence")
    public int avarageCadenceDb;

//    @ColumnInfo(name = "distance_Swum")
//    public int distanceSwum;
//
//    @ColumnInfo(name = "all_Burn_Kcal")
//    public int allBurnKcal;
//
//    @ColumnInfo(name = "all_Time_Swum")
//    public float allTimeSwum;

}
