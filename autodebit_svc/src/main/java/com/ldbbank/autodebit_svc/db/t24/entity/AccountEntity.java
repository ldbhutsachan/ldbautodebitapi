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
public class AccountEntity {

    @Id
    @Column(name = "RECID")
    private String accountNo;

    @Column(name = "SHORT_TITLE")
    private String shortTitle;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "CUSTOMER")
    private String customer;

    @Column(name = "WORKING_BALANCE")
    private BigDecimal workingBalance;

    @Column(name = "INACTIV_MARKER")
    private String inactivMarker;


}
