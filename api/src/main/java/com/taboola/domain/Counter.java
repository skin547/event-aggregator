package com.taboola.domain;


public class Counter {
    private String timeBucket;
    private String eventId;
    private int count;

    public Counter(String timeBucket, String eventId, int count) {
        this.timeBucket = timeBucket;
        this.eventId = eventId;
        this.count = count;
    }

    // Getters and setters
    public String getTimeBucket() {
        return timeBucket;
    }

    public void setTimeBucket(String timeBucket) {
        this.timeBucket = timeBucket;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
