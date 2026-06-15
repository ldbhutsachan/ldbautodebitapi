package com.ldbbank.autodebit_svc.model.corebank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReversalReq {
    private String txnRef;
}
