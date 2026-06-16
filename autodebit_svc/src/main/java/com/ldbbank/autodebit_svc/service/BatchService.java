package com.ldbbank.autodebit_svc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountMapperEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountTxnEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountMapperRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountTxnRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitCompanyRepository;
import com.ldbbank.autodebit_svc.db.t24.entity.AccountEntity;
import com.ldbbank.autodebit_svc.db.t24.repository.AccountRepository;
import com.ldbbank.autodebit_svc.model.corebank.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import unitl.Constant;
import unitl.RefGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BatchService {

    private final AutoDebitCompanyRepository companyRepo;
    private final AutoDebitAccountMapperRepository mapperRepo;
    private final AutoDebitAccountRepository accountRepo;
    private final AutoDebitAccountTxnRepository txnRepo;
    private final AccountRepository accountRepository;
    private final CorebankService corebankService;
    private final RestTemplate rest = new RestTemplate();

    public void runForDateRetry() {
        LocalDate date = LocalDate.now();
        log.info("=======start running for date========:" + date);

        List<AutoDebitCompanyEntity> companies = companyRepo.findByStatus("open");
        for (AutoDebitCompanyEntity comp : companies) {

            if (comp.getBatRunningDate() == null) continue;
            //ກວດສອບວັນທີ Running bat
            // if (!comp.getBatRunningDate().equals(date)) continue;
            //ກວດສອບ Percent ກ່ອນຈະ ຕັດເງິນ
            BigDecimal percent = comp.getPercent() == null ? BigDecimal.ZERO : comp.getPercent();
            // get mappers for company
            List<AutoDebitAccountMapperEntity> maps = mapperRepo.findByPartnerName(String.valueOf(comp.getCompanyCode()));

            for (AutoDebitAccountMapperEntity m : maps) {
                if (m.getStatus() == null || m.getStatus() != 1) continue; // only open

                // check account
                var accOpt = accountRepo.findByPartNerNameAndAccountCcy(m.getPartnerName(), m.getFromAcctCcy());
                if (accOpt.isEmpty()) continue;

                AutoDebitAccountEntity acc = accOpt.get();

                // check if transaction already exists for this account/date
                Optional<AutoDebitAccountTxnEntity> checkTxn = txnRepo.findByFromAcctNoAndTxnDateStatus(m.getFromAcctNo(), date,"retry");
                if (!checkTxn.isPresent()) {
                    log.info("Transaction already exists for account {} on date {}", m.getFromAcctNo(), date);
                    continue; // skip insert
                }

                // insert new transaction
                AutoDebitAccountTxnEntity mapperTxn = mapperInsertDataToAccountTxn(percent, comp, m, acc);

                // call fund transfer
                fundTransferResAPIStep01(comp, m, acc, mapperTxn);
            }

        }
    }

    public void runForMonth() {
        LocalDate date = LocalDate.now();
        log.info("=======start running for date========:" + date);

        List<AutoDebitCompanyEntity> companies = companyRepo.findByStatus("open");
        for (AutoDebitCompanyEntity comp : companies) {

            if (comp.getBatRunningDate() == null) continue;
            //ກວດສອບວັນທີ Running bat
           // if (!comp.getBatRunningDate().equals(date)) continue;
            //ກວດສອບ Percent ກ່ອນຈະ ຕັດເງິນ
            BigDecimal percent = comp.getPercent() == null ? BigDecimal.ZERO : comp.getPercent();
            // get mappers for company
            List<AutoDebitAccountMapperEntity> maps = mapperRepo.findByPartnerName(String.valueOf(comp.getCompanyCode()));

            for (AutoDebitAccountMapperEntity m : maps) {
                if (m.getStatus() == null || m.getStatus() != 1) continue; // only open

                // check account
                var accOpt = accountRepo.findByPartNerNameAndAccountCcy(m.getPartnerName(), m.getFromAcctCcy());
                if (accOpt.isEmpty()) continue;

                AutoDebitAccountEntity acc = accOpt.get();

                // check if transaction already exists for this account/date
                Optional<AutoDebitAccountTxnEntity> checkTxn = txnRepo.findByFromAcctNoAndTxnDate(m.getFromAcctNo(), date);
                if (checkTxn.isPresent()) {
                    log.info("Transaction already exists for account {} on date {}", m.getFromAcctNo(), date);
                    continue; // skip insert
                }

                // insert new transaction
                AutoDebitAccountTxnEntity mapperTxn = mapperInsertDataToAccountTxn(percent, comp, m, acc);

                // call fund transfer
                fundTransferResAPIStep01(comp, m, acc, mapperTxn);
            }

        }
    }

    //ຍິງໄປຕັດເງິນ ຢູ່ T24
    public AutoDebitAccountTxnEntity mapperInsertDataToAccountTxn (BigDecimal percent, AutoDebitCompanyEntity comp,
                                                                    AutoDebitAccountMapperEntity m , AutoDebitAccountEntity acc  ){
        String reference = RefGenerator.generateReference();
        BigDecimal closing = fetchClosingBalance(m.getFromAcctNo());
        BigDecimal amount = closing.multiply(percent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // amount will now have exactly 2 decimal places
        System.out.println(amount); // e.g. 100.20
        AutoDebitAccountTxnEntity txn = new AutoDebitAccountTxnEntity();
        txn.setFromAcctNo(m.getFromAcctNo());
        txn.setFromAcctName(m.getFromAcctName());
        txn.setFromAcctCcy(m.getFromAcctCcy());
        txn.setFromAcctAmount(amount);
        txn.setToAcctNo(acc.getAccountNo());
        txn.setToAcctName(acc.getAccountName());
        txn.setToAcctCcy(acc.getAccountCcy());
        txn.setToAcctAmount(amount);
        txn.setTxnDate(LocalDate.from(LocalDateTime.now()));
        txn.setPercent(comp.getPercent());
        txn.setBalanceAmount(closing);
        txn.setToAcctAmount(amount);
        txn.setTotalAmount(amount);
        txn.setTxnType("ACTC");
        txn.setRemark("auto debit edl");
        txn.setRef("");
        txn.setReference(reference);
        txn.setCoreTxnDate(LocalDate.from(LocalDateTime.now()).atStartOfDay());///resposne from core bank
        txn.setCoreReq("");
        txn.setCoreRes("");
        txn.setStatus("SUCCESS");//check status from core bank
        txnRepo.save(txn);

        return txn;
    }

    private BigDecimal fetchClosingBalance(String accountNo) {
        log.info("Fetching closing balance for account {}", accountNo);
        List<AccountEntity> accountInfo = accountRepository.findAccountDetails(accountNo);

        if (accountInfo != null && !accountInfo.isEmpty()) {
            // take the first record (or handle multiple if needed)
            AccountEntity acc = accountInfo.get(0);
            log.info("Fetching closing balance for account {} closing balance {}", accountNo,acc.getWorkingBalance());
            return acc.getWorkingBalance() != null ? acc.getWorkingBalance() : BigDecimal.ZERO;
        }else {
            log.info("Fetching closing balance for account {} closing balance {}", accountNo,BigDecimal.ZERO);
            return BigDecimal.ZERO;
        }
    }

    public APIResponse<FundTransferRes<FundTransferDataResponse>> fundTransferResAPIStep01(AutoDebitCompanyEntity comp,
                                                                                           AutoDebitAccountMapperEntity m ,
                                                                                           AutoDebitAccountEntity acc,AutoDebitAccountTxnEntity txn  ) {

        APIResponse<FundTransferRes<FundTransferDataResponse>> apiResponse = new APIResponse<>();
        try {

            // Prepare the fund transfer request
            FundTransferReq requestBody = prepareFundTransferRequestStep01(comp,m,acc,txn);
            log.info("Prepared FundTransferReq: {}", requestBody);

            // Call core banking service
            APIResponse<FundTransferRes<FundTransferDataResponse>> coreBankingResponse = corebankService.shippingFundTransferCoreBanking(requestBody);
            log.info("Shipping FundTransferRes: {}", coreBankingResponse.getData());

            if (coreBankingResponse.getData() != null) {
                FundTransferRes<FundTransferDataResponse> responseData = coreBankingResponse.getData();

                // Handle insufficient funds scenario
                if ("01".equals(responseData.getStatus())) {
                    log.info("INSUFFICIENT_FUND !!!");
                    responseData.setStatus("01");
                    responseData.setMessage("INSUFFICIENT_FUND");
                    apiResponse.setHttpStatus(101);
                    apiResponse.setMessage("INSUFFICIENT_FUND");
                    apiResponse.setData(responseData);
                    //let update core
                    updateStatusAutoDebit(txn,requestBody,responseData);

                    return apiResponse;
                }

                log.info("Core Banking Response T24RefID: {}", responseData.getDataResponse().getT24RefID());

                // Validate response and handle non-success cases
                if (!Constant.SUCCESS_CODE.equals(responseData.getStatus())) {
                    responseData.setStatus("05");
                    responseData.setMessage("TIMEOUT");
                    apiResponse.setHttpStatus(102);
                    apiResponse.setMessage("TIMEOUT");
                    apiResponse.setData(responseData);
                    //let update core
                    updateStatusAutoDebit(txn,requestBody,responseData);
                    return apiResponse;
                }
                // Process and log successful transaction
                CoreBankDataResponse coreBankData = new ObjectMapper()
                        .convertValue(responseData.getDataResponse(), CoreBankDataResponse.class);
                log.info("Transaction Successful, T24RefID: {}", coreBankData.getT24RefID());
                //let update core
                updateStatusAutoDebit(txn,requestBody,responseData);

                return coreBankingResponse;
            } else {
                FundTransferRes<FundTransferDataResponse> responseData = new FundTransferRes<>();
                responseData.setStatus("103");
                responseData.setMessage("Invalid response");
                apiResponse.setHttpStatus(103);
                apiResponse.setMessage("Invalid response received from core banking service");
                //let update core
                updateStatusAutoDebit(txn,requestBody,responseData);
                return apiResponse;
            }

        } catch (ResourceAccessException e) {
            log.error("Request timed out: {}", e.getMessage());
            apiResponse.setHttpStatus(HttpStatus.REQUEST_TIMEOUT.value());
            apiResponse.setMessage("REQUEST TIMEOUT: Unable to access resource");
            return apiResponse;
        } catch (Exception ex) {
            log.error("An unexpected error occurred during fund transfer", ex);
            apiResponse.setHttpStatus(500);
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return apiResponse;
        }
    }

    private FundTransferReq prepareFundTransferRequestStep01(AutoDebitCompanyEntity comp,
                                                             AutoDebitAccountMapperEntity m , AutoDebitAccountEntity acc,AutoDebitAccountTxnEntity txn ) {
        // Construct transaction reference
        String txnRef = String.format(
                "auto*debit*%s*%s",
                txn.getReference(),
                comp.getCompanyName()
        );

        String details = String.format(
                "Auto Debit =%s ",
                comp.getCompanyName()
        );

        BigDecimal totalPayAmount = txn.getTotalAmount();
        BigDecimal feeAmount = BigDecimal.valueOf(0.0);

        // Build and return the FundTransferReq object
        return FundTransferReq.builder()
                .action("PROCESS")
                .txnType("ACMB")
                .debitAcct(m.getFromAcctNo())
                .debitCurrency(m.getFromAcctCcy())
                .debitAmount("") // Populate if needed
                .creditAcct(acc.getAccountNo())
                .creditCurrency(acc.getAccountCcy())
                .creditAmount(totalPayAmount.toString())
                .customerName(comp.getCompanyName())
                .mobileNumber("1234567890") // Replace with dynamic value if applicable
                .billNo(txn.getReference())
                .detail(details)
                .txnRef(txnRef)
                .chargeType("SOKXAYLOT")
                .chargeCurrency(acc.getAccountCcy())
                .chargeAmount(feeAmount.toString())
                .build();
    }

    public APIResponse<ReversalRes> revertT24(APIResponse<FundTransferRes<FundTransferDataResponse>> fundTransferResAPIResponse) throws Exception {
        ReversalReq reversalTransferReq = new ReversalReq();
        reversalTransferReq.setTxnRef(fundTransferResAPIResponse.getData().getDataResponse().getT24RefID());
        APIResponse<ReversalRes> revertT24 =  corebankService.reversal(reversalTransferReq);
        log.info("revertT24 Response : {}",revertT24.getData());
        return revertT24;
    }

    public void updateStatusAutoDebit(AutoDebitAccountTxnEntity accountTxn,
                                      FundTransferReq fundTransferReq,
                                      FundTransferRes<FundTransferDataResponse> fundTransferResAPIResponse) {
        try {
            Optional<AutoDebitAccountTxnEntity> txnEntityOpt = txnRepo.findById(accountTxn.getKeyId());

            if (txnEntityOpt.isEmpty()) {
                log.warn("No AutoDebit transaction found with keyId={}", accountTxn.getKeyId());
                return;
            }

            AutoDebitAccountTxnEntity txn = txnEntityOpt.get();

            // Map core status codes using enum
            String coreStatus = fundTransferResAPIResponse != null ? fundTransferResAPIResponse.getStatus() : null;
            String mappedStatus = CoreBankStatus.fromCode(coreStatus);

            txn.setCoreTxnDate(LocalDateTime.now());
            txn.setCoreReq(fundTransferReq != null ? fundTransferReq.toString() : "N/A");
            txn.setCoreRes(fundTransferResAPIResponse != null ? fundTransferResAPIResponse.toString() : "N/A");

            if (fundTransferResAPIResponse != null
                    && fundTransferResAPIResponse.getDataResponse() != null
                    && fundTransferResAPIResponse.getDataResponse().getT24RefID() != null) {
                txn.setRef(fundTransferResAPIResponse.getDataResponse().getT24RefID());
            }

            txn.setStatus(mappedStatus);

            txnRepo.save(txn);

            log.info("AutoDebit transaction updated successfully with keyId={}, ref={}, status={}",
                    txn.getKeyId(), txn.getRef(), mappedStatus);

        } catch (Exception ex) {
            log.error("Failed to update AutoDebit transaction status for keyId={}", accountTxn.getKeyId(), ex);
            throw new RuntimeException("Error updating AutoDebit transaction", ex);
        }
    }

    public enum CoreBankStatus {
        SUCCEEDED("00", "SUCCEEDED"),
        TIMEOUT("05", "TIMEOUT"),
        INSUFFICIENT_FUND("01", "INSUFFICIENT_FUND"),
        INVALID_RESPONSE("103", "INVALID_RESPONSE"),
        UNKNOWN(null, "UNKNOWN");

        private final String code;
        private final String description;

        CoreBankStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public static String fromCode(String code) {
            for (CoreBankStatus status : values()) {
                if (status.code != null && status.code.equals(code)) {
                    return status.description;
                }
            }
            return UNKNOWN.description;
        }
    }

}


