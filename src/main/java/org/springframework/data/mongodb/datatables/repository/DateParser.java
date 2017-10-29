package org.springframework.data.mongodb.datatables.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {

    private static String[] FORMATS = {
            // ISO 8601 date-time
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            // ISO 8601 date-time
            "yyyy-MM-dd'T'HH:mm:ssZ",
            // ISO 8601 date-time with time-secfrac
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            // ISO 8601 date-time with time-secfrac
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            // ISO 8601 date-time without timezone
            "yyyy-MM-dd'T'HH:mm:ss",
            // Another commonly used pattern
            "yyyy-MM-dd HH:mm:ss",
            // ISO 8601 full-date
            "yyyy-MM-dd"
    };

    public static Date parse(String text) throws ParseException {
        for (final String format : FORMATS) {
            final SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(text);
            } catch (ParseException pe) {
                // continue to next format
            }
        }
        throw new ParseException("Unable to parse date from '" + text + '"', 0);
    }

}
