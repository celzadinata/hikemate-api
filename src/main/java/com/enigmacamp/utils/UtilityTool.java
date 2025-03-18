package com.enigmacamp.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class UtilityTool {
    public static boolean isDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Date parseToDate(String stringDate, boolean endDay) {
        final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(stringDate, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + stringDate + ". Format date must be yyyy-MM-dd. Example: 2025-01-01", e);
        }

        if(endDay) {
            return Date.from(localDate.atTime(23, 59, 59, 999_999_999).atZone(ZoneId.systemDefault()).toInstant());
        }else{
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }
}
