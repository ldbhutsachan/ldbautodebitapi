package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.service.AutoDebitAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/account")
public class AutoDebitAccountController {

    private final AutoDebitAccountService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AutoDebitAccountEntity body) {
        AutoDebitAccountEntity saved = service.save(body);
        return ResponseEntity.ok(new ApiResponse<>("00", "Created", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AutoDebitAccountEntity body) {
        return service.update(id, body)
                .map(e -> ResponseEntity.ok(new ApiResponse<>("00", "Updated", e)))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean ok = service.delete(id);
        if (!ok) return ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null));
        return ResponseEntity.ok(new ApiResponse<>("00", "Deleted", null));
    }

    @GetMapping
    public ResponseEntity<?> listByStatus(@RequestParam(required = false) String status) {
        if (status == null) return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findAllWithCompany()));
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findByStatusWithCompany(status)));
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findAllWithCompany()));
    }

}
