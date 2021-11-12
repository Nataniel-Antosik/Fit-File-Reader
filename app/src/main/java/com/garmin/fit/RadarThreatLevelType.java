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


public enum RadarThreatLevelType  {
    THREAT_UNKNOWN((short)0),
    THREAT_NONE((short)1),
    THREAT_APPROACHING((short)2),
    THREAT_APPROACHING_FAST((short)3),
    INVALID((short)255);

    protected short value;

    private RadarThreatLevelType(short value) {
        this.value = value;
    }

    public static RadarThreatLevelType getByValue(final Short value) {
        for (final RadarThreatLevelType type : RadarThreatLevelType.values()) {
            if (value == type.value)
                return type;
        }

        return RadarThreatLevelType.INVALID;
    }

    /**
     * Retrieves the String Representation of the Value
     * @return The string representation of the value
     */
    public static String getStringFromValue( RadarThreatLevelType value ) {
        return value.name();
    }

    public short getValue() {
        return value;
    }


}
