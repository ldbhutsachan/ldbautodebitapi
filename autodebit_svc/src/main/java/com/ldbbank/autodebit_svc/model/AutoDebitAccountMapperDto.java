package com.ldbbank.autodebit_svc.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AutoDebitAccountMapperDto {
    private Long id;
    private String accountNo;
    private String accountName;

    private Long companyId;
    private Long companyCode;
    private String companyName;

    private String sectionNo;
    private String sectionName;

    private String userId;
    private String userName;

    private Integer status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
