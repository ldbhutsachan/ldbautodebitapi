package com.ldbbank.autodebit_svc.mapper;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvTransactionEntity;
import com.ldbbank.autodebit_svc.dto.VvTransactionDto;

public class VvTransactionMapper {

    public static VvTransactionDto toDto(VvTransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        VvTransactionDto dto = new VvTransactionDto();
        dto.setKeyId(entity.getKeyId());
        dto.setFromAcctNo(entity.getFromAcctNo());
        dto.setFromAcctName(entity.getFromAcctName());
        dto.setFromAcctCcy(entity.getFromAcctCcy());
        dto.setFromAcctAmount(entity.getFromAcctAmount());
        dto.setToAcctNo(entity.getToAcctNo());
        dto.setToAcctName(entity.getToAcctName());
        dto.setToAcctCcy(entity.getToAcctCcy());
        dto.setToAcctAmount(entity.getToAcctAmount());
        dto.setTxnDate(entity.getTxnDate());
        dto.setPercent(entity.getPercent());
        dto.setBalanceAmount(entity.getBalanceAmount());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setTxnType(entity.getTxnType());
        dto.setRemark(entity.getRemark());
        dto.setRef(entity.getRef());
        dto.setCoreTxnDate(entity.getCoreTxnDate());
        dto.setCoreReq(entity.getCoreReq());
        dto.setCoreRes(entity.getCoreRes());
        dto.setUserRep(entity.getUserRep());
        dto.setRepDate(entity.getRepDate());
        dto.setBranchNo(entity.getBranchNo());
        dto.setBranchName(entity.getBranchName());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
