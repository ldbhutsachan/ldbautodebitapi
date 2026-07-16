package com.ldbbank.autodebit_svc.db.autodebit.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "ACCOUNT_LIMT_TXN")
public class AccountLimitTxnDbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_LIMT_TXN_SEQUENCE")
    @SequenceGenerator(name = "ACCOUNT_LIMT_TXN_SEQUENCE", sequenceName = "ACCOUNT_LIMT_TXN_SEQUENCE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPANAY_ID")
    private String companyId;

    @Column(name = "COMPANAY_NAME")
    private String companyName;

    @Column(name = "TIME_CHECK")
    private LocalDate timeCheck;

    @Column(name = "TIME_NOW")
    private LocalDateTime timeNow;

}
