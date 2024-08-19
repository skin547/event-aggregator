package com.taboola.domain;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeBucket {
    private static final String DATE_FORMAT = "yyyyMMddHHmm";
    private final String timeBucketString;
    private final Timestamp timestamp;

    public TimeBucket(String timeBucketString) {
        this.timeBucketString = timeBucketString;
        this.timestamp = convertToTimestamp(timeBucketString);
    }

    private Timestamp convertToTimestamp(String timeBucketString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            return new Timestamp(dateFormat.parse(timeBucketString).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid time bucket format, should be " + DATE_FORMAT, e);
        }
    }

    public String getTimeBucketString() {
        return timeBucketString;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
