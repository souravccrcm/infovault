package com.ccrcm.infovault.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateUtil() {}

    public static String nowAsString() {
        return LocalDateTime.now().format(FORMATTER);
    }
}