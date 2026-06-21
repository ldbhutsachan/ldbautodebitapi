package com.ldbbank.autodebit_svc.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VvTransactionDto {

    private Long keyId;
    private String fromAcctNo;
    private String fromAcctName;
    private String fromAcctCcy;
    private Double fromAcctAmount;
    private String toAcctNo;
    private String toAcctName;
    private String toAcctCcy;
    private Double toAcctAmount;
    private LocalDate txnDate;
    private Integer percent;
    private Double balanceAmount;
    private Double totalAmount;
    private String txnType;
    private String remark;
    private String ref;
    private LocalDateTime coreTxnDate;
    private String coreReq;
    private String coreRes;
    private String userRep;
    private LocalDate repDate;
    private Long branchNo;
    private String branchName;
    private String status;
}
