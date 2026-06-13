package com.ldbbank.autodebit_svc.db.autodebit.repository;


import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRpTxnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VvRpTxnRepository extends JpaRepository<VvRpTxnEntity, Long> {
    // Add custom query methods if needed
}

