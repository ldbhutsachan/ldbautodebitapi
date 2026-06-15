package com.ldbbank.autodebit_svc.model.corebank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class APIResponse<T> {

    private int httpStatus;
    private String message;
    private T data;
}
