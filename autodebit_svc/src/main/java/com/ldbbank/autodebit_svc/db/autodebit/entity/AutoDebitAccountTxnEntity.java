package com.ldbbank.autodebit_svc.db.autodebit.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
    @Data
    @Entity
    @Table(name = "AUTO_DEBIT_ACCOUNT_TXN", schema = "AUTO_DEBIT_USER")
    public class AutoDebitAccountTxnEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_DEBIT_TXN_SEQUENCE")
        @SequenceGenerator(name = "AUTO_DEBIT_TXN_SEQUENCE", sequenceName = "AUTO_DEBIT_TXN_SEQUENCE", allocationSize = 1)
        @Column(name = "KEY_ID")
        private Long keyId;


        @Column(name = "FROM_ACCT_NO", length = 200)
        private String fromAcctNo;

        @Column(name = "FROM_ACCT_NAME", length = 500)
        private String fromAcctName;

        @Column(name = "FROM_ACCT_CCY", length = 30)
        private String fromAcctCcy;

        @Column(name = "FROM_ACCT_TYPE", length = 200)
        private String fromAcctType;

        @Column(name = "FROM_ACCT_AMOUNT")
        private BigDecimal fromAcctAmount;

        @Column(name = "TO_ACCT_NO", length = 300)
        private String toAcctNo;

        @Column(name = "TO_ACCT_NAME", length = 100)
        private String toAcctName;

        @Column(name = "TO_ACCT_CCY", length = 30)
        private String toAcctCcy;

        @Column(name = "TO_ACCT_AMOUNT")
        private BigDecimal toAcctAmount;

        @Column(name = "TXN_DATE")
        private LocalDate txnDate;

        @Column(name = "PERCENT")
        private BigDecimal percent;

        @Column(name = "BALANE_AMOUNT")
        private BigDecimal balanceAmount;

        @Column(name = "TOTAL_AMOUNT")
        private BigDecimal totalAmount;

        @Column(name = "CAL_BALACE")
        private BigDecimal calTotalAmount;

        @Column(name = "TXN_TYPE", length = 200)
        private String txnType;

        @Column(name = "REMARK", length = 255)
        private String remark;

        @Column(name = "REF", length = 100)
        private String ref;

        @Column(name = "reference", length = 100)
        private String reference;

        @Column(name = "CORE_TXN_DATE")
        private LocalDateTime coreTxnDate;

        @Column(name = "CORE_REQ", length = 1500)
        private String coreReq;

        @Column(name = "CORE_RES", length = 1500)
        private String coreRes;

        @Column(name = "STATUS", length = 500)
        private String status;

        @Column(name = "USER_REP", length = 500)
        private String userRep;

        @Column(name = "REP_DATE")
        private LocalDate repDate;


    }
