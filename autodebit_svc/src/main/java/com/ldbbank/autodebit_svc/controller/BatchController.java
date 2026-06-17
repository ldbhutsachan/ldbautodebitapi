package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.model.whatapp.DataResponse2;
import com.ldbbank.autodebit_svc.service.BatchService;
import com.ldbbank.autodebit_svc.service.NotiWhatAppService;
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

    private final NotiWhatAppService notiWhatAppServiceImpl;

   // @Scheduled(cron = "0 08 28 * * *")
    @PostMapping("/startWhatAppRetry")
    public ResponseEntity<?> reportEdlDaily() {
        // Record the start time
        DataResponse2 dataResponse = new DataResponse2();
        dataResponse.setDataResponse(notiWhatAppServiceImpl.mapMsg());
        try {
            return ResponseEntity.ok(dataResponse);
        } catch (Exception ex) {
            dataResponse.setStatus("05");
            dataResponse.setMessage("ຂໍ້ມູນບໍ່ຖືກຕ້ອງ");
            log.error("Exception occurred: ", ex);
        }
        return ResponseEntity.ok(dataResponse);
    }

}

