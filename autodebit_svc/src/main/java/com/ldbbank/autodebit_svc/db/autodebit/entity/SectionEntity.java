package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

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
