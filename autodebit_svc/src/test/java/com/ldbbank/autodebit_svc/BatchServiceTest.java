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

    }
}
