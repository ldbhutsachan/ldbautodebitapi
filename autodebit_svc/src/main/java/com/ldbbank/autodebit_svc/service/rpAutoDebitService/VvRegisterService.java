package com.ldbbank.autodebit_svc.service.rpAutoDebitService;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRegisterEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.VvRegisterRepository;
import com.ldbbank.autodebit_svc.model.reportAutoDebit.ReportReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class VvRegisterService {

    private final VvRegisterRepository repository;
    public List<VvRegisterEntity> getRegisterData(ReportReq request) {
        return repository.findByBranchCodeAndUserDateBetween(
                request.getBranchCode(),
                request.getStartDate(),
                request.getEndDate()
        );
    }
}
