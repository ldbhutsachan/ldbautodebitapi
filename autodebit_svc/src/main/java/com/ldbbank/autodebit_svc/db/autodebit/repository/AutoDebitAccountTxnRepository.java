package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountTxnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoDebitAccountTxnRepository extends JpaRepository<AutoDebitAccountTxnEntity, Long> {
}
