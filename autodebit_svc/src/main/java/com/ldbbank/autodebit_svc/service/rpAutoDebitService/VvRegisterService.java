package com.ldbbank.autodebit_svc.service.rpAutoDebitService;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRegisterEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRpTxnEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.VvRegisterRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.VvRpTxnRepository;
import com.ldbbank.autodebit_svc.model.reportAutoDebit.BranchReportDto;
import com.ldbbank.autodebit_svc.model.reportAutoDebit.ReportReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VvRegisterService {

    private final VvRegisterRepository repository;
    private final VvRpTxnRepository vvRpTxnRepository;

    public List<VvRegisterEntity> getRegisterData(ReportReq request) {
        List<VvRegisterEntity> allData = repository.findAll();

        // If accountNo is provided, search by it (match fromAcctNo or toAcctNo)
        if (request.getAccountNo() != null && !request.getAccountNo().isBlank()) {
            String acct = request.getAccountNo();
            return allData.stream()
                    .filter(e -> (e.getFromAcctNo() != null && e.getFromAcctNo().contains(acct))
                            || (e.getToAcctNo() != null && e.getToAcctNo().contains(acct)))
                    .collect(Collectors.toList());
        }

        // Otherwise filter by date range with defaults
        LocalDate startDate = Objects.requireNonNullElse(request.getStartDate(), LocalDate.of(2026, 1, 1));
        LocalDate endDate = Objects.requireNonNullElse(request.getEndDate(), LocalDate.of(2026, 1, 2));

        return allData.stream()
                .filter(e -> e.getUserDate() != null
                        && !e.getUserDate().isBefore(startDate)
                        && !e.getUserDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Report breakdown by branch, with totals broken down by currency within each branch.
     */
    public List<BranchReportDto> getBranchReport(ReportReq request) {
        List<VvRpTxnEntity> allData = vvRpTxnRepository.findAll();

        // Filter by date range if provided
        if (request.getStartDate() != null) {
            allData = allData.stream()
                    .filter(e -> e.getTxnDate() != null && !e.getTxnDate().isBefore(request.getStartDate()))
                    .collect(Collectors.toList());
        }
        if (request.getEndDate() != null) {
            allData = allData.stream()
                    .filter(e -> e.getTxnDate() != null && !e.getTxnDate().isAfter(request.getEndDate()))
                    .collect(Collectors.toList());
        }
        // Filter by branch if provided (match against branchNo or branchName)
        if (request.getBranchCode() != null && !request.getBranchCode().isEmpty()) {
            allData = allData.stream()
                    .filter(e -> (e.getBranchNo() != null && String.valueOf(e.getBranchNo()).equals(request.getBranchCode()))
                            || (e.getBranchName() != null && e.getBranchName().equals(request.getBranchCode())))
                    .collect(Collectors.toList());
        }

        // Group by branchNo
        Map<Long, List<VvRpTxnEntity>> byBranch = allData.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getBranchNo() != null ? e.getBranchNo() : 0L,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return byBranch.entrySet().stream().map(entry -> {
            Long branchNo = entry.getKey();
            List<VvRpTxnEntity> branchData = entry.getValue();

            // Get branch name from first record (they should all be the same)
            String branchName = branchData.stream()
                    .map(VvRpTxnEntity::getBranchName)
                    .filter(n -> n != null && !n.isEmpty())
                    .findFirst()
                    .orElse("Unknown");

            // Group by currency (fromAcctCcy) and sum the fromAcctAmount
            Map<String, Double> byCurrency = branchData.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getFromAcctCcy() != null ? e.getFromAcctCcy() : "N/A",
                            LinkedHashMap::new,
                            Collectors.summingDouble(e -> e.getFromAcctAmount() != null ? e.getFromAcctAmount() : 0.0)
                    ));

            List<BranchReportDto.CurrencyBreakdown> currencies = byCurrency.entrySet().stream()
                    .map(e -> new BranchReportDto.CurrencyBreakdown(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            Double grandTotal = currencies.stream()
                    .mapToDouble(BranchReportDto.CurrencyBreakdown::getTotalAmount)
                    .sum();

            return new BranchReportDto(branchNo, branchName, currencies, grandTotal);
        }).collect(Collectors.toList());
    }
}
