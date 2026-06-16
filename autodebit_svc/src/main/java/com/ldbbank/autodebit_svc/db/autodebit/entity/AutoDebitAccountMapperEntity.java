package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "AUTO_DEBIT_ACCOUNT_MAPPER")
public class AutoDebitAccountMapperEntity {

    @Id
    @Column(name = "KEY_ID", nullable = false)
    private Long keyId;

    @Column(name = "FROM_ACCT_NO", nullable = false, length = 30)
    private String fromAcctNo;

    @Column(name = "FROM_ACCT_NAME", nullable = false, length = 100)
    private String fromAcctName;

    @Column(name = "FROM_ACCT_CCY", nullable = false, length = 10)
    private String fromAcctCcy;

    @Column(name = "FROM_ACCT_TYPE", nullable = false, length = 200)
    private String fromAcctType;

    @Column(name = "TO_ACCT_NO", nullable = false, length = 30)
    private String toAcctNo;

    @Column(name = "TO_ACCT_NAME", length = 100)
    private String toAcctName;

    @Column(name = "TO_ACCT_CCY", nullable = false, length = 100)
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

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "PARTNER_NAME", nullable = false, length = 100)
    private String partnerName;

    @Column(name = "BRANCH_CODE", nullable = false, length = 200)
    private String branchCode;

    @Column(name = "SIGNATURE", length = 4000)
    private String signature;

}
