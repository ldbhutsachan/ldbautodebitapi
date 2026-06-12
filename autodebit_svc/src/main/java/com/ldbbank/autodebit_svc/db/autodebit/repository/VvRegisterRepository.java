package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VvRegisterRepository extends JpaRepository<VvRegisterEntity, Long> {
    // You can add custom query methods here if needed
    List<VvRegisterEntity> findByBranchCodeAndUserDateBetween(String branchCode, LocalDate startDate, LocalDate endDate);
}

