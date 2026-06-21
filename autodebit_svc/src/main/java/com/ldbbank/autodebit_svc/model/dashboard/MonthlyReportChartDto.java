package com.ldbbank.autodebit_svc.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportChartDto {
    private List<String> labels;
    private List<ChartSeries> series;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChartSeries {
        private String name;
        private String type;
        private List<Double> data;
    }
}
