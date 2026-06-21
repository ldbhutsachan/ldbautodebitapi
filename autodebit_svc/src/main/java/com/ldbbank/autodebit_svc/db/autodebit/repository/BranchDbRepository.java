package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.BranchDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchDbRepository extends JpaRepository<BranchDbEntity, Long> {
}
