package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Не сделала "Попробуйте использовать LocalDateTime вместо LocalDate с прицелом на то, что в DB будет тип даты timestamp."
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return (startTime == null || lt.compareTo(startTime) >= 0) && (endTime == null || lt.compareTo(endTime) < 0);
    }

    public static boolean isBetweenDates(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || ld.compareTo(startDate) >= 0) && (endDate == null || ld.compareTo(endDate) <= 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String ld) {
        return ld.isEmpty() ? null : LocalDate.parse(ld);
    }

    public static LocalTime parseTime(String lt) {
        return lt.isEmpty() ? null : LocalTime.parse(lt);
    }
}

