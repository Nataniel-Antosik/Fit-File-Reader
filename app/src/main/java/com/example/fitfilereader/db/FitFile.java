package com.example.fitfilereader.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FitFile {

    @PrimaryKey
    public int fID = 0;

    @ColumnInfo(name = "distance_Swum")
    public int distanceSwum;

    @ColumnInfo(name = "all_Burn_Kcal")
    public int allBurnKcal;

    @ColumnInfo(name = "all_Time_Swum")
    public float allTimeSwum;

}
