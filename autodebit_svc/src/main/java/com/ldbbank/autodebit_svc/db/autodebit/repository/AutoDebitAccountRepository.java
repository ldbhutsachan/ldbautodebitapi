package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoDebitAccountRepository extends JpaRepository<AutoDebitAccountEntity, Long> {
    List<AutoDebitAccountEntity> findByStatus(String status);
    List<AutoDebitAccountEntity> findByCompanyId(Long companyId);
}
