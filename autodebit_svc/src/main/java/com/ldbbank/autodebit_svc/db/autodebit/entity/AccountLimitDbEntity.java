package com.ldbbank.autodebit_svc.db.autodebit.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "ACCOUNT_LIMT")
public class AccountLimitDbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_LIMT_SEQUENCE")
    @SequenceGenerator(name = "ACCOUNT_LIMT_SEQUENCE", sequenceName = "ACCOUNT_LIMT_SEQUENCE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AMT")
    private String amt; // or BigDecimal if numeric

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "COMPANAY_ID")
    private String companyId;

    @Column(name = "COMPANAY_NAME")
    private String companyName;

    @Column(name = "TIME_LIMIT")
    private String timeLimit;

}
