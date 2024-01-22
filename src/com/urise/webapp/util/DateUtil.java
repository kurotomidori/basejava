package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("YYYY/MM");
    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate of(String date) {
        if(date.isEmpty()) {
           return NOW;
        }
        String[] dateArray = date.split("-");
        return LocalDate.of(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]));
    }

    public static String dateToString(LocalDate ld) {
        if(ld == null) {
            return "";
        } else if (ld.isEqual(NOW)) {
            return "настоящее время";
        } else {
            return ld.format(DTF);
        }
    }
}
