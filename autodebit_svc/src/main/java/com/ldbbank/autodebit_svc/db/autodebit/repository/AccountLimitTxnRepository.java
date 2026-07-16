package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AccountLimitTxnDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLimitTxnRepository extends JpaRepository<AccountLimitTxnDbEntity, Long> {

    // One row is saved per dashboard2 query, so a company can have many rows —
    // count them to check usage against its configured limit.
    long countByCompanyId(String companyId);
}
