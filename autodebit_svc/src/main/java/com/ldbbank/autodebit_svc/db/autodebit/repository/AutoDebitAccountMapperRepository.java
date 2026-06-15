package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountMapperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoDebitAccountMapperRepository extends JpaRepository<AutoDebitAccountMapperEntity, Long> {
    List<AutoDebitAccountMapperEntity> findByStatus(Integer status);
    List<AutoDebitAccountMapperEntity> findByPartnerName(String partnerName);
}
