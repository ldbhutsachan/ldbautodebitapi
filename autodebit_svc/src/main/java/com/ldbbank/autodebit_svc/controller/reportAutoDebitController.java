package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRegisterEntity;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.reportAutoDebit.ReportReq;
import com.ldbbank.autodebit_svc.service.rpAutoDebitService.VvRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/debit")
public class reportAutoDebitController {
    private final VvRegisterService registerService;

    @PostMapping("/vv-register")
    public ApiResponse<List<VvRegisterEntity>> getRegister(@RequestBody ReportReq request) {
        List<VvRegisterEntity> data = registerService.getRegisterData(request);
        return new ApiResponse<>("00", "Success", data);
    }
}
