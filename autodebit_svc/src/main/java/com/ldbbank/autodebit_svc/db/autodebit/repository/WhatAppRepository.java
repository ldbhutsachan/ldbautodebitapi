package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.WhatAppDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WhatAppRepository extends JpaRepository<WhatAppDbEntity, Integer> {

    @Query(value = "SELECT * FROM VV_WHATAPP WHERE TO_ACCT_CCY IN (:currencies)", nativeQuery = true)
    List<WhatAppDbEntity> findAllByToAcctCcy(@Param("currencies") List<String> currencies);


}
