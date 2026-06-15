package com.ldbbank.autodebit_svc.db.autodebit.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "SECTION")
public class SectionEntity {

    @Id
    @Column(name = "KEY_ID", nullable = false)
    private Long keyId;

    @Column(name = "SECTION_NO", length = 30)
    private String sectionNo;

    @Column(name = "SECTION_NAME", length = 100)
    private String sectionName;

}
