package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoDebitCompanyRepository extends JpaRepository<AutoDebitCompanyEntity, Long> {
    List<AutoDebitCompanyEntity> findByStatus(String status);
}
