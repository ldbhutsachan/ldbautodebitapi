package com.ldbbank.autodebit_svc.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Component
public class ScheduledBatchRunner {
    private static final Logger log = LoggerFactory.getLogger(ScheduledBatchRunner.class);

    private final BatchService batchService;

    @Value("${batch.auto-run:false}")
    private boolean autoRun;

    private final AtomicBoolean running = new AtomicBoolean(false);

    // Runs every batch.interval.ms (default 30 minutes) after application start
    @Scheduled(fixedRateString = "${batch.interval.ms:1800000}", initialDelayString = "${batch.initial.delay.ms:60000}")
    public void scheduledRun() {
        if (!autoRun) {
            return;
        }
        if (!running.compareAndSet(false, true)) {
            log.info("ScheduledBatchRunner: previous run still in progress, skipping this schedule");
            return;
        }
        try {
            LocalDate today = LocalDate.now();
            log.info("ScheduledBatchRunner: starting batch for date {}", today);
            batchService.runForDate(today);
            log.info("ScheduledBatchRunner: finished batch for date {}", today);
        } catch (Exception ex) {
            log.error("ScheduledBatchRunner: error during batch run", ex);
        } finally {
            running.set(false);
        }
    }
}
