package com.ldbbank.autodebit_svc.model.reportAutoDebit;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ReportReq {
    private LocalDate startDate;
    private LocalDate endDate;
    private String branchCode;
    private String accountNo;
}
