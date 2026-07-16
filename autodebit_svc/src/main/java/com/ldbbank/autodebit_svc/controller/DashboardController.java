package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.dashboard.Dashboard2Dto;
import com.ldbbank.autodebit_svc.model.dashboard.DashboardDto;
import com.ldbbank.autodebit_svc.model.dashboard.MonthlyReportChartDto;
import com.ldbbank.autodebit_svc.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dash")
public class DashboardController {

    private final DashboardService dashboardService;


    /**
     * @param month "all" (default) or 1-12
     * @param year  "all" (default) or a calendar year, e.g. 2026, 2027 ...
     */
    @GetMapping("/dashboard")
    public ApiResponse<?> getDashboard(
            @RequestParam(value = "month", required = false, defaultValue = "all") String month,
            @RequestParam(value = "year", required = false, defaultValue = "all") String year) {
        try {
            Integer monthFilter = parseFilterParam(month, 1, 12, "month");
            Integer yearFilter = parseFilterParam(year, 2000, 2100, "year");

            // ເພີ້ມ ລາຍງານປະຈຳເດືອນ ຈາກ transaction (monthly report added to DashboardDto)
            DashboardDto data = dashboardService.getDashboard(monthFilter, yearFilter);
            return new ApiResponse<>("00", "Success", data);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>("01", ex.getMessage(), null);
        } catch (Exception ex) {
            log.error("Error fetching dashboard data: {}", ex.getMessage(), ex);
            return new ApiResponse<>("05", "Failed to load dashboard data: " + ex.getMessage(), null);
        }
    }

    /**
     * Parses a filter query param that accepts either the literal "all" (no filtering
     * on that dimension) or an integer within [min, max].
     */
    private Integer parseFilterParam(String value, int min, int max, String paramName) {
        if (value == null || "all".equalsIgnoreCase(value.trim())) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < min || parsed > max) {
                throw new IllegalArgumentException(paramName + " must be 'all' or between " + min + " and " + max);
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(paramName + " must be 'all' or a valid number");
        }
    }

    /**
     * Monthly report data structured for ApexCharts mixed chart (Vue component).
     * Returns {@code labels} (ISO date strings) and {@code series} per currency
     * with types column/area/line — directly consumable by Vue ApexCharts.
     */
    @GetMapping("/monthly-report-chart")
    public ApiResponse<?> getMonthlyReportChart() {
        try {
            MonthlyReportChartDto data = dashboardService.getMonthlyReportChart();
            return new ApiResponse<>("00", "Success", data);
        } catch (Exception ex) {
            log.error("Error fetching monthly report chart data: {}", ex.getMessage(), ex);
            return new ApiResponse<>("05", "Failed to load monthly report chart data: " + ex.getMessage(), null);
        }
    }

    /**
     * Dashboard2 — realtime account monitor (ລາຍງານຍອດເງິນເຫຼືອທ້າຍບັນຊີ).
     * Pulls live balances from T24 for accounts registered for auto-debit and returns
     * account detail plus branch/currency/account-type summaries.
     *
     * @param branchCode optional filter by branch code (AUTO_DEBIT_ACCOUNT_MAPPER.BRANCH_CODE), or "all"
     * @param accountNo  optional filter by a single account number, or "all"
     */
    @GetMapping("/dashboard2")
    public ApiResponse<?> getDashboard2(
            @RequestParam(value = "branchCode", required = false, defaultValue = "all") String branchCode,
            @RequestParam(value = "accountNo", required = false, defaultValue = "all") String accountNo) {
        try {
            Dashboard2Dto data = dashboardService.getDashboard2(branchCode, accountNo);
            return new ApiResponse<>("00", "Success", data);
        } catch (Exception ex) {
            log.error("Error fetching dashboard2 data: {}", ex.getMessage(), ex);
            return new ApiResponse<>("05", "Failed to load dashboard2 data: " + ex.getMessage(), null);
        }
    }
}
