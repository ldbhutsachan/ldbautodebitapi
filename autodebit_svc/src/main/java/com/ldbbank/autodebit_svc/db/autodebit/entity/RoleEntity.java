package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
