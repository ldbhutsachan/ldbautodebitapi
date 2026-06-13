package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VvUserRepository extends JpaRepository<VvUserEntity, Long> {
    // Add custom query methods if needed
    List<VvUserEntity> findByMenuNoIn(java.util.List<Long> menuNos);
    List<VvUserEntity> findByUserName(String userName);

}

