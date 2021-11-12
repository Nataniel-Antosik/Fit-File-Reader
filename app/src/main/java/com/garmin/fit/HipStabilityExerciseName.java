////////////////////////////////////////////////////////////////////////////////
// The following FIT Protocol software provided may be used with FIT protocol
// devices only and remains the copyrighted property of Garmin Canada Inc.
// The software is being provided on an "as-is" basis and as an accommodation,
// and therefore all warranties, representations, or guarantees of any kind
// (whether express, implied or statutory) including, without limitation,
// warranties of merchantability, non-infringement, or fitness for a particular
// purpose, are specifically disclaimed.
//
// Copyright 2021 Garmin Canada Inc.
////////////////////////////////////////////////////////////////////////////////
// ****WARNING****  This file is auto-generated!  Do NOT edit this file.
// Profile Version = 21.60Release
// Tag = production/akw/21.60.00-0-g38385705
////////////////////////////////////////////////////////////////////////////////


package com.garmin.fit;

import java.util.HashMap;
import java.util.Map;

public class HipStabilityExerciseName  {
    public static final int BAND_SIDE_LYING_LEG_RAISE = 0;
    public static final int DEAD_BUG = 1;
    public static final int WEIGHTED_DEAD_BUG = 2;
    public static final int EXTERNAL_HIP_RAISE = 3;
    public static final int WEIGHTED_EXTERNAL_HIP_RAISE = 4;
    public static final int FIRE_HYDRANT_KICKS = 5;
    public static final int WEIGHTED_FIRE_HYDRANT_KICKS = 6;
    public static final int HIP_CIRCLES = 7;
    public static final int WEIGHTED_HIP_CIRCLES = 8;
    public static final int INNER_THIGH_LIFT = 9;
    public static final int WEIGHTED_INNER_THIGH_LIFT = 10;
    public static final int LATERAL_WALKS_WITH_BAND_AT_ANKLES = 11;
    public static final int PRETZEL_SIDE_KICK = 12;
    public static final int WEIGHTED_PRETZEL_SIDE_KICK = 13;
    public static final int PRONE_HIP_INTERNAL_ROTATION = 14;
    public static final int WEIGHTED_PRONE_HIP_INTERNAL_ROTATION = 15;
    public static final int QUADRUPED = 16;
    public static final int QUADRUPED_HIP_EXTENSION = 17;
    public static final int WEIGHTED_QUADRUPED_HIP_EXTENSION = 18;
    public static final int QUADRUPED_WITH_LEG_LIFT = 19;
    public static final int WEIGHTED_QUADRUPED_WITH_LEG_LIFT = 20;
    public static final int SIDE_LYING_LEG_RAISE = 21;
    public static final int WEIGHTED_SIDE_LYING_LEG_RAISE = 22;
    public static final int SLIDING_HIP_ADDUCTION = 23;
    public static final int WEIGHTED_SLIDING_HIP_ADDUCTION = 24;
    public static final int STANDING_ADDUCTION = 25;
    public static final int WEIGHTED_STANDING_ADDUCTION = 26;
    public static final int STANDING_CABLE_HIP_ABDUCTION = 27;
    public static final int STANDING_HIP_ABDUCTION = 28;
    public static final int WEIGHTED_STANDING_HIP_ABDUCTION = 29;
    public static final int STANDING_REAR_LEG_RAISE = 30;
    public static final int WEIGHTED_STANDING_REAR_LEG_RAISE = 31;
    public static final int SUPINE_HIP_INTERNAL_ROTATION = 32;
    public static final int WEIGHTED_SUPINE_HIP_INTERNAL_ROTATION = 33;
    public static final int INVALID = Fit.UINT16_INVALID;

    private static final Map<Integer, String> stringMap;

