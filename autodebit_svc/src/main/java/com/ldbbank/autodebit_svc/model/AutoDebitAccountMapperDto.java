package com.ldbbank.autodebit_svc.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AutoDebitAccountMapperDto {
    private Long id;
    private Long accountId;
    private String accountNo;
    private String accountName;

    private Long companyId;
    private String companyCode;
    private String companyName;

    private String sectionNo;
    private String sectionName;

    private Long userId;
    private String userName;

    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
