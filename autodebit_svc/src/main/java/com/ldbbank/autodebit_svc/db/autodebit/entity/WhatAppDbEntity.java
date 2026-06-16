package com.ldbbank.autodebit_svc.db.autodebit.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "VV_WHATAPP") // adjust to your actual table name
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhatAppDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRANCH_NO")
    private Long branchNo;

    @Column(name = "BRANCH_NAME", length = 128)
    private String branchName;

    @Column(name = "TO_ACCT_CCY", length = 20)
    private String toAcctCcy;

    @Column(name = "COMPANY_NAME", length = 128)
    private String companyName;

    @Column(name = "TXN_DATE")
    private LocalDate txnDate;

    @Column(name = "TO_ACCT_AMOUNT", precision = 19, scale = 2)
    private BigDecimal toAcctAmount;
}
