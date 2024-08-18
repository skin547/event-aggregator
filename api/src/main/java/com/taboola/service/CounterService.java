package com.taboola.service;

import com.taboola.domain.Counter;
import com.taboola.domain.TimeBucket;
import com.taboola.domain.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounterService {

    private final CounterRepository counterRepository;

    @Autowired
    public CounterService(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    public List<Counter> getCounters(String timeBucketStr) {
        TimeBucket timeBucket = new TimeBucket(timeBucketStr);
        return counterRepository.getCounters(timeBucket);
    }

    public Counter getCounterByEventId(String timeBucketStr, String eventId) {
        TimeBucket timeBucket = new TimeBucket(timeBucketStr);
        return counterRepository.getCountersOfEvent(timeBucket, eventId);
    }
}
