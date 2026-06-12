package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "VV_REGISTER")
public class VvRegisterEntity {

    @Id
    @Column(name = "KEY_ID", nullable = false)
    private Long keyId;

    @Column(name = "FROM_ACCT_NO", length = 30)
    private String fromAcctNo;

    @Column(name = "FROM_ACCT_NAME", length = 100)
    private String fromAcctName;

    @Column(name = "FROM_ACCT_CCY", length = 10)
    private String fromAcctCcy;

    @Column(name = "TO_ACCT_NO", length = 30)
    private String toAcctNo;

    @Column(name = "TO_ACCT_NAME", length = 100)
    private String toAcctName;

    @Column(name = "TO_ACCT_CCY", length = 100)
    private String toAcctCcy;

    @Column(name = "STATUS_BY_AT")
    private LocalDateTime statusByAt;

    @Column(name = "IMAGE_PATH", length = 1500)
    private String imagePath;

    @Column(name = "IMAGE_NAME", length = 700)
    private String imageName;

    @Column(name = "PERCENT")
    private Integer percent;

    @Column(name = "TXN_TYPE", length = 20)
    private String txnType;

    @Column(name = "REMARK", length = 255)
    private String remark;

    @Column(name = "USER_BY", length = 50)
    private String userBy;

    @Column(name = "USER_DATE")
    private LocalDate userDate;

    @Column(name = "PARTNER_NAME", length = 100)
    private String partnerName;

    @Column(name = "BRANCH_CODE", length = 200)
    private String branchCode;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_NAME", length = 200)
    private String userName;

    @Column(name = "SECTION_NO", length = 100)
    private String sectionNo;

    @Column(name = "SECTION_NAME", length = 100)
    private String sectionName;

    @Column(name = "NAME", length = 200)
    private String name;

    @Column(name = "MOBILE", length = 100)
    private String mobile;

    @Column(name = "MAIL", length = 200)
    private String mail;

    @Column(name = "ROLE_NO")
    private Long roleNo;

    @Column(name = "ROLE_NAME", length = 128)
    private String roleName;

    @Column(name = "STATUS", length = 100)
    private String status;

}
