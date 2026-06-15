package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MAP_USER_MENU")
public class MapUserMenuEntity {

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "MENU_ID")
    private Long menuId;

    @Column(name = "USER_ID")
    private Long userId;

}
