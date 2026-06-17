package com.ldbbank.autodebit_svc.db.autodebit.repository;

import com.ldbbank.autodebit_svc.db.autodebit.entity.BranchDbEntity;
import org.springframework.data.repository.CrudRepository;

public interface BranchDbRepository extends CrudRepository<BranchDbEntity, Integer> {
}
