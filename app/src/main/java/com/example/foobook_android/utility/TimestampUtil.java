package com.example.foobook_android.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Utility class for handling timestamps.
public class TimestampUtil {

    // Gets the current timestamp.
    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // Gets the current timestamp for file.
    public static String getCurrentTimestampFile() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return now.format(formatter);
    }


}

