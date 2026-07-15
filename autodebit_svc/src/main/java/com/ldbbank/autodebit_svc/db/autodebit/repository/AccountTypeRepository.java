package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AccountTypeDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountTypeRepository extends JpaRepository<AccountTypeDbEntity,Long> {
    List<AccountTypeDbEntity> findByCodeIn(List<String> codes);
}
