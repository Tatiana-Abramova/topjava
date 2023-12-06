package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Не сделала "Попробуйте использовать LocalDateTime вместо LocalDate с прицелом на то, что в DB будет тип даты timestamp."
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        boolean result = true;
        if (startTime != null) {
            result = lt.compareTo(startTime) >= 0;
        }
        if (endTime != null) {
            result = result && lt.compareTo(endTime) < 0;
        }
        return result;
    }

    public static boolean isBetweenDates(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        boolean result = true;
        if (startDate != null) {
            result = ld.compareTo(startDate) >= 0;
        }
        if (endDate != null) {
            result = result && ld.compareTo(endDate) <= 0;
        }
        return result;
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

