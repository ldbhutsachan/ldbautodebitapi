package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.excaption.ApiResponse;
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

    @GetMapping("/dashboard")
    public ApiResponse<?> getDashboard() {
        try {
            // ເພີ້ມ ລາຍງານປະຈຳເດືອນ ຈາກ transaction (monthly report added to DashboardDto)
            DashboardDto data = dashboardService.getDashboard();
            return new ApiResponse<>("00", "Success", data);
        } catch (Exception ex) {
            log.error("Error fetching dashboard data: {}", ex.getMessage(), ex);
            return new ApiResponse<>("05", "Failed to load dashboard data: " + ex.getMessage(), null);
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
}
