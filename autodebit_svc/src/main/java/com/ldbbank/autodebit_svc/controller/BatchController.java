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

    @PostMapping("/schedule1")
    public void dailyRun() {
        try {
            batchService.runForDate();
        } catch (Exception ex) {
            System.err.println("Scheduled batch error: " + ex.getMessage());
        }
    }
}

