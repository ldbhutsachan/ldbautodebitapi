package com.ldbbank.autodebit_svc.db.t24.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "FBNK_CURRENCY", schema = "T24USR")
public class ExchangeRateEntity {

    @Id
    @Column(name = "RECID")
    private String currencyCode;

    @Column(name = "BUY_RATE")
    private BigDecimal buyRate;

    @Column(name = "SELL_RATE")
    private BigDecimal sellRate;

    @Column(name = "EXCHANGE_DATE")
    private String exchangeDate;

    @Column(name = "TYPE")
    private Integer type;
}
