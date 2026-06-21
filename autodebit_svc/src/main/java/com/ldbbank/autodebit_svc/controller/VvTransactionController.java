package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvTransactionEntity;
import com.ldbbank.autodebit_svc.dto.VvTransactionDto;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.mapper.VvTransactionMapper;
import com.ldbbank.autodebit_svc.service.rpAutoDebitService.VvTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/txn")
public class VvTransactionController {

    private final VvTransactionService transactionService;

    @GetMapping("/vv-transactions")
    public ApiResponse<List<VvTransactionDto>> getAllTransactions(
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<VvTransactionDto> data = transactionService.searchTransactions(accountNo, startDate, endDate)
                .stream()
                .map(VvTransactionMapper::toDto)
                .collect(Collectors.toList());
        return new ApiResponse<>("00", "Success", data);
    }

    @GetMapping("/vv-transactions/{id}")
    public ApiResponse<VvTransactionDto> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(entity -> new ApiResponse<>("00", "Success", VvTransactionMapper.toDto(entity)))
                .orElse(new ApiResponse<>("01", "Transaction not found", null));
    }

    @PostMapping("/vv-transactions")
    public ApiResponse<VvTransactionDto> createTransaction(@RequestBody VvTransactionEntity entity) {
        VvTransactionEntity saved = transactionService.saveTransaction(entity);
        return new ApiResponse<>("00", "Created", VvTransactionMapper.toDto(saved));
    }

    @DeleteMapping("/vv-transactions/{id}")
    public ApiResponse<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return new ApiResponse<>("00", "Deleted", null);
    }
}
