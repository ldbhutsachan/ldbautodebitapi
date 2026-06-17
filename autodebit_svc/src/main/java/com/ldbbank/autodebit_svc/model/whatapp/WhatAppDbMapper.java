package com.ldbbank.autodebit_svc.model.whatapp;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WhatAppDbMapper {

    private Long branchNo;
    private String branchName;
    private String toAcctCcy;
    private String companyName;
    private LocalDate txnDate;
    private String toAcctAmountLak;
    private String toAcctAmountUsd;
    private String toAcctAmountThb;
    private String toAcctAmountCny;
}
