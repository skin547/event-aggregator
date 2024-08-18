package com.taboola.api;

import java.time.Instant;
import java.util.List;

import com.taboola.domain.Counter;
import com.taboola.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class Controller {
    private final CounterService service;

    @Autowired
    public Controller(CounterService service) {
        this.service = service;
    }


    @GetMapping("/currentTime")
    public long time() {
        return Instant.now().toEpochMilli();
    }


    // the timeBucket should be like yyyyMMddHHmm
    @GetMapping("/counters/time/{timeBucket}")
    public List<Counter> getCountersByTimeBucket(@PathVariable("timeBucket") String timeBucket) {
        return this.service.getCounters(timeBucket);
    }

    // the timeBucket should be like yyyyMMddHHmm
    @GetMapping("/counters/time/{timeBucket}/eventId/{eventId}")
    public Counter getCounterByTimeBucketAndEventId(@PathVariable("timeBucket") String timeBucket,
                                                    @PathVariable("eventId") String eventId) {
        return this.service.getCounterByEventId(timeBucket, eventId);
    }

}
