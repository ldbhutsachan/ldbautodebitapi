package com.ldbbank.autodebit_svc.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRealtimeDto {
    private String accountNo;
    private String cif;
    private String category;
    private String categoryName;
    private String accountName;
    private String accountOfficer;
    private String branchCode;
    private String ccy;
    private BigDecimal balance;
    private String inactiveFlag;
    private String openingDate;
}
