package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "USER_TXN")
public class AccountMapperEntity {

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

    @Column(name = "TXN_TYPE", length = 20)
    private String txnType;

    @Column(name = "REMARK", length = 255)
    private String remark;

    @Column(name = "USER_BY", length = 50)
    private String userBy;

    @Column(name = "USER_DATE")
    private LocalDate userDate;

    @Column(name = "USER_EDITING", length = 50)
    private String userEditing;

    @Column(name = "EDITING_DATE")
    private LocalDate editingDate;

    @Column(name = "USER_STATUS_BY", length = 20)
    private String userStatusBy;

    @Column(name = "USER_STATUS_DATE")
    private LocalDate userStatusDate;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "PARTNER_NAME", length = 100)
    private String partnerName;

    @Column(name = "BRANCH_CODE", length = 200)
    private String branchCode;

    @Column(name = "SIGNATURE", length = 4000)
    private String signature;

}
