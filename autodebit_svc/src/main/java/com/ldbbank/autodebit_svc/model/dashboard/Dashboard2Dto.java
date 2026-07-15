package com.ldbbank.autodebit_svc.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Realtime account monitor report ("dashboard2"), modeled after the branch/currency/
 * account-type layout of ລາຍງານຍອດເງິນເຫຼືອທ້າຍບັນຊີ (Book1.xlsx).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard2Dto {

    // ──────────────────────────────
    // Account detail (realtime from T24, CATEGORY mapped to its ACCOUNT_TYPE description)
    // ──────────────────────────────
    private List<AccountRealtimeDto> accounts;

    // ──────────────────────────────
    // ຍອດລວມກິບ / ໂດລາ / ບາດ / ຢວນ — raw total balance per currency, no conversion
    // ──────────────────────────────
    private List<CurrencyTotal> currencyTotals;

    // ──────────────────────────────
    // ລາຍລະອຽດສາຂາ ເເລະ ຍອດຍົກມາທຽບໃສ່ກີບ — per-branch balances by currency + LAK equivalent
    // ──────────────────────────────
    private List<BranchSummary> branchSummaries;

    // ──────────────────────────────
    // Per account-type (CATEGORY → TYPE_NAME) totals in LAK equivalent
    // ──────────────────────────────
    private List<CategorySummary> categorySummaries;

    // ──────────────────────────────
    // ລວມຍອດເງິນທັງໝົດ (ທຽບເທົ່າກີບ) — grand total across all accounts, LAK equivalent
    // ──────────────────────────────
    private BigDecimal grandTotalLak;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrencyTotal {
        private String ccy;
        private BigDecimal totalBalance;
        private long accountCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BranchSummary {
        private String branchCode;
        private String branchName;
        private long accountCount;
        private Map<String, BigDecimal> balanceByCcy;
        private BigDecimal totalLakEquivalent;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategorySummary {
        private String category;
        private String categoryName;
        private long accountCount;
        private BigDecimal totalLakEquivalent;
    }
}
