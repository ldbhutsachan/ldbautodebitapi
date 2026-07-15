package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.t24.entity.ExchangeRateEntity;
import com.ldbbank.autodebit_svc.model.corebank.*;

public interface CorebankService {
    public APIResponse<FundTransferRes<FundTransferDataResponse>> shippingFundTransferCoreBanking(FundTransferReq req);
    public APIResponse<ReversalRes> reversal(ReversalReq req) throws Exception;
    public APIResponse<ExchangeRateEntity> getExchangeRate(String currencyCode);
}
