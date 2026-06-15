package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "BRANCH")
public class BranchDbEntity {

    @Id
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


}
