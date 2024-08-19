package com.taboola.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taboola.domain.Counter;
import com.taboola.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getCountersByTimeBucket(@PathVariable("timeBucket") String timeBucket) {

        try{
            List<Counter> counters = this.service.getCounters(timeBucket);
            Map<String, Integer> response = new HashMap<>();
            for (Counter counter : counters) {
                response.put(counter.getEventId(), counter.getCount());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Unexpected error occurred" + e);
            return new ResponseEntity<>("internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // the timeBucket should be like yyyyMMddHHmm
    @GetMapping("/counters/time/{timeBucket}/eventId/{eventId}")
    public ResponseEntity<?> getCounterByTimeBucketAndEventId(@PathVariable("timeBucket") String timeBucket,
                                                                    @PathVariable("eventId") String eventId) {
        try{
            Counter c = this.service.getCounterByEventId(timeBucket, eventId);
            int count = (c != null) ? c.getCount() : 0;
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Unexpected error occurred" + e);
            return new ResponseEntity<>("internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
