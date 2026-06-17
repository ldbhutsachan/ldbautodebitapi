package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "AUTO_DEBIT_BRANCH")
public class BranchDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_DEBIT_BRANCH_SEQUENCE")
    @SequenceGenerator(name = "AUTO_DEBIT_BRANCH_SEQUENCE", sequenceName = "AUTO_DEBIT_BRANCH_SEQUENCE", allocationSize = 1)
    @Column(name = "BRANCH_NO", nullable = false)
    private Long branchNo;

    @Column(name = "BRANCH_NAME", length = 128)
    private String branchName;

    @Column(name = "MAKE_BY", length = 128)
    private String makeBy;

    @Column(name = "MAKE_BY_AT")
    private LocalDateTime makeByAt;

    @Column(name = "STATUS_BY", length = 128)
    private String statusBy;

    @Column(name = "STATUS_BY_AT")
    private LocalDateTime statusByAt;

    @Column(name = "STATUS", length = 100)
    private String status;

    @Column(name = "PARTNER_ID", length = 100)
    private String partnerId;


}
