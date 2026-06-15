package com.ldbbank.autodebit_svc.db.autodebit.entity;

import lombok.Data;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "USER_LOGIN")
public class UserDbEntity {

    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

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

    @Column(name = "USER_NAME", length = 200)
    private String userName;

    @Column(name = "SECTION_NO", length = 100)
    private String sectionNo;

    @Column(name = "NAME", length = 200)
    private String name;

    @Column(name = "MOBILE", length = 100)
    private String mobile;

    @Column(name = "MAIL", length = 200)
    private String mail;

}
