package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.BranchDbEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.AutoDebitBranchReq;
import com.ldbbank.autodebit_svc.service.BranchService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unitl.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/branch")
public class BranchController {
    private final BranchService branchService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AutoDebitBranchReq body, HttpServletRequest request) {
        try {
            String userName = extractUserName(request);
            if (userName == null) {
                return ResponseEntity.status(401).body(new ApiResponse<>("01", "Unauthorized: Missing or invalid token", null));
            }

            BranchDbEntity saved = branchService.createBranch(body, userName);
            if (saved == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null));
            }
            return ResponseEntity.ok(new ApiResponse<>("00", "ບັນທືກຂໍ້ມູນ ສໍາເລັດ !!!", saved));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດບັນທືກ ຂໍ້ມູນໄດ້ !!! : " + ex.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AutoDebitBranchReq body, HttpServletRequest request) {
        try {
            String userName = extractUserName(request);
            if (userName == null) {
                return ResponseEntity.status(401).body(new ApiResponse<>("01", "Unauthorized: Missing or invalid token", null));
            }

            return branchService.updateBranch(id, body, userName)
                    .map(b -> ResponseEntity.ok(new ApiResponse<>("00", "ອັບເດດຂໍ້ມູນ ສໍາເລັດ !!!", b)))
                    .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null)));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດອັບເດດ ຂໍ້ມູນໄດ້ !!! : " + ex.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            boolean deleted = branchService.deleteBranch(id);
            if (!deleted) {
                return ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null));
            }
            return ResponseEntity.ok(new ApiResponse<>("00", "ລືບຂໍ້ມູນ ສໍາເລັດ !!!", null));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດລືບ ຂໍ້ມູນໄດ້ !!! : " + ex.getMessage(), null));
        }
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body, HttpServletRequest request) {
        try {
            String userName = extractUserName(request);
            if (userName == null) {
                return ResponseEntity.status(401).body(new ApiResponse<>("01", "Unauthorized: Missing or invalid token", null));
            }

            String status = body.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>("02", "Status is required", null));
            }

            return branchService.updateStatus(id, status, userName)
                    .map(b -> ResponseEntity.ok(new ApiResponse<>("00", "ອັບເດດສະຖານະ ສໍາເລັດ !!!", b)))
                    .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null)));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດອັບເດດສະຖານະໄດ້ !!! : " + ex.getMessage(), null));
        }
    }

    @GetMapping("/listBranch")
    public ResponseEntity<?> list() {
        try {
            return ResponseEntity.ok(new ApiResponse<>("00", "OK", branchService.listAllWithCompany()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດດຶງຂໍ້ມູນໄດ້ !!! : " + ex.getMessage(), null));
        }
    }

    /**
     * Extract username from the bearer token in the Authorization header.
     */
    private String extractUserName(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        Claims claims = JwtTokenUtil.parseToken(token);
        return claims.get("userName", String.class);
    }
}
