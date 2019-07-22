package com.kutseiko.bicycle.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateConverter {

    public static LocalDate convertDateToLocalDate(java.sql.Date date) {
        return date.toLocalDate();
    }

    public static java.sql.Date convertLocalDateToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    public static LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
