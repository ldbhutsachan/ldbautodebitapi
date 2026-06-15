package com.ldbbank.autodebit_svc.db.autodebit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "VV_USER")
public class VvUserEntity {

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "USER_NAME", length = 200)
    private String userName;

    @Column(name = "NAME", length = 200)
    private String name;

    @Column(name = "MOBILE", length = 100)
    private String mobile;

    @Column(name = "MAIL", length = 200)
    private String mail;

    @Column(name = "ROLE_NAME", length = 128)
    private String roleName;

    @Column(name = "ACCOUNT_NON_EXPIRED")
    private Long accountNonExpired;

    @Column(name = "ACCOUNT_NON_LOCKED")
    private Long accountNonLocked;

    @Column(name = "CREDENTIALS_NON_EXPIRED")
    private Long credentialsNonExpired;

    @Column(name = "ENABLED")
    private Long enabled;

    @Column(name = "PASSWORD", length = 128)
    private String password;

    @Column(name = "MENU_NO")
    private Long menuNo;

    @Column(name = "MENU_NAME", length = 128)
    private String menuName;

    @Column(name = "MENU_PATH", length = 128)
    private String menuPath;

    @Column(name = "MENU_ICON", length = 128)
    private String menuIcon;

    @Column(name = "ORDER_TYPE")
    private Integer orderType;

}
