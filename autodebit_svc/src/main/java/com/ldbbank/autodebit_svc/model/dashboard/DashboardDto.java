package com.ldbbank.autodebit_svc.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

    // ──────────────────────────────
    // Overview counts
    // ──────────────────────────────
    private long totalRegistrations;
    private long totalTransactions;
    private long totalReportTransactions;
    private long totalAccounts;
    private long totalCompanies;
    private long totalBranches;
    private long totalAccountMappers;
    private long totalAutoDebitTxns;
    private long totalUsers;

    // ──────────────────────────────
    // Financial summary
    // ──────────────────────────────
    private FinancialSummary financialSummary;

    // ──────────────────────────────
    // Status breakdowns
    // ──────────────────────────────
    private Map<String, Long> registrationsByStatus;
    private Map<String, Long> rpTxnsByStatus;
    private Map<String, Long> transactionsByStatus;
    private Map<String, Long> autoDebitTxnsByStatus;

    // ──────────────────────────────
    // Branch summary
    // ──────────────────────────────
    private List<BranchSummary> branchSummaries;

    // ──────────────────────────────
    // Currency breakdown
    // ──────────────────────────────
    private List<CurrencySummary> currencySummaries;

    // ──────────────────────────────
    // Monthly report (grouped by year-month)
    // ──────────────────────────────
    private List<MonthlyReport> monthlyReports;

    // ──────────────────────────────
    // Nested classes
    // ──────────────────────────────

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinancialSummary {
        private double totalVvRpTxnAmount;
        private double totalVvTransactionAmount;
        private double totalAutoDebitTxnAmount;
        private double totalAutoDebitTxnCnyAmount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BranchSummary {
        private long branchNo;
        private String branchName;
        private long transactionCount;
        private double totalAmount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrencySummary {
        private String currency;
        private double totalAmount;
        private long transactionCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyReport {
        private int year;
        private int month;
        private long transactionCount;
        private double totalAmount;
        private String currency;
    }
}
