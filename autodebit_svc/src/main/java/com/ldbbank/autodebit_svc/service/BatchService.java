package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountMapperEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountTxnEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountMapperRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountTxnRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitCompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BatchService {

    private final AutoDebitCompanyRepository companyRepo;
    private final AutoDebitAccountMapperRepository mapperRepo;
    private final AutoDebitAccountRepository accountRepo;
    private final AutoDebitAccountTxnRepository txnRepo;
    private final RestTemplate rest = new RestTemplate();

    @Value("${t24.url:}")
    private String t24Url;

    @Value("${t24.mock:true}")
    private boolean t24Mock;

    public BatchService(AutoDebitCompanyRepository companyRepo,
                        AutoDebitAccountMapperRepository mapperRepo,
                        AutoDebitAccountRepository accountRepo,
                        AutoDebitAccountTxnRepository txnRepo) {
        this.companyRepo = companyRepo;
        this.mapperRepo = mapperRepo;
        this.accountRepo = accountRepo;
        this.txnRepo = txnRepo;
    }

    public void runForDate(LocalDate date) {
        List<AutoDebitCompanyEntity> companies = companyRepo.findByStatus("open");
        for (AutoDebitCompanyEntity comp : companies) {
            if (comp.getBatRunningDate() == null) continue;
            if (!comp.getBatRunningDate().equals(date)) continue;
            BigDecimal percent = comp.getPercent() == null ? BigDecimal.ZERO : comp.getPercent();
            // get mappers for company
            List<AutoDebitAccountMapperEntity> maps = mapperRepo.findByCompanyId(comp.getId());
            for (AutoDebitAccountMapperEntity m : maps) {
                if (m.getStatus() == null || m.getStatus() != 1) continue; // only open
                var accOpt = accountRepo.findById(m.getAccountId());
                if (accOpt.isEmpty()) continue;
                AutoDebitAccountEntity acc = accOpt.get();
                BigDecimal closing = fetchClosingBalance(acc.getAccountNo());
                BigDecimal amount = closing.multiply(percent).divide(BigDecimal.valueOf(100));

                AutoDebitAccountTxnEntity txn = new AutoDebitAccountTxnEntity();
                txn.setAccountId(acc.getId());
                txn.setCompanyId(comp.getId());
                txn.setClosingBalance(closing);
                txn.setAmount(amount);
                txn.setTxnDate(LocalDateTime.now());
                txn.setDescription("Auto debit calculation: percent=" + percent);
                txn.setCreatedAt(LocalDateTime.now());
                txnRepo.save(txn);
            }
        }
    }

    private BigDecimal fetchClosingBalance(String accountNo) {
        if (t24Mock || t24Url == null || t24Url.isBlank()) {
            // mock value for testing
            return BigDecimal.valueOf(1000);
        }
        try {
            String url = t24Url + "?accountNo=" + accountNo;
            return rest.getForObject(url, BigDecimal.class);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }
}
