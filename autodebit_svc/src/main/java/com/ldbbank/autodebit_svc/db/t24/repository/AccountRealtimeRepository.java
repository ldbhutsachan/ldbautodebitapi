package com.ldbbank.autodebit_svc.db.t24.repository;

import com.ldbbank.autodebit_svc.db.t24.entity.AccountRealtimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRealtimeRepository extends JpaRepository<AccountRealtimeEntity, String> {

    // Native query using Oracle XMLTABLE — realtime account monitor (dashboard2).
    // CIF, ACCOUNT_OFFICER, BRANCH_CODE and OPENING_DATE come straight off FBNK_ACCOUNT;
    // the rest is extracted from the multivalue XMLRECORD via XMLTABLE.
    @Query(
            value = """
            SELECT
                x.RECID           AS RECID,
                t.CUSTOMER        AS CIF,
                t.CATEGORY        AS CATEGORY,
                t.SHORT_TITLE     AS ACCOUNT_NAME,
                x.ACCOUNT_OFFICER AS ACCOUNT_OFFICER,
                x.CO_CODE         AS BRANCH_CODE,
                t.CURRENCY        AS CCY,
                t.WORKING_BALANCE AS BALANCE,
                t.INACTIV_MARKER  AS INACTIVE_FLAG,
                x.OPENING_DATE    AS OPENING_DATE
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
            WHERE x.RECID IN (:accountNos)
            ORDER BY x.RECID ASC
            """,
            nativeQuery = true
    )
    List<AccountRealtimeEntity> findAccountsRealtime(@Param("accountNos") List<String> accountNos);

    @Query(
            value = """
            SELECT
                x.RECID           AS RECID,
                t.CUSTOMER        AS CIF,
                t.CATEGORY        AS CATEGORY,
                t.SHORT_TITLE     AS ACCOUNT_NAME,
                x.ACCOUNT_OFFICER AS ACCOUNT_OFFICER,
                x.CO_CODE         AS BRANCH_CODE,
                t.CURRENCY        AS CCY,
                t.WORKING_BALANCE AS BALANCE,
                t.INACTIV_MARKER  AS INACTIVE_FLAG,
                x.OPENING_DATE    AS OPENING_DATE
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
            WHERE x.RECID = :accountNos
            ORDER BY x.RECID ASC
            """,
            nativeQuery = true
    )
    List<AccountRealtimeEntity> findAccountsRealtimeByAccNo(@Param("accountNos") String accountNos);
}