    static {
        stringMap = new HashMap<Integer, String>();
        stringMap.put(BAND_SIDE_LYING_LEG_RAISE, "BAND_SIDE_LYING_LEG_RAISE");
        stringMap.put(DEAD_BUG, "DEAD_BUG");
        stringMap.put(WEIGHTED_DEAD_BUG, "WEIGHTED_DEAD_BUG");
        stringMap.put(EXTERNAL_HIP_RAISE, "EXTERNAL_HIP_RAISE");
        stringMap.put(WEIGHTED_EXTERNAL_HIP_RAISE, "WEIGHTED_EXTERNAL_HIP_RAISE");
        stringMap.put(FIRE_HYDRANT_KICKS, "FIRE_HYDRANT_KICKS");
        stringMap.put(WEIGHTED_FIRE_HYDRANT_KICKS, "WEIGHTED_FIRE_HYDRANT_KICKS");
        stringMap.put(HIP_CIRCLES, "HIP_CIRCLES");
        stringMap.put(WEIGHTED_HIP_CIRCLES, "WEIGHTED_HIP_CIRCLES");
        stringMap.put(INNER_THIGH_LIFT, "INNER_THIGH_LIFT");
        stringMap.put(WEIGHTED_INNER_THIGH_LIFT, "WEIGHTED_INNER_THIGH_LIFT");
        stringMap.put(LATERAL_WALKS_WITH_BAND_AT_ANKLES, "LATERAL_WALKS_WITH_BAND_AT_ANKLES");
        stringMap.put(PRETZEL_SIDE_KICK, "PRETZEL_SIDE_KICK");
        stringMap.put(WEIGHTED_PRETZEL_SIDE_KICK, "WEIGHTED_PRETZEL_SIDE_KICK");
        stringMap.put(PRONE_HIP_INTERNAL_ROTATION, "PRONE_HIP_INTERNAL_ROTATION");
        stringMap.put(WEIGHTED_PRONE_HIP_INTERNAL_ROTATION, "WEIGHTED_PRONE_HIP_INTERNAL_ROTATION");
        stringMap.put(QUADRUPED, "QUADRUPED");
        stringMap.put(QUADRUPED_HIP_EXTENSION, "QUADRUPED_HIP_EXTENSION");
        stringMap.put(WEIGHTED_QUADRUPED_HIP_EXTENSION, "WEIGHTED_QUADRUPED_HIP_EXTENSION");
        stringMap.put(QUADRUPED_WITH_LEG_LIFT, "QUADRUPED_WITH_LEG_LIFT");
        stringMap.put(WEIGHTED_QUADRUPED_WITH_LEG_LIFT, "WEIGHTED_QUADRUPED_WITH_LEG_LIFT");
        stringMap.put(SIDE_LYING_LEG_RAISE, "SIDE_LYING_LEG_RAISE");
        stringMap.put(WEIGHTED_SIDE_LYING_LEG_RAISE, "WEIGHTED_SIDE_LYING_LEG_RAISE");
        stringMap.put(SLIDING_HIP_ADDUCTION, "SLIDING_HIP_ADDUCTION");
        stringMap.put(WEIGHTED_SLIDING_HIP_ADDUCTION, "WEIGHTED_SLIDING_HIP_ADDUCTION");
        stringMap.put(STANDING_ADDUCTION, "STANDING_ADDUCTION");
        stringMap.put(WEIGHTED_STANDING_ADDUCTION, "WEIGHTED_STANDING_ADDUCTION");
        stringMap.put(STANDING_CABLE_HIP_ABDUCTION, "STANDING_CABLE_HIP_ABDUCTION");
        stringMap.put(STANDING_HIP_ABDUCTION, "STANDING_HIP_ABDUCTION");
        stringMap.put(WEIGHTED_STANDING_HIP_ABDUCTION, "WEIGHTED_STANDING_HIP_ABDUCTION");
        stringMap.put(STANDING_REAR_LEG_RAISE, "STANDING_REAR_LEG_RAISE");
        stringMap.put(WEIGHTED_STANDING_REAR_LEG_RAISE, "WEIGHTED_STANDING_REAR_LEG_RAISE");
        stringMap.put(SUPINE_HIP_INTERNAL_ROTATION, "SUPINE_HIP_INTERNAL_ROTATION");
        stringMap.put(WEIGHTED_SUPINE_HIP_INTERNAL_ROTATION, "WEIGHTED_SUPINE_HIP_INTERNAL_ROTATION");
    }


    /**
     * Retrieves the String Representation of the Value
     * @return The string representation of the value, or empty if unknown
     */
    public static String getStringFromValue( Integer value ) {
        if( stringMap.containsKey( value ) ) {
            return stringMap.get( value );
        }

        return "";
    }

    /**
     * Retrieves a value given a string representation
     * @return The value or INVALID if unkwown
     */
    public static Integer getValueFromString( String value ) {
        for( Map.Entry<Integer, String> entry : stringMap.entrySet() ) {
            if( entry.getValue().equals( value ) ) {
                return entry.getKey();
            }
        }

        return INVALID;
    }

}
