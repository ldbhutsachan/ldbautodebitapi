package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoDebitAccountRepository extends JpaRepository<AutoDebitAccountEntity, Long> {
    List<AutoDebitAccountEntity> findByStatus(String status);
    Optional<AutoDebitAccountEntity> findByPartNerName(String partNerName);
    Optional<AutoDebitAccountEntity> findByPartNerNameAndAccountCcy(String partNerName, String accountCcy);
}
