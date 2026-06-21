package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.ChildMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildMenuRepository extends JpaRepository<ChildMenuEntity, Long> {

    List<ChildMenuEntity> findByMenuIdOrderByOrderTypeAsc(String menuId);

}
