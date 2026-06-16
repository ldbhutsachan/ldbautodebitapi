package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountTxnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AutoDebitAccountTxnRepository extends JpaRepository<AutoDebitAccountTxnEntity, Long> {

    Optional<AutoDebitAccountTxnEntity> findByFromAcctNoAndTxnDateAndStatus(String fromAcctNo, LocalDate txnDate, String status);
    Optional<AutoDebitAccountTxnEntity> findByFromAcctNoAndTxnDate(String fromAcctNo, LocalDate txnDate);
}
