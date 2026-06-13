package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.service.AutoDebitCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/company")
public class AutoDebitCompanyController {

    private final AutoDebitCompanyService service;
    private final com.ldbbank.autodebit_svc.service.MediaUploadService mediaUploadService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AutoDebitCompanyEntity body) {
        AutoDebitCompanyEntity saved = service.save(body);
        return ResponseEntity.ok(new ApiResponse<>("00", "Created", saved));
    }

    // multipart create: accepts company JSON as 'company' part and optional file as 'file' part
    @PostMapping(path = "/multipart", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createWithFile(@RequestPart("company") AutoDebitCompanyEntity body,
                                            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                var info = mediaUploadService.store(file, "companies");
                body.setImagePath(info.get("path"));
                body.setImageName(info.get("name"));
            }
            AutoDebitCompanyEntity saved = service.save(body);
            return ResponseEntity.ok(new ApiResponse<>("00", "Created", saved));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("02", "Create failed: " + ex.getMessage(), null));
        }
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            var info = mediaUploadService.store(file, "companies");
            boolean ok = service.updateImage(id, info.get("path"), info.get("name"));
            if (!ok) return ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null));
            return ResponseEntity.ok(new ApiResponse<>("00", "Image updated", info));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("02", "Upload failed: " + ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AutoDebitCompanyEntity body) {
        return service.update(id, body)
                .map(e -> ResponseEntity.ok(new ApiResponse<>("00", "Updated", e)))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null)));
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        boolean ok = service.disable(id);
        if (!ok) return ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null));
        return ResponseEntity.ok(new ApiResponse<>("00", "Disabled", null));
    }

    @GetMapping
    public ResponseEntity<?> listByStatus(@RequestParam(required = false) String status) {
        if (status == null) return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findAll()));
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findByStatus(status)));
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findAll()));
    }
}
