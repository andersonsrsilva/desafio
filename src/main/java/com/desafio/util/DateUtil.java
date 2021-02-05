package com.desafio.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {

    public static LocalDateTime initialDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toLocalDate().atTime(LocalTime.MIN);
    }

    public static LocalDateTime finalDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toLocalDate().atTime(LocalTime.MAX);
    }

}
