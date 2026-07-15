package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.db.t24.entity.AccountEntity;
import com.ldbbank.autodebit_svc.db.t24.entity.ExchangeRateEntity;
import com.ldbbank.autodebit_svc.db.t24.repository.AccountRepository;
import com.ldbbank.autodebit_svc.excaption.ApiResponse;
import com.ldbbank.autodebit_svc.model.corebank.APIResponse;
import com.ldbbank.autodebit_svc.service.CorebankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/t24")
public class T24Controller {
    private final AccountRepository accountRepository;
    private final CorebankService corebankService;

    @PostMapping("/t24Account")
    public ResponseEntity<?> t24Account(
            @RequestParam(value = "accountNo", required = false) String accountNo) {
        try {
            List<AccountEntity> accountMapper = accountRepository.findAccountDetails(accountNo);

            if (accountMapper.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>("01", "ບໍ່ພົບຂໍ້ມູນ !!!", null));
            }

            return ResponseEntity.ok(new ApiResponse<>("00", "ສໍາເລັດ !!!", accountMapper));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse<>("05", "ບໍ່ສາມາດຄົ້ນຫາໄດ້ !!!  : " + ex.getMessage(), null));
        }
    }

    //
    @PostMapping("/rate")
    public ResponseEntity<?> exchangeRateFromCcy(){
        String currencyCode = "THB";
        APIResponse<ExchangeRateEntity> getExchangeRate =  corebankService.getExchangeRate(currencyCode);

        return ResponseEntity.ok(new ApiResponse<>("00", "ສໍາເລັດ !!!", getExchangeRate));
    }
}
