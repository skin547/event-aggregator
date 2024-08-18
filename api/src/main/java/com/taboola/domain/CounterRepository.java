package com.taboola.domain;

import java.util.List;

public interface CounterRepository {
    public List<Counter> getCounters(TimeBucket timeBucket);
    public Counter getCountersOfEvent(TimeBucket timeBucket, String eventId);
}
