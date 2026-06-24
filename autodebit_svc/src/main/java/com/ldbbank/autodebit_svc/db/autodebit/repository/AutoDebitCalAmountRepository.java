package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCalAmountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoDebitCalAmountRepository extends JpaRepository<AutoDebitCalAmountEntity, Long> {

    Optional<AutoDebitCalAmountEntity> findByCcyAndType(String ccy, String type);
}
