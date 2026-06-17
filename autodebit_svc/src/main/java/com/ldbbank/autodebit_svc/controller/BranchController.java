package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.BranchDbEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.AutoDebitBranchReq;
import com.ldbbank.autodebit_svc.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/branch")
public class BranchController {
    private final BranchService branchService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AutoDebitBranchReq body) {
        BranchDbEntity saved = branchService.createBranch(body);
        try {
            if (saved == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null));
            }
            return ResponseEntity.ok(new ApiResponse<>("00", "ບັນທືກຂໍ້ມູນ ສໍາເລັດ !!!",saved));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດບັນທືກ ຂໍ້ມູນໄດ້ !!!  : " + ex.getMessage(), null));
        }
    }
}
