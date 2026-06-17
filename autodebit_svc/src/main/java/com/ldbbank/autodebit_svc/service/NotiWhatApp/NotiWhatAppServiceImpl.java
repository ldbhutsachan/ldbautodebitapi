package com.ldbbank.autodebit_svc.service.NotiWhatApp;

import com.ldbbank.autodebit_svc.db.autodebit.entity.WhatAppDbEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.WhatAppRepository;
import com.ldbbank.autodebit_svc.service.NotiWhatAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class NotiWhatAppServiceImpl  implements NotiWhatAppService {
    private final WhatAppRepository vvRpTxnRepository;

    @Override
    public String mapMsg() {
        OkHttpClient client = new OkHttpClient();

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = yesterday.format(formatter);

        // Query DB
        List<String> curr= Arrays.asList("USD", "THB", "CNY","LAK");
        List<WhatAppDbEntity> mapTxn = vvRpTxnRepository.findAllByToAcctCcy(curr);
        // Define the currencies you always want to show
        List<String> currencies = Arrays.asList("LAK", "USD", "THB", "CNY");

        // Group by branch name, then by currency
        Map<String, Map<String, BigDecimal>> groupedTotals =
                mapTxn.stream().collect(Collectors.groupingBy(
                        WhatAppDbEntity::getBranchName,
                        Collectors.groupingBy(
                                WhatAppDbEntity::getToAcctCcy,
                                Collectors.reducing(BigDecimal.ZERO,
                                        WhatAppDbEntity::getToAcctAmount,
                                        BigDecimal::add)
                        )
                ));

        // Build message
        StringBuilder sb = new StringBuilder();
        sb.append("ຂ້າພະເຈົ້າ ຕາງໜ້າຂະເເໜ່ງສິນເຊື່ອ, ຂໍອະນຸຍາດລາຍງານການຕັດບັນຊີຂອງ ບໍລິສັດ ໄຟຟ້າລາວ ປະຈໍາວັນທີ: ")
                .append(formattedDate)
                .append(" ເຊິ່ງມີລາຍລະອຽດດັ່ງນີ້:\n");

        // After building groupedTotals (branch -> currency -> sum)
        Map<String, BigDecimal> grandTotals = new HashMap<>();

        for (Map.Entry<String, Map<String, BigDecimal>> branchEntry : groupedTotals.entrySet()) {
            String branchName = branchEntry.getKey();
            sb.append("\n--- ສາຂາ: ").append(branchName).append(" ---\n");

            Map<String, BigDecimal> totalsByCurrency = branchEntry.getValue();

            // Always show all currencies, defaulting to 0 if missing
            for (String currency : currencies) {
                BigDecimal total = totalsByCurrency.getOrDefault(currency, BigDecimal.ZERO);
                sb.append(String.format("   ສະກຸນເງິນ: %s | ລວມຍອດ: %,.2f\n", currency, total));

                // accumulate into grand totals
                grandTotals.merge(currency, total, BigDecimal::add);
            }
        }

        // Print grand totals at the bottom
        sb.append("\n=== ລວມຍອດທັງໝົດ ===\n");
        for (String currency : currencies) {
            BigDecimal total = grandTotals.getOrDefault(currency, BigDecimal.ZERO);
            sb.append(String.format("   ສະກຸນເງິນ: %s | ລວມຍອດທັງໝົດ: %,.2f\n", currency, total));
        }


        sb.append("\n   ດັ່ງນັ້ນ ຈິ່ງຮຽນລາຍງານມາຍັງທ່ານ ເພື່ອຊາບ");
        sb.append("\n   ນ ເອມີ ");
        sb.append("\n   ຂໍຂອບໃຈ");

        String messageMapped = sb.toString();
        log.info("Message mapped: {}", messageMapped);

        // Send to WhatsApp API
        FormBody body = new FormBody.Builder()
                .add("token", "c0uvwpufi0cqii14")
                .add("to", "120363426180144831@g.us")
                .add("body", messageMapped)
                .build();

        Request apiRequest = new Request.Builder()
                .url("https://api.ultramsg.com/instance115088/messages/chat")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(apiRequest).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return "Message sent successfully: " + response.body().string();
            } else {
                return "Failed to send message. HTTP Code: " + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while sending the message.";
        }
    }


}

