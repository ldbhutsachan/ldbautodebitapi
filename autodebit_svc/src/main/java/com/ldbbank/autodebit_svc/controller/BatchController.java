package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/batch")
public class BatchController {

    private final BatchService batchService;

    @PostMapping("/startRetry")
    public void dailyRun() {
        try {
            batchService.runForDateRetry();
        } catch (Exception ex) {
            System.err.println("Scheduled batch error: " + ex.getMessage());
        }
    }

    @Scheduled(cron = "0 20 11 16 * ?") // run at 09:00 on the 16th of every month
    public void monthlyRun() {
        try {
            log.info("====start Monthly batch executed for date========:" + LocalDate.now());
            batchService.runForMonth();
        } catch (Exception ex) {
            log.error("Scheduled batch error: {}", ex.getMessage(), ex);
        }
    }

}

