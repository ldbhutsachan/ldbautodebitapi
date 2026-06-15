package com.ldbbank.autodebit_svc.model.corebank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoreBankDataResponse {
    private String t24RefID;
    private String debitAmount;
    private String creditAmount;
    private String creditAccount;
    private String fee;
    private String rate;
    private String dateTime;
}
