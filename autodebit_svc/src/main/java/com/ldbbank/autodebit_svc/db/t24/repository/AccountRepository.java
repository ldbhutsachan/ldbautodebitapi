package com.ldbbank.autodebit_svc.db.t24.repository;

import com.ldbbank.autodebit_svc.db.t24.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {


    // Native query using Oracle XMLTABLE
    @Query(
            value = """
            SELECT
                x.RECID AS RECID,
                t.SHORT_TITLE,
                t.CATEGORY,
                DECODE(t.CATEGORY, 6001, 'SAVING', 1001, 'CURRENT', t.CATEGORY) AS CATEGORY_NAME,
                t.CURRENCY,
                t.CUSTOMER,
                DECODE(t.WORKING_BALANCE, NULL, 0, t.WORKING_BALANCE) AS WORKING_BALANCE,
                t.INACTIV_MARKER
            FROM T24USR.FBNK_ACCOUNT x,
                 XMLTABLE(
                     '$t/row'
                     PASSING x.XMLRECORD AS "t"
                     COLUMNS
                         SHORT_TITLE     VARCHAR2(100) PATH 'c3[1]',
                         CATEGORY        VARCHAR2(6)   PATH 'c2',
                         CURRENCY        VARCHAR2(3)   PATH 'c8',
                         CUSTOMER        VARCHAR2(10)  PATH 'c1',
                         WORKING_BALANCE NUMBER        PATH 'c27',
                         INACTIV_MARKER  VARCHAR2(1)   PATH 'c22'
                 ) t
            WHERE x.RECID = :accountNo
            """,
            nativeQuery = true
    )
    List<AccountEntity> findAccountDetails(@Param("accountNo") String accountNo);
}
