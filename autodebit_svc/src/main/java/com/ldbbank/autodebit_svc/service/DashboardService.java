package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.*;
import com.ldbbank.autodebit_svc.db.autodebit.repository.*;
import com.ldbbank.autodebit_svc.model.dashboard.DashboardDto;
import com.ldbbank.autodebit_svc.model.dashboard.MonthlyReportChartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DashboardService {

    private final VvRegisterRepository vvRegisterRepository;
    private final VvRpTxnRepository vvRpTxnRepository;
    private final VvTransactionRepository vvTransactionRepository;
    private final AutoDebitAccountRepository autoDebitAccountRepository;
    private final AutoDebitCompanyRepository autoDebitCompanyRepository;
    private final BranchDbRepository branchDbRepository;
    private final AutoDebitAccountMapperRepository autoDebitAccountMapperRepository;
    private final AutoDebitAccountTxnRepository autoDebitAccountTxnRepository;
    private final UserDbRepository userDbRepository;

    public DashboardDto getDashboard() {
        DashboardDto dto = new DashboardDto();

        // ── Overview counts (lightweight COUNT queries) ──
        dto.setTotalRegistrations(vvRegisterRepository.count());
        dto.setTotalTransactions(vvTransactionRepository.count());
        dto.setTotalReportTransactions(vvRpTxnRepository.count());
        dto.setTotalAccounts(autoDebitAccountRepository.count());
        dto.setTotalCompanies(autoDebitCompanyRepository.count());
        dto.setTotalBranches(branchDbRepository.count());
        dto.setTotalAccountMappers(autoDebitAccountMapperRepository.count());
        dto.setTotalAutoDebitTxns(autoDebitAccountTxnRepository.count());
        dto.setTotalUsers(userDbRepository.count());

        // ── Fetch full datasets once and reuse ──
        List<VvRpTxnEntity> allRpTxns = vvRpTxnRepository.findAll();
        List<VvTransactionEntity> allVTxns = vvTransactionRepository.findAll();
        List<AutoDebitAccountTxnEntity> allAutoDebitTxns = autoDebitAccountTxnRepository.findAll();

        // ── Financial summary ──
        dto.setFinancialSummaryClosing(buildFinancialSummaryClosing(allRpTxns, allVTxns, allAutoDebitTxns));

        // ── Financial summary ──
        dto.setFinancialSummary(buildFinancialSummary(allRpTxns, allVTxns, allAutoDebitTxns));

        // ── Status breakdowns ──
        // Build counts for each status type
        dto.setRegistrationsByStatus(buildStatusCounts(
                allRpTxns.stream()
                        .map(VvRpTxnEntity::getStatus)
                        .filter("SUCCEEDED"::equalsIgnoreCase)
                        .collect(Collectors.toList())
        ));

        dto.setRpTxnsByStatus(buildStatusCounts(
                allRpTxns.stream()
                        .map(VvRpTxnEntity::getStatus)
                        .filter("INSUFFICIENT_FUND"::equalsIgnoreCase)
                        .collect(Collectors.toList())
        ));

        dto.setTransactionsByStatus(buildStatusCounts(
                allRpTxns.stream()
                        .map(VvRpTxnEntity::getStatus)
                        .filter("Invalid response"::equalsIgnoreCase)
                        .collect(Collectors.toList())
        ));

        dto.setAutoDebitTxnsByStatus(buildStatusCounts(
                allRpTxns.stream()
                        .map(VvRpTxnEntity::getStatus)
                        .filter("TIMEOUT"::equalsIgnoreCase)
                        .collect(Collectors.toList())
        ));


        // ── Branch summary ──
        dto.setBranchSummaries(buildBranchSummaries(allRpTxns));

        // ── Currency breakdown (sum count & totalAmount by ccy from all sources) ──
        dto.setCurrencySummaries(buildCurrencySummaries(allRpTxns, allVTxns, allAutoDebitTxns));

        // ── Monthly report (grouped by year-month from all transaction sources) ──
        dto.setMonthlyReports(buildMonthlyReports(allRpTxns, allVTxns, allAutoDebitTxns));

        return dto;
    }

    private DashboardDto.FinancialSummaryClosing buildFinancialSummaryClosing(
            List<VvRpTxnEntity> rpTxns,
            List<VvTransactionEntity> vTxns,
            List<AutoDebitAccountTxnEntity> autoDebitTxns) {

        // Sum LAK from rpTxns
        double totalRpTxnLak = rpTxns.stream()
                .filter(e -> "LAK".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getBalanceAmount() != null ? e.getBalanceAmount() : 0.0)
                .sum();

        // Sum USD from vTxns
        double totalVTxnUsd = vTxns.stream()
                .filter(e -> "USD".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getBalanceAmount() != null ? e.getBalanceAmount() : 0.0)
                .sum();

        // Sum THB from autoDebitTxns
        double totalAutoDebitThbTxn = autoDebitTxns.stream()
                .filter(e -> "THB".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getBalanceAmount() != null ? e.getBalanceAmount().doubleValue() : 0.0)
                .sum();

        // Sum CNY from autoDebitTxns
        double totalAutoDebitCnyTxn = autoDebitTxns.stream()
                .filter(e -> "CNY".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getBalanceAmount() != null ? e.getBalanceAmount().doubleValue() : 0.0)
                .sum();

        return new DashboardDto.FinancialSummaryClosing(
                totalRpTxnLak,
                totalVTxnUsd,
                totalAutoDebitThbTxn,
                totalAutoDebitCnyTxn
        );
    }private DashboardDto.FinancialSummary buildFinancialSummary(
            List<VvRpTxnEntity> rpTxns,
            List<VvTransactionEntity> vTxns,
            List<AutoDebitAccountTxnEntity> autoDebitTxns) {

        // Sum LAK from rpTxns
        double totalRpTxnLak = rpTxns.stream()
                .filter(e -> "LAK".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getToAcctAmount() != null ? e.getToAcctAmount() : 0.0)
                .sum();

        // Sum USD from vTxns
        double totalVTxnUsd = vTxns.stream()
                .filter(e -> "USD".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getToAcctAmount() != null ? e.getToAcctAmount() : 0.0)
                .sum();

        // Sum THB from autoDebitTxns
        double totalAutoDebitThbTxn = autoDebitTxns.stream()
                .filter(e -> "THB".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getToAcctAmount() != null ? e.getToAcctAmount().doubleValue() : 0.0)
                .sum();

        // Sum CNY from autoDebitTxns
        double totalAutoDebitCnyTxn = autoDebitTxns.stream()
                .filter(e -> "CNY".equalsIgnoreCase(e.getToAcctCcy()))
                .mapToDouble(e -> e.getToAcctAmount() != null ? e.getToAcctAmount().doubleValue() : 0.0)
                .sum();

        return new DashboardDto.FinancialSummary(
                totalRpTxnLak,
                totalVTxnUsd,
                totalAutoDebitThbTxn,
                totalAutoDebitCnyTxn
        );
    }


    private Map<String, Long> buildStatusCounts(List<String> statuses) {
        return statuses.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        s -> s,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }


    private List<DashboardDto.BranchSummary> buildBranchSummaries(List<VvRpTxnEntity> rpTxns) {
        Map<Long, List<VvRpTxnEntity>> byBranch = rpTxns.stream()
                .filter(e -> e.getBranchNo() != null)
                .collect(Collectors.groupingBy(
                        VvRpTxnEntity::getBranchNo,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return byBranch.entrySet().stream().map(entry -> {
            Long branchNo = entry.getKey();
            List<VvRpTxnEntity> txns = entry.getValue();

            String branchName = txns.stream()
                    .map(VvRpTxnEntity::getBranchName)
                    .filter(n -> n != null && !n.isEmpty())
                    .findFirst()
                    .orElse("Unknown");

            double totalAmount = txns.stream()
                    .mapToDouble(e -> e.getFromAcctAmount() != null ? e.getFromAcctAmount() : 0.0)
                    .sum();

            return new DashboardDto.BranchSummary(branchNo, branchName, txns.size(), totalAmount);
        }).collect(Collectors.toList());
    }

    private List<DashboardDto.CurrencySummary> buildCurrencySummaries(
            List<VvRpTxnEntity> rpTxns,
            List<VvTransactionEntity> vTxns,
            List<AutoDebitAccountTxnEntity> autoDebitTxns) {

        // Target currencies in display order: LAK (ກີບ), THB (ບາດ), USD (ໂດລາ), CNY (ຢວນ)
        // Each currency reads from its designated source to avoid triple-counting
        List<String> targetCurrencies = List.of("LAK", "THB", "USD", "CNY");

        // ── Build per-currency buckets ──
        // LAK from rpTxns
        Map<String, List<Double>> grouped = new LinkedHashMap<>();
        rpTxns.stream()
                .filter(t -> "LAK".equalsIgnoreCase(t.getToAcctCcy()) && t.getToAcctAmount() != null)
                .forEach(t -> grouped.computeIfAbsent("LAK", k -> new ArrayList<>()).add(t.getToAcctAmount()));

        // USD from vTxns
        vTxns.stream()
                .filter(t -> "USD".equalsIgnoreCase(t.getToAcctCcy()) && t.getToAcctAmount() != null)
                .forEach(t -> grouped.computeIfAbsent("USD", k -> new ArrayList<>()).add(t.getToAcctAmount()));

        // THB from autoDebitTxns
        autoDebitTxns.stream()
                .filter(t -> "THB".equalsIgnoreCase(t.getToAcctCcy()) && t.getToAcctAmount() != null)
                .forEach(t -> grouped.computeIfAbsent("THB", k -> new ArrayList<>()).add(t.getToAcctAmount().doubleValue()));

        // CNY from autoDebitTxns
        autoDebitTxns.stream()
                .filter(t -> "CNY".equalsIgnoreCase(t.getToAcctCcy()) && t.getToAcctAmount() != null)
                .forEach(t -> grouped.computeIfAbsent("CNY", k -> new ArrayList<>()).add(t.getToAcctAmount().doubleValue()));

        // Build result in target currency order
        return targetCurrencies.stream()
                .map(ccy -> {
                    List<Double> amounts = grouped.getOrDefault(ccy, Collections.emptyList());

                    double total = amounts.stream().mapToDouble(Double::doubleValue).sum();
                    return new DashboardDto.CurrencySummary(ccy, total, 0);
                })
                .collect(Collectors.toList());
    }

    private List<DashboardDto.MonthlyReport> buildMonthlyReports(
            List<VvRpTxnEntity> rpTxns,
            List<VvTransactionEntity> vTxns,
            List<AutoDebitAccountTxnEntity> autoDebitTxns) {

        // Target currencies in desired display order: LAK (ກີບ), THB (ບາດ), USD (ໂດລາ), CNY (ຢວນ)
        // Each currency reads from its designated source to avoid triple-counting
        List<String> targetCurrencies = List.of("LAK", "THB", "USD", "CNY");

        // Group amounts by year-month + currency key (e.g. "2026-06|LAK")
        Map<String, List<Double>> grouped = new LinkedHashMap<>();

        // LAK from rpTxns
        rpTxns.stream()
                .filter(t -> "LAK".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String key = t.getTxnDate().getYear() + "-"
                            + String.format("%02d", t.getTxnDate().getMonthValue()) + "|LAK";
                    grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(t.getToAcctAmount());
                });

        // USD from vTxns
        vTxns.stream()
                .filter(t -> "USD".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String key = t.getTxnDate().getYear() + "-"
                            + String.format("%02d", t.getTxnDate().getMonthValue()) + "|USD";
                    grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(t.getToAcctAmount());
                });

        // THB from autoDebitTxns
        autoDebitTxns.stream()
                .filter(t -> "THB".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String key = t.getTxnDate().getYear() + "-"
                            + String.format("%02d", t.getTxnDate().getMonthValue()) + "|THB";
                    grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(t.getToAcctAmount().doubleValue());
                });

        // CNY from autoDebitTxns
        autoDebitTxns.stream()
                .filter(t -> "CNY".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String key = t.getTxnDate().getYear() + "-"
                            + String.format("%02d", t.getTxnDate().getMonthValue()) + "|CNY";
                    grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(t.getToAcctAmount().doubleValue());
                });

        // Build and sort the result list (most recent month first)
        return grouped.entrySet().stream()
                .map(entry -> {
                    String[] keyParts = entry.getKey().split("\\|");
                    String[] dateParts = keyParts[0].split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    String currency = keyParts[1];
                    List<Double> amounts = entry.getValue();
                    double total = amounts.stream().mapToDouble(Double::doubleValue).sum();
                    return new DashboardDto.MonthlyReport(year, month, amounts.size(), total, currency);
                })
                .sorted((a, b) -> {
                    if (a.getYear() != b.getYear()) return Integer.compare(b.getYear(), a.getYear());
                    if (a.getMonth() != b.getMonth()) return Integer.compare(b.getMonth(), a.getMonth());
                    return Integer.compare(
                            indexOfCurrency(a.getCurrency(), targetCurrencies),
                            indexOfCurrency(b.getCurrency(), targetCurrencies)
                    );
                })
                .collect(Collectors.toList());
    }

    private int indexOfCurrency(String currency, List<String> targetCurrencies) {
        int i = targetCurrencies.indexOf(currency);
        return i >= 0 ? i : Integer.MAX_VALUE;
    }

    /**
     * Build monthly report data structured for an ApexCharts mixed chart.
     * Returns labels (year-month) and series per currency with assigned chart types.
     * <p>
     * Chart type mapping:
     * <ul>
     *   <li>LAK (ກີບ) → column</li>
     *   <li>THB (ບາດ) → area</li>
     *   <li>USD (ໂດລາ) → line</li>
     *   <li>CNY (ຢວນ) → column</li>
     * </ul>
     */
    public MonthlyReportChartDto getMonthlyReportChart() {
        List<VvRpTxnEntity> allRpTxns = vvRpTxnRepository.findAll();
        List<VvTransactionEntity> allVTxns = vvTransactionRepository.findAll();
        List<AutoDebitAccountTxnEntity> allAutoDebitTxns = autoDebitAccountTxnRepository.findAll();

        // ── Define currencies and their chart types ──
        // Order: LAK, THB, USD, CNY
        List<String> currencies = List.of("LAK", "THB", "USD", "CNY");
        Map<String, String> chartTypes = Map.of(
                "LAK", "column",
                "THB", "area",
                "USD", "line",
                "CNY", "column"
        );

        // ── Group amounts by year-month per currency ──
        // Map: currency -> (Map: year-month -> list of amounts)
        Map<String, Map<String, List<Double>>> raw = new LinkedHashMap<>();
        for (String ccy : currencies) {
            raw.put(ccy, new LinkedHashMap<>());
        }

        // LAK from rpTxns
        allRpTxns.stream()
                .filter(t -> "LAK".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String ym = t.getTxnDate().getYear() + "-" + String.format("%02d", t.getTxnDate().getMonthValue()) + "-01";
                    raw.get("LAK").computeIfAbsent(ym, k -> new ArrayList<>()).add(t.getToAcctAmount());
                });

        // USD from vTxns
        allVTxns.stream()
                .filter(t -> "USD".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String ym = t.getTxnDate().getYear() + "-" + String.format("%02d", t.getTxnDate().getMonthValue()) + "-01";
                    raw.get("USD").computeIfAbsent(ym, k -> new ArrayList<>()).add(t.getToAcctAmount());
                });

        // THB from autoDebitTxns
        allAutoDebitTxns.stream()
                .filter(t -> "THB".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String ym = t.getTxnDate().getYear() + "-" + String.format("%02d", t.getTxnDate().getMonthValue()) + "-01";
                    raw.get("THB").computeIfAbsent(ym, k -> new ArrayList<>()).add(t.getToAcctAmount().doubleValue());
                });

        // CNY from autoDebitTxns
        allAutoDebitTxns.stream()
                .filter(t -> "CNY".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> {
                    String ym = t.getTxnDate().getYear() + "-" + String.format("%02d", t.getTxnDate().getMonthValue()) + "-01";
                    raw.get("CNY").computeIfAbsent(ym, k -> new ArrayList<>()).add(t.getToAcctAmount().doubleValue());
                });

        // ── Collect all unique year-month labels and sort ascending ──
        Set<String> allLabels = new TreeSet<>();
        for (Map<String, List<Double>> byYm : raw.values()) {
            allLabels.addAll(byYm.keySet());
        }
        List<String> labels = new ArrayList<>(allLabels);

        // ── Build series: for each currency, fill amounts matching the sorted labels ──
        // If a currency has no data for a month, use 0.0
        List<MonthlyReportChartDto.ChartSeries> seriesList = new ArrayList<>();
        for (String ccy : currencies) {
            Map<String, List<Double>> byYm = raw.get(ccy);
            List<Double> data = new ArrayList<>();
            for (String label : labels) {
                List<Double> amounts = byYm.getOrDefault(label, Collections.emptyList());
                double total = amounts.stream().mapToDouble(Double::doubleValue).sum();
                data.add(total);
            }
            seriesList.add(new MonthlyReportChartDto.ChartSeries(
                    ccy,
                    chartTypes.get(ccy),
                    data
            ));
        }

        return new MonthlyReportChartDto(labels, seriesList);
    }
}
