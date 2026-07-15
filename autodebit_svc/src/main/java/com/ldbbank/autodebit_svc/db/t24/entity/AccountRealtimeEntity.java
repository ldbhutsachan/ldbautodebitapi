package com.ldbbank.autodebit_svc.db.t24.entity;


import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "FBNK_ACCOUNT", schema = "T24USR")
public class AccountRealtimeEntity {

    @Id
    @Column(name = "RECID")
    private String accountNo;

    @Column(name = "CIF")
    private String cif;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "ACCOUNT_OFFICER")
    private String accountOfficer;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "CCY")
    private String ccy;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "INACTIVE_FLAG")
    private String inactiveFlag;

    @Column(name = "OPENING_DATE")
    private String openingDate;
}
