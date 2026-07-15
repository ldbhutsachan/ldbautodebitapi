package com.ldbbank.autodebit_svc.db.autodebit.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ACCOUNT_TYPE")
public class AccountTypeDbEntity {
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CODE", length = 100)
    private String code;

    @Column(name = "TYPE_NAME", length = 30)
    private String typeName;

}
