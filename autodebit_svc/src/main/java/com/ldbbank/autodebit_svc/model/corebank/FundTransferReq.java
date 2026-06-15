package com.ldbbank.autodebit_svc.model.corebank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FundTransferReq {
    private String action;
    private String txnType;
    private String debitAcct;
    private String debitCurrency;
    private String debitAmount;
    private String creditAcct;
    private String creditCurrency;
    private String creditAmount;
    private String customerName;
    private String mobileNumber;
    private String billNo;
    private String detail;
    private String txnRef;
    private String chargeType;
    private String chargeCurrency;
    private String chargeAmount;

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return super.toString();  // Fallback to default toString if JSON processing fails
        }
    }
}