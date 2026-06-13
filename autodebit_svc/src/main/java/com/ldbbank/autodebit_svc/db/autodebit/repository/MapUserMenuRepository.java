package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.MapUserMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapUserMenuRepository extends JpaRepository<MapUserMenuEntity, Long> {
    List<MapUserMenuEntity> findByUserId(Long userId);
}
