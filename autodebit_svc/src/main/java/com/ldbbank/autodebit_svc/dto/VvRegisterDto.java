package com.ldbbank.autodebit_svc.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class VvRegisterDto {

    private Long keyId;
    private String fromAcctNo;
    private String fromAcctName;
    private String fromAcctCcy;
    private String toAcctNo;
    private String toAcctName;
    private String toAcctCcy;
    private LocalDateTime statusByAt;
    private String imagePath;
    private String imageName;
    private Integer percent;
    private String txnType;
    private String remark;
    private String userBy;
    private LocalDate userDate;
    private String partnerName;
    private String branchCode;
    private Long userId;
    private String userName;
    private String sectionNo;
    private String sectionName;
    private String name;
    private String mobile;
    private String mail;
    private Long roleNo;
    private String roleName;
    private String status;
}