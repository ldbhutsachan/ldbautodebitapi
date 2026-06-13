package com.ldbbank.autodebit_svc.db.autodebit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "AUTO_DEBIT_COMPANY")
public class AutoDebitCompanyEntity {

    @Id
    @SequenceGenerator(name = "COMPANY_SEQUENCE", sequenceName = "COMPANY_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_SEQUENCE")
    @Column(name = "COMPANYNO", length = 64)

    private Long companyCode;

    @Column(name = "COMPANY_NAME", length = 256)
    private String companyName;

    @Column(name = "STATUS", length = 32)
    private String status; // e.g. open, disabled

    @Column(name = "PERCENT")
    private java.math.BigDecimal percent;

    @Column(name = "BAT_RUNNING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime batRunningDate;



    @Column(name = "MAKE_BY_AT")
    private LocalDateTime  createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime  updatedAt;

    @Column(name = "IMAGE_PATH", length = 1024)
    private String imagePath;

    @Column(name = "IMAGE_NAME", length = 512)
    private String imageName;

}

