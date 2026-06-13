package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "AUTO_DEBIT_ACCOUNT")
public class AutoDebitAccountEntity {

    @Id
    @SequenceGenerator(name = "auto_debit_account_seq", sequenceName = "AUTO_DEBIT_ACCOUNT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_debit_account_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_NO", length = 64)
    private String accountNo;

    @Column(name = "ACCOUNT_NAME", length = 256)
    private String accountName;

    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "STATUS", length = 32)
    private String status; // e.g. open, disabled

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
