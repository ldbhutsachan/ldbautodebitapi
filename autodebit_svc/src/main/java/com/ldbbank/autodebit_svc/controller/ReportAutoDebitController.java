package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRegisterEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.reportAutoDebit.BranchReportDto;
import com.ldbbank.autodebit_svc.model.reportAutoDebit.ReportReq;
import com.ldbbank.autodebit_svc.service.rpAutoDebitService.VvRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/debit")
public class ReportAutoDebitController {
    private final VvRegisterService registerService;

    @PostMapping("/vv-register")
    public ApiResponse<List<VvRegisterEntity>> getRegister(@RequestBody ReportReq request) {
        List<VvRegisterEntity> data = registerService.getRegisterData(request);
        return new ApiResponse<>("00", "Success", data);
    }

    /**
     * Report breakdown by branch, with totals broken down by currency within each branch.
     * ລາຍງານເເຍກຕາມສາຂາ  ເເລະ ລວມຍອດ ເເຍກຕາມ ສະກູນ ທີຂື້ນກັບສາຂານັ້ນ
     */

    @PostMapping("/branch-report")
    public ApiResponse<List<BranchReportDto>> getBranchReport(@RequestBody ReportReq request) {
        List<BranchReportDto> data = registerService.getBranchReport(request);
        return new ApiResponse<>("00", "Success", data);
    }
}
