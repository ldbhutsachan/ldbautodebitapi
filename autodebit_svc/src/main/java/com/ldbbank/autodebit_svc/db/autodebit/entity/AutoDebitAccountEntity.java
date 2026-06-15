package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "AUTO_DEBIT_ACCOUNT")
public class AutoDebitAccountEntity {

    @Id
    @SequenceGenerator(name = "AUTO_DEBIT_ACCOUNT_SEQUENCE", sequenceName = "AUTO_DEBIT_ACCOUNT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_DEBIT_ACCOUNT_SEQUENCE")
    @Column(name = "KEY_ID")
    private Long id;

    @Column(name = "ACCOUNT_NO", length = 64)
    private String accountNo;

    @Column(name = "ACCOUNT_NAME", length = 256)
    private String accountName;

    @Column(name = "PARTNER_NAME")
    private String partNerName;

    @Column(name = "ACCOUNT_CCY", length = 256)
    private String accountCcy;

    @Column(name = "STATUS", length = 32)
    private String status; // e.g. open, disabled

    @Column(name = "MAKE_BY")
    private String makeBy;

    @Column(name = "MAKE_DATE")
    private LocalDateTime createdAt;


}
