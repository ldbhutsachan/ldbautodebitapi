package com.ldbbank.autodebit_svc.controller;


import com.ldbbank.autodebit_svc.model.whatapp.DataResponse2;
import com.ldbbank.autodebit_svc.service.NotiWhatAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchWhatAppController {

   private final NotiWhatAppService notiWhatAppServiceImpl;

    @Scheduled(cron = "0 19 17 * * *")
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
