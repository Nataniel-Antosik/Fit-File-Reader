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


public enum AutoSyncFrequency  {
    NEVER((short)0),
    OCCASIONALLY((short)1),
    FREQUENT((short)2),
    ONCE_A_DAY((short)3),
    REMOTE((short)4),
    INVALID((short)255);

    protected short value;

    private AutoSyncFrequency(short value) {
        this.value = value;
    }

    public static AutoSyncFrequency getByValue(final Short value) {
        for (final AutoSyncFrequency type : AutoSyncFrequency.values()) {
            if (value == type.value)
                return type;
        }

        return AutoSyncFrequency.INVALID;
    }

    /**
     * Retrieves the String Representation of the Value
     * @return The string representation of the value
     */
    public static String getStringFromValue( AutoSyncFrequency value ) {
        return value.name();
    }

    public short getValue() {
        return value;
    }


}
