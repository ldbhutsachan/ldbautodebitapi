package com.ldbbank.autodebit_svc.db.autodebit.entity;

import jakarta.persistence.*;
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
