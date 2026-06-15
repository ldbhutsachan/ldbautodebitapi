package com.ldbbank.autodebit_svc.model.corebank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FundTransferRes <T> {
    private String status;
    private String message;
    private T dataResponse;
}
