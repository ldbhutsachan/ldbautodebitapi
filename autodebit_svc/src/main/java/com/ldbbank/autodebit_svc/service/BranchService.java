package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.BranchDbEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.BranchDbRepository;
import com.ldbbank.autodebit_svc.model.AutoDebitBranchReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class BranchService {
    private final BranchDbRepository jpa;

    public BranchDbEntity createBranch(AutoDebitBranchReq branchReq) {
        BranchDbEntity entity = new BranchDbEntity();
        entity.setBranchName(branchReq.getBranchName());
        entity.setStatus(branchReq.getStatus());
        entity.setPartnerId(branchReq.getPartnerId());
        return jpa.save(entity);
    }
}
