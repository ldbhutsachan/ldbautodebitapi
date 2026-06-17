package com.ldbbank.autodebit_svc.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AutoDebitBranchReq {
    private String branchName;
    private String status;
    private String partnerId;
}
