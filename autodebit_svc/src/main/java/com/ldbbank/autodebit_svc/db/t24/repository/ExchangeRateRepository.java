package com.ldbbank.autodebit_svc.db.t24.repository;

import com.ldbbank.autodebit_svc.db.t24.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, String> {

    // Native query using Oracle XMLTABLE
    @Query(
            value = """
            SELECT
                x.RECID AS RECID,
                t.BUY_RATE,
                t.SELL_RATE,
                TO_CHAR(SYSDATE, 'DD-MON-YY') AS EXCHANGE_DATE,
                CASE WHEN x.RECID = 'USD' THEN 3
                     WHEN x.RECID = 'THB' THEN 2
                     WHEN x.RECID = 'CNY' THEN 1
                     ELSE 0 END AS TYPE
            FROM T24USR.FBNK_CURRENCY x,
                 XMLTABLE(
                     '$t/row'
                     PASSING x.XMLRECORD AS "t"
                     COLUMNS
                         BUY_RATE  NUMBER PATH 'c16[1]',
                         SELL_RATE NUMBER PATH 'c17[1]'
                 ) t
            WHERE x.RECID = :currencyCode
            OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY
            """,
            nativeQuery = true
    )
    List<ExchangeRateEntity> findExchangeRate(@Param("currencyCode") String currencyCode);
}
