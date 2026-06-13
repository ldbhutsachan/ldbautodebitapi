package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountMapperEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.service.AutoDebitAccountMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/account-mapper")
public class AutoDebitAccountMapperController {

    private final AutoDebitAccountMapperService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AutoDebitAccountMapperEntity body) {
        AutoDebitAccountMapperEntity saved = service.save(body);
        return ResponseEntity.ok(new ApiResponse<>("00", "Created", saved));
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

    @GetMapping
    public ResponseEntity<?> listByStatus(@RequestParam(required = false) Integer status) {
        if (status == null) return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findByStatusWithJoin(1)));
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findByStatusWithJoin(status)));
    }
}
