package com.desafio.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static int diffMinutes(LocalDateTime localDateTime) {
        return (int) ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
    }

}
