package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "AUTO_DEBIT_ACCOUNT_MAPPER")
public class AutoDebitAccountMapperEntity {

    @Id
    @SequenceGenerator(name = "auto_debit_account_mapper_seq", sequenceName = "AUTO_DEBIT_ACCOUNT_MAPPER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_debit_account_mapper_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "SECTION_NO", length = 30)
    private String sectionNo; // branch code

    @Column(name = "USER_ID")
    private Long userId; // user_login.USER_ID

    @Column(name = "STATUS")
    private Integer status; // 0 = close, 1 = open

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
