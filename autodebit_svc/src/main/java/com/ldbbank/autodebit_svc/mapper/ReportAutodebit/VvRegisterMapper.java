package com.ldbbank.autodebit_svc.mapper.ReportAutodebit;

import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRegisterEntity;
import com.ldbbank.autodebit_svc.dto.VvRegisterDto;

public class VvRegisterMapper {

    public static VvRegisterDto toDto(VvRegisterEntity entity) {
        VvRegisterDto dto = new VvRegisterDto();
        dto.setKeyId(entity.getKeyId());
        dto.setFromAcctNo(entity.getFromAcctNo());
        dto.setFromAcctName(entity.getFromAcctName());
        dto.setToAcctNo(entity.getToAcctNo());
        dto.setToAcctName(entity.getToAcctName());
        dto.setTxnType(entity.getTxnType());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
