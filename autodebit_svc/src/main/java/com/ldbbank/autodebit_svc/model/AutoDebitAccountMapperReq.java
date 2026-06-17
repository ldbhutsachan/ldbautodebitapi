package com.ldbbank.autodebit_svc.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
public class AutoDebitAccountMapperReq {
    private String fromAcctNo;
    private String fromAcctName;
    private String fromAcctCcy;
    private String fromAcctType;
    private String toAcctNo;
    private String toAcctName;
    private String toAcctCcy;
    private String remark;
    private String userBy;
    private LocalDate userDate;
    private Integer status;
    private String partnerName;
    private String branchCode;

}
