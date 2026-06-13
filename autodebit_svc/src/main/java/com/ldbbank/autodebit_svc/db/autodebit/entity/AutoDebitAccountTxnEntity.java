package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "AUTO_DEBIT_ACCOUNT_TXN")
public class AutoDebitAccountTxnEntity {

    @Id
    @SequenceGenerator(name = "auto_debit_account_txn_seq", sequenceName = "AUTO_DEBIT_ACCOUNT_TXN_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_debit_account_txn_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CLOSING_BALANCE")
    private BigDecimal closingBalance;

    @Column(name = "TXN_DATE")
    private LocalDateTime txnDate;

    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

}
