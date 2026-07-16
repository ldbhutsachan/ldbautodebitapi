package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AccountLimitDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountLimitDbRepository extends JpaRepository<AccountLimitDbEntity, Long> {

    Optional<AccountLimitDbEntity> findByCompanyId(String companyId);
}
