package com.ldbbank.autodebit_svc.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AutoDebitAccountDto {
    private Long id;
    private String accountNo;
    private String accountName;
    private Long companyId;
    private String companyCode;
    private String companyName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
