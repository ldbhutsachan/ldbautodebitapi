package com.ldbbank.autodebit_svc.model.reportAutoDebit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchReportDto {
    private Long branchNo;
    private String branchName;
    private List<CurrencyBreakdown> currencies;
    private Double grandTotal;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrencyBreakdown {
        private String currency;
        private Double totalAmount;
    }
}
