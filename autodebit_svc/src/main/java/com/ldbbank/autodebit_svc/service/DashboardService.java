package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.*;
import com.ldbbank.autodebit_svc.db.autodebit.repository.*;
import com.ldbbank.autodebit_svc.db.t24.entity.AccountRealtimeEntity;
import com.ldbbank.autodebit_svc.db.t24.entity.ExchangeRateEntity;
import com.ldbbank.autodebit_svc.db.t24.repository.AccountRealtimeRepository;
import com.ldbbank.autodebit_svc.model.corebank.APIResponse;
import com.ldbbank.autodebit_svc.model.dashboard.AccountRealtimeDto;
import com.ldbbank.autodebit_svc.model.dashboard.Dashboard2Dto;
import com.ldbbank.autodebit_svc.model.dashboard.DashboardDto;
import com.ldbbank.autodebit_svc.model.dashboard.MonthlyReportChartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class DashboardService {
    private final AccountLimitDbRepository  accountLimitDbRepository;
    private final AccountLimitTxnRepository  accountLimitTxnRepository;

    private final CorebankService corebankService;

    private final VvRegisterRepository vvRegisterRepository;
    private final VvRpTxnRepository vvRpTxnRepository;
    private final VvTransactionRepository vvTransactionRepository;
    private final AutoDebitAccountRepository autoDebitAccountRepository;
    private final AutoDebitCompanyRepository autoDebitCompanyRepository;
    private final BranchDbRepository branchDbRepository;
    private final AutoDebitAccountMapperRepository autoDebitAccountMapperRepository;
    private final AutoDebitAccountTxnRepository autoDebitAccountTxnRepository;
    private final UserDbRepository userDbRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountRealtimeRepository accountRealtimeRepository;


    /**
     * @param month 1-12 to filter to a specific month, or {@code null} for all months
     * @param year  e.g. 2026 to filter to a specific year, or {@code null} for all years
     */
    public DashboardDto getDashboard(Integer month, Integer year) {
        DashboardDto dto = new DashboardDto();

        // ── Fetch full datasets once and filter by month/year (null = no filter on that dimension) ──
        List<VvRpTxnEntity> allRpTxns = filterByMonthYear(
                vvRpTxnRepository.findAll(), VvRpTxnEntity::getTxnDate, month, year);
        List<VvTransactionEntity> allVTxns = filterByMonthYear(
                vvTransactionRepository.findAll(), VvTransactionEntity::getTxnDate, month, year);
        List<AutoDebitAccountTxnEntity> allAutoDebitTxns = filterByMonthYear(
                autoDebitAccountTxnRepository.findAll(), AutoDebitAccountTxnEntity::getTxnDate, month, year);

        // ── Overview counts (transaction-based counts reflect the month/year filter; master-data counts don't) ──
        dto.setTotalRegistrations(vvRegisterRepository.count());
        dto.setTotalTransactions((long) allVTxns.size());
        dto.setTotalReportTransactions((long) allRpTxns.size());
        dto.setTotalAccounts(autoDebitAccountRepository.count());
        dto.setTotalCompanies(autoDebitCompanyRepository.count());
        dto.setTotalBranches(branchDbRepository.count());
        dto.setTotalAccountMappers(autoDebitAccountMapperRepository.count());
        dto.setTotalAutoDebitTxns((long) allAutoDebitTxns.size());
        dto.setTotalUsers(userDbRepository.count());

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


    private <T> List<T> filterByMonthYear(List<T> items, java.util.function.Function<T, LocalDate> dateExtractor,
                                           Integer month, Integer year) {
        if (month == null && year == null) {
            return items;
        }
        return items.stream()
                .filter(item -> {
                    LocalDate date = dateExtractor.apply(item);
                    if (date == null) return false;
                    if (year != null && date.getYear() != year) return false;
                    if (month != null && date.getMonthValue() != month) return false;
                    return true;
                })
                .collect(Collectors.toList());
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

    /**
     * Monthly report shown in LAK (ກີບ) only.
     * LAK transactions are summed as-is; foreign-currency transactions (USD/THB/CNY)
     * are converted to their LAK equivalent using the T24 exchange buy rate before
     * being added into the same monthly total.
     */
    private List<DashboardDto.MonthlyReport> buildMonthlyReports(
            List<VvRpTxnEntity> rpTxns,
            List<VvTransactionEntity> vTxns,
            List<AutoDebitAccountTxnEntity> autoDebitTxns) {

        // Fetch current exchange rates once per foreign currency (avoid one DB call per transaction)
        Map<String, BigDecimal> buyRates = fetchBuyRates();

        // Group LAK-equivalent totals by year-month
        Map<String, Double> totalsByMonth = new LinkedHashMap<>();
        Map<String, Long> countsByMonth = new LinkedHashMap<>();

        // LAK from rpTxns — no conversion needed
        rpTxns.stream()
                .filter(t -> "LAK".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> addToMonthlyTotal(totalsByMonth, countsByMonth, t.getTxnDate(), t.getToAcctAmount()));

        // USD from vTxns — converted to LAK
        vTxns.stream()
                .filter(t -> "USD".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> addToMonthlyTotal(totalsByMonth, countsByMonth, t.getTxnDate(),
                        convertToLak(t.getToAcctAmount(), "USD", buyRates)));

        // THB from autoDebitTxns — converted to LAK
        autoDebitTxns.stream()
                .filter(t -> "THB".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> addToMonthlyTotal(totalsByMonth, countsByMonth, t.getTxnDate(),
                        convertToLak(t.getToAcctAmount().doubleValue(), "THB", buyRates)));

        // CNY from autoDebitTxns — converted to LAK
        autoDebitTxns.stream()
                .filter(t -> "CNY".equalsIgnoreCase(t.getToAcctCcy())
                        && t.getTxnDate() != null && t.getToAcctAmount() != null)
                .forEach(t -> addToMonthlyTotal(totalsByMonth, countsByMonth, t.getTxnDate(),
                        convertToLak(t.getToAcctAmount().doubleValue(), "CNY", buyRates)));

        // Build and sort the result list (most recent month first)
        return totalsByMonth.entrySet().stream()
                .map(entry -> {
                    String[] dateParts = entry.getKey().split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    long count = countsByMonth.getOrDefault(entry.getKey(), 0L);
                    return new DashboardDto.MonthlyReport(year, month, count, entry.getValue(), "LAK");
                })
                .sorted((a, b) -> {
                    if (a.getYear() != b.getYear()) return Integer.compare(b.getYear(), a.getYear());
                    return Integer.compare(b.getMonth(), a.getMonth());
                })
                .collect(Collectors.toList());
    }

    private void addToMonthlyTotal(Map<String, Double> totalsByMonth, Map<String, Long> countsByMonth,
                                    LocalDate txnDate, double lakAmount) {
        String key = txnDate.getYear() + "-" + String.format("%02d", txnDate.getMonthValue());
        totalsByMonth.merge(key, lakAmount, Double::sum);
        countsByMonth.merge(key, 1L, Long::sum);
    }

    private double convertToLak(double amount, String currency, Map<String, BigDecimal> buyRates) {
        BigDecimal rate = buyRates.get(currency);
        if (rate == null) {
            return 0.0;
        }
        return amount * rate.doubleValue();
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

    /**
     * Dashboard2 — realtime account monitor.
     * <p>
     * Pulls live balances straight from T24 (FBNK_ACCOUNT via XMLTABLE) for accounts
     * registered for auto-debit, then maps each account's raw T24 CATEGORY code to its
     * description in ACCOUNT_TYPE. That mapping is done here in application code rather
     * than in SQL because T24 and the app database are two separate physical Oracle
     * databases (see {@code SecondDataSourceConfig} / {@code PrimaryDataSourceConfig}).
     *
     * @param branchCode optional filter on T24 CO_CODE, or {@code null} for all branches
     * @param accountNo  optional filter on a single account number, or {@code null} for all accounts
     */
    public Dashboard2Dto getDashboard2(String branchCode, String accountNo) {
        List<AutoDebitAccountMapperEntity> mappers = autoDebitAccountMapperRepository.findByStatus(1).stream()
                .filter(m -> m.getFromAcctNo() != null)
                .filter(m -> accountNo == null || accountNo.isBlank() || m.getFromAcctNo().equals(accountNo))
                .collect(Collectors.toList());

        List<String> accountNos = mappers.stream()
                .map(AutoDebitAccountMapperEntity::getFromAcctNo)
                .distinct()
                .collect(Collectors.toList());

        if (accountNos.isEmpty()) {
            return new Dashboard2Dto(Collections.emptyList(), Collections.emptyList(),
                    Collections.emptyList(), Collections.emptyList(), BigDecimal.ZERO);
        }
        // ── check limit: reject once this company has hit its configured query quota ──
        checkAccountLimit(mappers.get(0).getPartnerName());

        // ── branchCode per account comes from AUTO_DEBIT_ACCOUNT_MAPPER, not T24 CO_CODE ──
        Map<String, String> branchCodeByAccountNo = mappers.stream()
                .filter(m -> m.getBranchCode() != null)
                .collect(Collectors.toMap(AutoDebitAccountMapperEntity::getFromAcctNo,
                        AutoDebitAccountMapperEntity::getBranchCode, (a, b) -> a));
        Map<Long, String> branchNameByNo = fetchBranchNames(branchCodeByAccountNo.values());

        List<AccountRealtimeEntity> realtimeAccounts = fetchRealtimeAccounts(accountNos, branchCode);

        Map<String, String> categoryNameByCode = fetchCategoryNames(realtimeAccounts);
        Map<String, BigDecimal> buyRates = fetchBuyRates();
        //accountDtos
        List<AccountRealtimeDto> accountDtos = buildAccountDtos(realtimeAccounts, categoryNameByCode, branchCodeByAccountNo, branchNameByNo);
        //currencyTotals
        List<Dashboard2Dto.CurrencyTotal> currencyTotals = buildCurrencyTotals(realtimeAccounts);
        //branchSummaries
        List<Dashboard2Dto.BranchSummary> branchSummaries = buildDashboard2BranchSummaries(realtimeAccounts, branchCodeByAccountNo, branchNameByNo, buyRates);
        //categorySummaries
        List<Dashboard2Dto.CategorySummary> categorySummaries = buildCategorySummaries(realtimeAccounts, categoryNameByCode, buyRates);
        BigDecimal grandTotalLak = totalLakEquivalent(realtimeAccounts, buyRates);

        saveAccountLimit(mappers.get(0));

        return new Dashboard2Dto(accountDtos, currencyTotals, branchSummaries, categorySummaries, grandTotalLak);
    }

    // ── reject the query once this company has reached its configured ACCOUNT_LIMIT.AMT quota
    //    of dashboard2 calls (tracked as row-count in ACCOUNT_LIMT_TXN) ──
    private void checkAccountLimit(String companyId) {
        accountLimitDbRepository.findByCompanyId(companyId).ifPresent(limitConfig -> {
            long limit = parseLimitAmt(limitConfig.getAmt());
            if (limit < 0) {
                return;
            }
            long usedCount = accountLimitTxnRepository.countByCompanyId(companyId);
            if (usedCount >= limit) {
                throw new IllegalStateException("ທ່ານຕິດລີມິດໃນການເບີ່ງຂໍ້ມູນບັນຊີຂອງທ່ານ !!! ກະລຸນາເເຈ້ງ ຜູ້ຄຸ້ມຄອງລະບົບເພຶ່ອເພີ້ມຈໍານວນຄັ້ງໃນການເບີ່ງ !!! " + companyId);
            }
        });
    }

    private long parseLimitAmt(String amt) {
        try {
            return Long.parseLong(amt.trim());
        } catch (NumberFormatException | NullPointerException ex) {
            log.warn("Invalid ACCOUNT_LIMIT.AMT value '{}', limit check skipped", amt);
            return -1;
        }
    }

    private void saveAccountLimit(AutoDebitAccountMapperEntity mapper) {
        String companyId = mapper.getPartnerName();
        String companyName = autoDebitCompanyRepository.findById(Long.valueOf(companyId))
                .map(AutoDebitCompanyEntity::getCompanyName)
                .orElse(null);

        AccountLimitTxnDbEntity entity = new AccountLimitTxnDbEntity();
        entity.setCompanyId(companyId);
        entity.setCompanyName(companyName);
        entity.setTimeNow(LocalDateTime.now());
        entity.setTimeCheck(LocalDate.now());
        accountLimitTxnRepository.save(entity);
    }


    // ── ຄົ້ນຫາເທື່ອລະບັນຊີເເລ້ວເອົາມາລວມກັນ — query T24 one account at a time, then merge ──
    private List<AccountRealtimeEntity> fetchRealtimeAccounts(List<String> accountNos, String branchCode) {
        List<AccountRealtimeEntity> realtimeAccounts = new ArrayList<>();
        for (String accNo : accountNos) {
            realtimeAccounts.addAll(accountRealtimeRepository.findAccountsRealtimeByAccNo(accNo));
        }
        return realtimeAccounts.stream()
                .filter(a -> branchCode == null || branchCode.isBlank() || branchCode.equalsIgnoreCase(a.getBranchCode()))
                .collect(Collectors.toList());
    }

    // ── Map CATEGORY -> TYPE_NAME (application-side join across the two databases) ──
    private Map<String, String> fetchCategoryNames(List<AccountRealtimeEntity> realtimeAccounts) {
        List<String> categoryCodes = realtimeAccounts.stream()
                .map(AccountRealtimeEntity::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return categoryCodes.isEmpty()
                ? Collections.emptyMap()
                : accountTypeRepository.findByCodeIn(categoryCodes).stream()
                        .collect(Collectors.toMap(AccountTypeDbEntity::getCode, AccountTypeDbEntity::getTypeName, (a, b) -> a));
    }

    private List<AccountRealtimeDto> buildAccountDtos(List<AccountRealtimeEntity> realtimeAccounts,
                                                        Map<String, String> categoryNameByCode,
                                                        Map<String, String> branchCodeByAccountNo,
                                                        Map<Long, String> branchNameByNo) {
        return realtimeAccounts.stream()
                .map(a -> {
                    String branchCode = branchCodeByAccountNo.get(a.getAccountNo());
                    return new AccountRealtimeDto(
                            a.getAccountNo(),
                            a.getCif(),
                            a.getCategory(),
                            categoryNameByCode.get(a.getCategory()),
                            a.getAccountName(),
                            a.getAccountOfficer(),
                            branchCode,
                            resolveBranchName(branchCode, branchNameByNo),
                            a.getCcy(),
                            a.getBalance(),
                            a.getInactiveFlag(),
                            a.getOpeningDate()
                    );
                })
                .collect(Collectors.toList());
    }

    // ── ຍອດລວມກິບ/ໂດລາ/ບາດ/ຢວນ — raw total per currency ──
    private List<Dashboard2Dto.CurrencyTotal> buildCurrencyTotals(List<AccountRealtimeEntity> realtimeAccounts) {
        return realtimeAccounts.stream()
                .filter(a -> a.getCcy() != null)
                .collect(Collectors.groupingBy(AccountRealtimeEntity::getCcy, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(e -> new Dashboard2Dto.CurrencyTotal(e.getKey(), sumBalances(e.getValue()), e.getValue().size()))
                .collect(Collectors.toList());
    }

    // ── Group key used for accounts with no AUTO_DEBIT_ACCOUNT_MAPPER.branchCode, so they still
    //    show up in branchSummaries instead of silently vanishing from the report. ──
    private static final String UNASSIGNED_BRANCH_CODE = "UNASSIGNED";

    // ── ລາຍລະອຽດສາຂາ ເເລະ ຍອດຍົກມາທຽບໃສ່ກີບ — per branch, by currency + LAK equivalent ──
    // branchCode/branchName come from AUTO_DEBIT_ACCOUNT_MAPPER / AUTO_DEBIT_BRANCH (app's own
    // branch numbering), not from T24 CO_CODE, since accounts are grouped by how they were
    // registered for auto-debit rather than by their live T24 branch.
    // Accounts whose mapper row has no branchCode are grouped under UNASSIGNED_BRANCH_CODE
    // rather than dropped, so branchSummaries totals always reconcile with grandTotalLak.
    private List<Dashboard2Dto.BranchSummary> buildDashboard2BranchSummaries(
            List<AccountRealtimeEntity> realtimeAccounts, Map<String, String> branchCodeByAccountNo,
            Map<Long, String> branchNameByNo, Map<String, BigDecimal> buyRates) {

        Map<String, List<AccountRealtimeEntity>> accountsByBranch = realtimeAccounts.stream()
                .collect(Collectors.groupingBy(
                        a -> branchCodeByAccountNo.getOrDefault(a.getAccountNo(), UNASSIGNED_BRANCH_CODE),
                        LinkedHashMap::new, Collectors.toList()));

        return accountsByBranch.entrySet().stream()
                .map(e -> new Dashboard2Dto.BranchSummary(
                        e.getKey(),
                        UNASSIGNED_BRANCH_CODE.equals(e.getKey()) ? null : resolveBranchName(e.getKey(), branchNameByNo),
                        e.getValue().size(),
                        balanceByCcy(e.getValue()),
                        totalLakEquivalent(e.getValue(), buyRates)
                ))
                .collect(Collectors.toList());
    }

    // ── Look up AUTO_DEBIT_BRANCH names for a set of branch codes in one query ──
    private Map<Long, String> fetchBranchNames(Collection<String> branchCodes) {
        List<Long> branchNos = branchCodes.stream()
                .map(this::parseBranchNo)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return branchNos.isEmpty()
                ? Collections.emptyMap()
                : branchDbRepository.findByBranchNoIn(branchNos).stream()
                        .collect(Collectors.toMap(BranchDbEntity::getBranchNo, BranchDbEntity::getBranchName, (a, b) -> a));
    }

    private String resolveBranchName(String branchCode, Map<Long, String> branchNameByNo) {
        if (branchCode == null) {
            return null;
        }
        Long branchNo = parseBranchNo(branchCode);
        String branchName = branchNo != null ? branchNameByNo.get(branchNo) : null;
        if (branchName == null) {
            log.warn("No AUTO_DEBIT_BRANCH match for branchCode '{}', branchName will be blank", branchCode);
        }
        return branchName;
    }

    // ── Per account-type (CATEGORY) totals in LAK equivalent ──
    private List<Dashboard2Dto.CategorySummary> buildCategorySummaries(
            List<AccountRealtimeEntity> realtimeAccounts, Map<String, String> categoryNameByCode,
            Map<String, BigDecimal> buyRates) {

        return realtimeAccounts.stream()
                .filter(a -> a.getCategory() != null)
                .collect(Collectors.groupingBy(AccountRealtimeEntity::getCategory, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(e -> new Dashboard2Dto.CategorySummary(
                        e.getKey(),
                        categoryNameByCode.get(e.getKey()),
                        e.getValue().size(),
                        totalLakEquivalent(e.getValue(), buyRates)
                ))
                .collect(Collectors.toList());
    }

    // ── Exchange rates for LAK-equivalent totals ──
    private Map<String, BigDecimal> fetchBuyRates() {
        Map<String, BigDecimal> buyRates = new HashMap<>();
        for (String ccy : List.of("USD", "THB", "CNY")) {
            APIResponse<ExchangeRateEntity> rate = corebankService.getExchangeRate(ccy);
            if (rate.getData() != null && rate.getData().getBuyRate() != null) {
                buyRates.put(ccy, rate.getData().getBuyRate());
            } else {
                log.warn("No exchange rate found for {}, excluded from LAK-equivalent totals", ccy);
            }
        }
        return buyRates;
    }

    private Long parseBranchNo(String branchCode) {
        try {
            return Long.valueOf(branchCode.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private BigDecimal sumBalances(List<AccountRealtimeEntity> accounts) {
        return accounts.stream().map(a -> nvl(a.getBalance())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, BigDecimal> balanceByCcy(List<AccountRealtimeEntity> accounts) {
        return accounts.stream()
                .filter(a -> a.getCcy() != null)
                .collect(Collectors.groupingBy(
                        AccountRealtimeEntity::getCcy,
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, a -> nvl(a.getBalance()), BigDecimal::add)
                ));
    }

    private BigDecimal totalLakEquivalent(List<AccountRealtimeEntity> accounts, Map<String, BigDecimal> buyRates) {
        return accounts.stream()
                .map(a -> toLak(a.getCcy(), nvl(a.getBalance()), buyRates))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal toLak(String ccy, BigDecimal amount, Map<String, BigDecimal> buyRates) {
        if (ccy == null) return BigDecimal.ZERO;
        if ("LAK".equalsIgnoreCase(ccy)) return amount;
        BigDecimal rate = buyRates.get(ccy.toUpperCase());
        return rate != null ? amount.multiply(rate) : BigDecimal.ZERO;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
