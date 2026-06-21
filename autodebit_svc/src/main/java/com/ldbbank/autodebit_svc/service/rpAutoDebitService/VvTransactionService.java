package com.ldbbank.autodebit_svc.service.rpAutoDebitService;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvTransactionEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.VvTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VvTransactionService {

    private final VvTransactionRepository repository;

    public List<VvTransactionEntity> getAllTransactions() {
        return repository.findAll();
    }

    public List<VvTransactionEntity> searchTransactions(String accountNo, LocalDate startDate, LocalDate endDate) {
        // No filters → return all directly, avoid stream overhead
        if ((accountNo == null || accountNo.isBlank()) && startDate == null && endDate == null) {
            return getAllTransactions();
        }

        List<VvTransactionEntity> allData = repository.findAll();

        // Filter by accountNo (match fromAcctNo or toAcctNo)
        if (accountNo != null && !accountNo.isBlank()) {
            allData = allData.stream()
                    .filter(e -> (e.getFromAcctNo() != null && e.getFromAcctNo().contains(accountNo))
                            || (e.getToAcctNo() != null && e.getToAcctNo().contains(accountNo)))
                    .collect(Collectors.toList());
        }

        // Filter by startDate
        if (startDate != null) {
            allData = allData.stream()
                    .filter(e -> e.getTxnDate() != null && !e.getTxnDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        // Filter by endDate
        if (endDate != null) {
            allData = allData.stream()
                    .filter(e -> e.getTxnDate() != null && !e.getTxnDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        return allData;
    }

    public Optional<VvTransactionEntity> getTransactionById(Long keyId) {
        return repository.findById(keyId);
    }

    public VvTransactionEntity saveTransaction(VvTransactionEntity entity) {
        return repository.save(entity);
    }

    public void deleteTransaction(Long keyId) {
        repository.deleteById(keyId);
    }
}
