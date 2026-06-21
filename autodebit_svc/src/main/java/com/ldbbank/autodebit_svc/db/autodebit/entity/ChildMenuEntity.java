package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CHILD_MENU")
public class ChildMenuEntity {

    @Id
    @Column(name = "CH_ID", nullable = false)
    private Long chId;

    @Column(name = "CH_NAME", length = 200)
    private String chName;

    @Column(name = "MENU_ID", length = 202)
    private String menuId;

 @Column(name = "CH_PATH", length = 202)
    private String chPath;
 @Column(name = "CH_ICON", length = 202)
    private String chIcon;

    @Column(name = "ORDER_TYPE")
    private Integer orderType;

}
