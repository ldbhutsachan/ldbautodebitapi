package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.user.ClientInfo;
import com.ldbbank.autodebit_svc.service.AutoDebitCompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/company")
public class AutoDebitCompanyController {

    private final AutoDebitCompanyService service;
    private final com.ldbbank.autodebit_svc.service.MediaUploadService mediaUploadService;

    @PostMapping("/create")
    public ResponseEntity<?> saveCompanyWithImage(
            @RequestAttribute(value = "clientInfo") ClientInfo clientInfo,
            @ModelAttribute AutoDebitCompanyEntity entity,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            log.info("Saving company with image"+clientInfo.getUsername());

            Map<String, String> info = null;
            if (file != null && !file.isEmpty()) {
                info = mediaUploadService.store(file, "companies");
                entity.setImagePath(info.get("url"));
                entity.setImageName(info.get("name"));
            }
            AutoDebitCompanyEntity saved = service.save(entity);

            if (saved == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null));
            }

            return ResponseEntity.ok(new ApiResponse<>("00", "ບັນທືກຂໍ້ມູນ ສໍາເລັດ !!!", info));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດບັນທືກ ບໍລິສັດໄດ້ !!!  : " + ex.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @ModelAttribute AutoDebitCompanyEntity entity,
                                    @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        Map<String, String> info = null;
        if (file != null && !file.isEmpty()) {
            info = mediaUploadService.store(file, "companies");
            entity.setImagePath(info.get("url"));   // public URL
            entity.setImageName(info.get("name"));  // original filename
        }

        return service.update(id, entity)
                .map(e -> ResponseEntity.ok(new ApiResponse<>("00", "Updated", e)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse<>("01", "Not found", null)));
    }


    @PostMapping("/disable/{id}")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        boolean ok = service.disable(id);
        if (!ok) return ResponseEntity.status(404).body(new ApiResponse<>("01", "Not found", null));
        return ResponseEntity.ok(new ApiResponse<>("00", "Disabled", null));
    }

    @GetMapping("/listCompany")
    public ResponseEntity<?> listByStatus(@RequestParam(required = false) String status) {
        if (status == null) return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findAll()));
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findByStatus(status)));
    }

    @GetMapping("/listCompanyAll")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(new ApiResponse<>("00", "OK", service.findAll()));
    }
}
