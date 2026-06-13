package com.ldbbank.autodebit_svc;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountMapperEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountTxnEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountMapperRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountTxnRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitCompanyRepository;
import com.ldbbank.autodebit_svc.service.BatchService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BatchServiceTest {

    @Test
    void runForDate_createsTxnEntries_basedOnPercentAndClosingBalance() {
        // mocks
        AutoDebitCompanyRepository companyRepo = mock(AutoDebitCompanyRepository.class);
        AutoDebitAccountMapperRepository mapperRepo = mock(AutoDebitAccountMapperRepository.class);
        AutoDebitAccountRepository accountRepo = mock(AutoDebitAccountRepository.class);
        AutoDebitAccountTxnRepository txnRepo = mock(AutoDebitAccountTxnRepository.class);

        BatchService svc = new BatchService(companyRepo, mapperRepo, accountRepo, txnRepo);

        // setup company
        AutoDebitCompanyEntity comp = new AutoDebitCompanyEntity();
        comp.setId(1L);
        comp.setStatus("open");
        comp.setBatRunningDate(LocalDate.of(2026,6,13));
        comp.setPercent(BigDecimal.valueOf(10)); // 10%

        when(companyRepo.findByStatus("open")).thenReturn(List.of(comp));

        // mapper
        AutoDebitAccountMapperEntity map = new AutoDebitAccountMapperEntity();
        map.setId(11L);
        map.setAccountId(100L);
        map.setCompanyId(1L);
        map.setStatus(1);
        when(mapperRepo.findByCompanyId(1L)).thenReturn(List.of(map));

        // account
        AutoDebitAccountEntity acc = new AutoDebitAccountEntity();
        acc.setId(100L);
        acc.setAccountNo("ACC100");
        acc.setAccountName("TestAccount");
        when(accountRepo.findById(100L)).thenReturn(Optional.of(acc));

        // run for date
        svc.runForDate(LocalDate.of(2026,6,13));

        // capture saved txn
        ArgumentCaptor<AutoDebitAccountTxnEntity> captor = ArgumentCaptor.forClass(AutoDebitAccountTxnEntity.class);
        verify(txnRepo, times(1)).save(captor.capture());
        AutoDebitAccountTxnEntity saved = captor.getValue();

        assertEquals(100L, saved.getAccountId());
        assertEquals(1L, saved.getCompanyId());
        // mock closing balance default is 1000 in BatchService, amount = 1000 * 10% = 100
        assertEquals(BigDecimal.valueOf(1000), saved.getClosingBalance());
        assertEquals(0, BigDecimal.valueOf(100).compareTo(saved.getAmount()));
        assertNotNull(saved.getTxnDate());
        assertNotNull(saved.getCreatedAt());
    }
}
