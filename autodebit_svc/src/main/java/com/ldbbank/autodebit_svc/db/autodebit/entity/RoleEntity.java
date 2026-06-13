package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ROLE")
public class RoleEntity {

    @Id
    @Column(name = "ROLE_NO", nullable = false)
    private Long roleNo;

    @Column(name = "ROLE_NAME", length = 128)
    private String roleName;

    @Column(name = "SEC_NO", length = 100)
    private String secNo;

}
