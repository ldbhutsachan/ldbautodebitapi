package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "COMPANY")
public class CompanyDbEntity {

    @Id
    @Column(name = "COMPANYNO", nullable = false)
    private Long companyNo;

    @Column(name = "COMPANY_NAME", length = 128)
    private String companyName;

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

    @Column(name = "IMAGE_PATH", length = 1500)
    private String imagePath;

    @Column(name = "IMAGE_NAME", length = 700)
    private String imageName;

    @Column(name = "PERCENT")
    private Integer percent;

}
