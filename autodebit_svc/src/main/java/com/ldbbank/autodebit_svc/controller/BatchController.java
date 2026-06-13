package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@RestController
@RequestMapping("/batch")
public class BatchController {

    private final BatchService batchService;

    @Value("${batch.key:}")
    private String batchKey;

    @PostMapping("/run")
    public ResponseEntity<?> runBatch(@RequestHeader(value = "X-BATCH-KEY", required = false) String key,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (batchKey != null && !batchKey.isBlank()) {
            if (key == null || !key.equals(batchKey)) {
                return ResponseEntity.status(401).body("Invalid batch key");
            }
        }
        batchService.runForDate(date);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/start-now")
    public ResponseEntity<?> startNow(@RequestHeader(value = "X-BATCH-KEY", required = false) String key) {
        if (batchKey != null && !batchKey.isBlank()) {
            if (key == null || !key.equals(batchKey)) {
                return ResponseEntity.status(401).body("Invalid batch key");
            }
        }
        // Run asynchronously so HTTP returns immediately
        new Thread(() -> {
            try {
                batchService.runForDate(LocalDate.now());
            } catch (Exception ex) {
                // log via stdout since logger not injected here
                System.err.println("Batch start-now error: " + ex.getMessage());
            }
        }).start();
        return ResponseEntity.ok("Batch started");
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleRun(@RequestHeader(value = "X-BATCH-KEY", required = false) String key,
                                         @RequestParam("time") String timeStr) {
        if (batchKey != null && !batchKey.isBlank()) {
            if (key == null || !key.equals(batchKey)) {
                return ResponseEntity.status(401).body("Invalid batch key");
            }
        }
        LocalTime time;
        try {
            time = LocalTime.parse(timeStr);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Invalid time format. Use HH:mm, e.g. 15:20");
        }
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime runAtInitial = now.withHour(time.getHour()).withMinute(time.getMinute()).withSecond(0).withNano(0);
        final LocalDateTime runAt = runAtInitial.isBefore(now) ? runAtInitial.plusDays(1) : runAtInitial;
        final long delay = Duration.between(now, runAt).toMillis();

        final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.schedule(() -> {
            try {
                batchService.runForDate(runAt.toLocalDate());
            } catch (Exception ex) {
                System.err.println("Scheduled batch error: " + ex.getMessage());
            } finally {
                exec.shutdown();
            }
        }, delay, TimeUnit.MILLISECONDS);

        return ResponseEntity.ok("Scheduled batch at " + runAt.toString());
    }

    // Runs daily at 15:13 server time
    @Scheduled(cron = "0 29 15 * * *")
    public void dailyRun() {
        try {
            batchService.runForDate(LocalDate.now());
        } catch (Exception ex) {
            System.err.println("Scheduled batch error: " + ex.getMessage());
        }
    }
}

