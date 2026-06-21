package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountMapperEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.AutoDebitAccountMapperReq;
import com.ldbbank.autodebit_svc.service.AutoDebitAccountMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/account-map")
public class AutoDebitAccountMapperController {

    private final AutoDebitAccountMapperService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AutoDebitAccountMapperReq body) {
        AutoDebitAccountMapperEntity saved = service.save(body);
        try {
        if (saved == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("00", "ບັນທືກຂໍ້ມູນ ສໍາເລັດ !!!",saved));
    } catch (Exception ex) {
        return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດບັນທືກ ຂໍ້ມູນໄດ້ !!!  : " + ex.getMessage(), null));
    }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AutoDebitAccountMapperEntity body) {
        return service.update(id, body)
                .map(e -> ResponseEntity.ok(new ApiResponse<>("00", "Updated", e)))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null)));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer status = body.get("status");
        if (status == null) return ResponseEntity.badRequest().body(new ApiResponse<>("02", "missing status", null));
        boolean ok = service.updateStatus(id, status);
        if (!ok) return ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null));
        return ResponseEntity.ok(new ApiResponse<>("00", "Status updated", null));
    }


    @GetMapping("/accountMaper")
    public ResponseEntity<?> listByStatus() {
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findMapAccountAll()));
    }
}
