package com.example.txnservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "ACCOUNT_TXN")
public class AccountTxnEntity {

    @Id
    @Column(name = "KEY_ID", nullable = false)
    private Long keyId;

    @Column(name = "FROM_ACCT_NO", length = 30)
    private String fromAcctNo;

    @Column(name = "FROM_ACCT_NAME", length = 100)
    private String fromAcctName;

    @Column(name = "FROM_ACCT_CCY", length = 3)
    private String fromAcctCcy;

    @Column(name = "FROM_ACCT_AMOUNT")
    private Double fromAcctAmount;

    @Column(name = "TO_ACCT_NO", length = 30)
    private String toAcctNo;

    @Column(name = "TO_ACCT_NAME", length = 100)
    private String toAcctName;

    @Column(name = "TO_ACCT_CCY", length = 3)
    private String toAcctCcy;

    @Column(name = "TO_ACCT_AMOUNT")
    private Double toAcctAmount;

    @Column(name = "TXN_DATE")
    private LocalDate txnDate;

    @Column(name = "PERCENT")
    private Long percent;

    @Column(name = "BALANE_AMOUNT")
    private Double balanceAmount;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    @Column(name = "TXN_TYPE", length = 20)
    private String txnType;

    @Column(name = "REMARK", length = 255)
    private String remark;

    @Column(name = "REF", length = 100)
    private String ref;

    @Column(name = "CORE_TXN_DATE")
    private LocalDateTime coreTxnDate;

    @Column(name = "CORE_REQ", length = 1500)
    private String coreReq;

    @Column(name = "CORE_RES", length = 1500)
    private String coreRes;

    @Column(name = "STATUS", length = 200)
    private String status;

    @Column(name = "USER_REP", length = 200)
    private String userRep;

    @Column(name = "REP_DATE")
    private LocalDate repDate;
}