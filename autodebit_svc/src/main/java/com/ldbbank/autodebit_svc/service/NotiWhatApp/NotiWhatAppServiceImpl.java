package com.ldbbank.autodebit_svc.service.NotiWhatApp;



import com.ldbbank.autodebit_svc.db.autodebit.entity.VvRpTxnEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.WhatAppDbEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.VvRpTxnRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.WhatAppRepository;
import com.ldbbank.autodebit_svc.db.t24.repository.AccountRepository;
import com.ldbbank.autodebit_svc.model.whatapp.WhatAppNotiMsg;
import com.ldbbank.autodebit_svc.service.NotiWhatAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class NotiWhatAppServiceImpl  implements NotiWhatAppService {
    private final WhatAppRepository vvRpTxnRepository;
    @Override
    public String mapMsg() {
        OkHttpClient client = new OkHttpClient();

        // Convert date
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy"); // matches DB format
        String formattedDate = yesterday.format(formatter);

        // Query DB
        List<WhatAppDbEntity> mapTxn = vvRpTxnRepository.findAll();

        // Build message
        StringBuilder sb = new StringBuilder();
        sb.append("ຂ້າພະເຈົ້າ ຕາງໜ້າຂະເເໜ່ງສິນເຊື່ອ, ຂໍອະນຸຍາດລາຍງານການເຄື່ອນໄຫວບັນຊີຂອງ ບໍລິສັດ ໄຟຟ້າລາວ ຈໍາກັດ ປະຈໍາວັນທີ: ")
                .append(formattedDate)
                .append(" ເຊິ່ງມີລາຍລະອຽດດັ່ງນີ້:\n");

        for (WhatAppDbEntity txn : mapTxn) {
            sb.append(String.format("ສາຂາ: %s | ສະກຸນເງິນ: %s | ບໍລິສັດ: %s | ວັນທີ: %s | ຈໍານວນ: %,.2f\n",
                    txn.getBranchName(),
                    txn.getToAcctCcy(),
                    txn.getCompanyName(),
                    txn.getTxnDate(),
                    txn.getToAcctAmount()));
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
