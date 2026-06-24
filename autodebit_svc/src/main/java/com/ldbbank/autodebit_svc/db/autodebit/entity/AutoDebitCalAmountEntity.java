package com.ldbbank.autodebit_svc.db.autodebit.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "AUTO_DEBIT_CAL", schema = "AUTO_DEBIT_USER")
public class AutoDebitCalAmountEntity {

    @Id
    @Column(name = "KEY_ID")
    private Long keyId;

    @Column(name = "CCY", length = 20)
    private String ccy;

    @Column(name = "START_AMOUNT")
    private BigDecimal startAmount;

    @Column(name = "TYPE", length = 20)
    private String type;
}
