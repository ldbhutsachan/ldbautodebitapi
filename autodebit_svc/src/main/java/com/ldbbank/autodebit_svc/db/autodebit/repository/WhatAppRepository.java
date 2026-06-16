package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.WhatAppDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface WhatAppRepository extends JpaRepository<WhatAppDbEntity, Integer> {
    List<WhatAppDbEntity> findAllByTxnDate(LocalDate txnDate);
}
