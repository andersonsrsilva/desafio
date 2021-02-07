package com.desafio.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static LocalDateTime initialDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toLocalDate().atTime(LocalTime.MIN);
    }

    public static LocalDateTime finalDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    public static LocalDateTime initialDate(LocalDate date) {
        return date.atTime(LocalTime.MIN);
    }

    public static LocalDateTime finalDate(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    public static int diffMinutes(LocalDateTime first) {
        return (int) ChronoUnit.MINUTES.between(first, LocalDateTime.now());
    }

    public static int diffMinutes(LocalDateTime first, LocalDateTime last) {
        return (int) ChronoUnit.MINUTES.between(first, last);
    }

    public static boolean isValid(final String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String dateToString(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return data.format(formatter);
    }

    public static LocalDateTime changeTime(LocalDateTime localDateTime, LocalTime localTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), localTime);
    }
}
