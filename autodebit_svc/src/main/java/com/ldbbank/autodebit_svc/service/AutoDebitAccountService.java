package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitCompanyRepository;
import com.ldbbank.autodebit_svc.model.AutoDebitAccountDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AutoDebitAccountService {
    private final AutoDebitAccountRepository accountRepo;
    private final AutoDebitCompanyRepository companyRepo;

    public AutoDebitAccountService(AutoDebitAccountRepository accountRepo, AutoDebitCompanyRepository companyRepo) {
        this.accountRepo = accountRepo;
        this.companyRepo = companyRepo;
    }

    public AutoDebitAccountEntity save(AutoDebitAccountEntity e) {
        LocalDateTime now = LocalDateTime.now();
        e.setCreatedAt(now);
        if (e.getStatus() == null) e.setStatus("open");
        return accountRepo.save(e);
    }

    public Optional<AutoDebitAccountEntity> update(Long id, AutoDebitAccountEntity change) {
        return accountRepo.findById(id).map(existing -> {
            if (change.getAccountNo() != null) existing.setAccountNo(change.getAccountNo());
            if (change.getAccountName() != null) existing.setAccountName(change.getAccountName());
            if (change.getPartNerName() != null) existing.setPartNerName(change.getPartNerName());
            if (change.getStatus() != null) existing.setStatus(change.getStatus());
            return accountRepo.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!accountRepo.existsById(id)) return false;
        accountRepo.deleteById(id);
        return true;
    }

    public List<AutoDebitAccountDto> findByStatusWithCompany(String status) {
        List<AutoDebitAccountEntity> accounts = accountRepo.findByStatus(status);
        List<AutoDebitAccountDto> result = new ArrayList<>();
        for (AutoDebitAccountEntity a : accounts) {
            AutoDebitAccountDto d = mapToDto(a);
            if (a.getPartNerName() != null) {
                Optional<AutoDebitCompanyEntity> c = companyRepo.findById(Long.valueOf(a.getPartNerName()));
                c.ifPresent(company -> {
                    d.setCompanyCode(company.getCompanyCode());
                    d.setCompanyName(company.getCompanyName());
                });
            }
            result.add(d);
        }
        return result;
    }

    public List<AutoDebitAccountDto> findAllWithCompany() {
        List<AutoDebitAccountEntity> accounts = accountRepo.findAll();
        List<AutoDebitAccountDto> result = new ArrayList<>();
        for (AutoDebitAccountEntity a : accounts) result.add(mapToDto(a));
        return result;
    }

    private AutoDebitAccountDto mapToDto(AutoDebitAccountEntity a) {
        AutoDebitAccountDto d = new AutoDebitAccountDto();
        d.setId(a.getId());
        d.setAccountNo(a.getAccountNo());
        d.setAccountName(a.getAccountName());
        d.setCompanyId(a.getPartNerName());
        d.setStatus(a.getStatus());
        d.setCreatedAt(a.getCreatedAt());
        return d;
    }
}
