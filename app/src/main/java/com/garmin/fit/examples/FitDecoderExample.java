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

package com.garmin.fit.examples;

import com.garmin.fit.FitDecoder;
import com.garmin.fit.FitMessages;
import com.garmin.fit.FitRuntimeException;
import com.garmin.fit.plugins.HrToRecordMesgBroadcastPlugin;

import java.io.FileInputStream;

public class FitDecoderExample {

    public static void main(String[] args) {
        System.out.println("FIT Decode Example Application");

        if (!validateCommandLine(args)) {
            printUsage();
            return;
        }

        try {
            System.out.println("Opening file: " + args[0]);
            FileInputStream inputStream = new FileInputStream(args[0]);

            System.out.println("Decoding file...");
            FitDecoder fitDecoder = new FitDecoder();
            FitMessages fitMessages;

            if (args.length == 1) {
                fitMessages = fitDecoder.decode(inputStream);
            } else {
                fitMessages = fitDecoder.decode(inputStream, new HrToRecordMesgBroadcastPlugin());
            }

            System.out.println("File decoded");

            // fitMessages will contain all of the messages decoded from the file.
            printMessageSummary(fitMessages);

        } catch (java.io.IOException e) {
            System.out.println("IOException opening file: " + args[0]);
            e.printStackTrace();
            return;
        } catch (FitRuntimeException e) {
            System.out.println("FitRuntimeException decoding file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception decoding file: " + e.getMessage());
            e.printStackTrace();
        }

        return;
    }

    private static boolean validateCommandLine(String[] args) {
        if (args.length == 0 || args.length > 2) {
            return false;
        }

        if (args.length == 2) {
            return args[1].equals("-hr");
        }

        return true;
    }

    private static void printUsage() {
        System.out.println("Usage: java DecodeExample <filename> [-hr]");
        System.out.println("<filename>      required");
        System.out.println("-hr             optional argument to use the HrMesgToBroadcastPlugin when decoding file");
    }

    private static void printMessageSummary(FitMessages fitMessages) {
        if(!fitMessages.getFileIdMesgs().isEmpty()) {
            System.out.println("FileId Messages: " + fitMessages.getFileIdMesgs().size());
        }
    }
}
