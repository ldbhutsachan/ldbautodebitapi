package com.ldbbank.autodebit_svc.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AutoDebitAccountDto {
    private Long id;
    private String accountNo;
    private String accountName;
    private String companyId;
    private Long companyCode;
    private String companyName;
    private String status;
    private LocalDateTime createdAt;

}
