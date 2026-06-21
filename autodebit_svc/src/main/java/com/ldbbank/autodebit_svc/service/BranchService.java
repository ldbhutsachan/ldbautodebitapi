package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.BranchDbEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitCompanyRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.BranchDbRepository;
import com.ldbbank.autodebit_svc.model.AutoDebitBranchReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BranchService {
    private final BranchDbRepository jpa;
    private final AutoDebitCompanyRepository companyRepo;

    public BranchDbEntity createBranch(AutoDebitBranchReq branchReq, String makeBy) {
        BranchDbEntity entity = new BranchDbEntity();
        entity.setBranchName(branchReq.getBranchName());
        entity.setStatus("OPEN");
        entity.setPartnerId(branchReq.getPartnerId());
        entity.setMakeBy(makeBy);
        entity.setMakeByAt(LocalDateTime.now());
        return jpa.save(entity);
    }

    public Optional<BranchDbEntity> updateBranch(Long branchNo, AutoDebitBranchReq req, String statusBy) {
        return jpa.findById(branchNo).map(existing -> {
            if (req.getBranchName() != null) {
                existing.setBranchName(req.getBranchName());
            }
            if (req.getPartnerId() != null) {
                existing.setPartnerId(req.getPartnerId());
            }
            if (req.getStatus() != null) {
                existing.setStatus(req.getStatus());
            }
            existing.setStatusBy(statusBy);
            existing.setStatusByAt(LocalDateTime.now());
            return jpa.save(existing);
        });
    }

    public boolean deleteBranch(Long branchNo) {
        if (jpa.existsById(branchNo)) {
            jpa.deleteById(branchNo);
            return true;
        }
        return false;
    }

    public Optional<BranchDbEntity> updateStatus(Long branchNo, String status, String statusBy) {
        return jpa.findById(branchNo).map(existing -> {
            existing.setStatus(status);
            existing.setStatusBy(statusBy);
            existing.setStatusByAt(LocalDateTime.now());
            return jpa.save(existing);
        });
    }

    public List<Map<String, Object>> listAllWithCompany() {
        List<BranchDbEntity> branches = jpa.findAll();
        // Map partnerId -> companyName
        Map<String, String> companyMap = companyRepo.findAll()
                .stream()
                .filter(c -> c.getCompanyCode() != null)
                .collect(Collectors.toMap(
                        c -> String.valueOf(c.getCompanyCode()),
                        c -> c.getCompanyName() != null ? c.getCompanyName() : "",
                        (a, b) -> a
                ));

        return branches.stream().map(b -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("branchNo", b.getBranchNo());
            map.put("branchName", b.getBranchName());
            map.put("status", b.getStatus());
            map.put("partnerId", b.getPartnerId());
            map.put("companyName", companyMap.getOrDefault(b.getPartnerId(), ""));
            map.put("makeBy", b.getMakeBy());
            map.put("makeByAt", b.getMakeByAt());
            map.put("statusBy", b.getStatusBy());
            map.put("statusByAt", b.getStatusByAt());
            return map;
        }).collect(Collectors.toList());
    }

    public List<BranchDbEntity> findAll() {
        return jpa.findAll();
    }

    public Optional<BranchDbEntity> findById(Long branchNo) {
        return jpa.findById(branchNo);
    }
}
