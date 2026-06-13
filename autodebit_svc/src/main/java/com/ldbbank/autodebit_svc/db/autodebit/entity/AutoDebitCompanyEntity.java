package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "AUTO_DEBIT_COMPANY")
public class AutoDebitCompanyEntity {

    @Id
    @SequenceGenerator(name = "auto_debit_company_seq", sequenceName = "AUTO_DEBIT_COMPANY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_debit_company_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPANY_CODE", length = 64)
    private String companyCode;

    @Column(name = "COMPANY_NAME", length = 256)
    private String companyName;

    @Column(name = "STATUS", length = 32)
    private String status; // e.g. open, disabled

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "IMAGE_PATH", length = 1024)
    private String imagePath;

    @Column(name = "IMAGE_NAME", length = 512)
    private String imageName;
}

